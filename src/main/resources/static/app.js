// === Config ===
const API_BASE = ""; // same host (Spring serving /static). If using another: "http://localhost:8080"

// === State ===
let statuses = [];
let tasks = [];
let searchQuery = "";

// === DOM ===
const boardEl = document.getElementById("board");
const modalEl = document.getElementById("modal");
const loadingEl = document.getElementById("loading");
const toastsEl = document.getElementById("toasts");

const btnOpenCreate = document.getElementById("btnOpenCreate");
const btnOpenCreateTop = document.getElementById("btnOpenCreateTop");
const btnCloseModal = document.getElementById("btnCloseModal");
const btnCancel = document.getElementById("btnCancel");
const btnRefresh = document.getElementById("btnRefresh");

const taskForm = document.getElementById("taskForm");
const taskName = document.getElementById("taskName");
const taskStatus = document.getElementById("taskStatus");
const modalTitle = document.getElementById("modalTitle");
const btnSubmit = document.getElementById("btnSubmit");

const kpiTotal = document.getElementById("kpiTotal");
const kpiStatuses = document.getElementById("kpiStatuses");

const searchInput = document.getElementById("searchInput");

// === Helpers ===
function setLoading(v) {
  loadingEl.classList.toggle("hidden", !v);
}

function toast(title, msg, type = "good") {
  const el = document.createElement("div");
  el.className = `toast ${type}`;
  el.innerHTML = `
    <div class="toast-title">${escapeHtml(title)}</div>
    <div class="toast-msg">${escapeHtml(msg)}</div>
  `;
  toastsEl.appendChild(el);
  setTimeout(() => {
    el.style.opacity = "0";
    el.style.transform = "translateY(6px)";
    el.style.transition = "all .25s ease";
    setTimeout(() => el.remove(), 260);
  }, 2800);
}

function escapeHtml(s) {
  return String(s ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function pickDotClass(index) {
  const dots = ["dot-blue", "dot-amber", "dot-green", "dot-slate", "dot-violet", "dot-rose", "dot-cyan"];
  return dots[index % dots.length];
}

async function api(path, opts = {}) {
  const res = await fetch(API_BASE + path, {
    headers: { "Content-Type": "application/json", ...(opts.headers || {}) },
    ...opts,
  });

  if (!res.ok) {
    let body = "";
    try { body = await res.text(); } catch {}
    throw new Error(`HTTP ${res.status} ${res.statusText}${body ? " • " + body : ""}`);
  }

  // 204 no content
  if (res.status === 204) return null;

  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

// === Data load ===
async function loadAll() {
  setLoading(true);
  try {
    // Fetch statuses first to build columns
    statuses = await api("/api/statuses");
    tasks = await api("/api/tasks");

    render();
    toast("Done", "Board updated", "good");
  } catch (e) {
    console.error(e);
    toast("Error", e.message || "Failed to load data", "bad");
  } finally {
    setLoading(false);
  }
}

// === Render ===
function render() {
  // KPIs
  kpiTotal.textContent = tasks.length;
  kpiStatuses.textContent = statuses.length;

  // Modal select
  taskStatus.innerHTML = statuses
    .map(s => `<option value="${s.statusId}">${escapeHtml(s.name)}</option>`)
    .join("");

  // Board columns
  const q = searchQuery.trim().toLowerCase();
  const filtered = q
    ? tasks.filter(t => (t.name || "").toLowerCase().includes(q))
    : tasks;

  boardEl.innerHTML = statuses.map((s, idx) => {
    const colTasks = filtered.filter(t => t.statusId === s.statusId);
    const dotClass = pickDotClass(idx);

    return `
      <section class="column" data-statusid="${s.statusId}">
        <div class="col-head">
          <div class="col-title">
            <span class="dot ${dotClass}"></span>
            <span>${escapeHtml(s.name)}</span>
          </div>
          <div class="count">${colTasks.length}</div>
        </div>

        <div class="col-body">
          ${colTasks.length ? colTasks.map(t => cardHtml(t, s, dotClass)).join("") : `
            <div class="empty">There are no tasks here.<br>Create one or move another.</div>
          `}
        </div>
      </section>
    `;
  }).join("");

  // Wire events for cards
  boardEl.querySelectorAll("[data-action]").forEach(btn => {
    btn.addEventListener("click", onCardAction);
  });

  boardEl.querySelectorAll("select[data-move]").forEach(sel => {
    sel.addEventListener("change", onMoveTask);
  });

  boardEl.querySelectorAll("button[data-edit]").forEach(btn => {
    btn.addEventListener("click", onEditTask);
  });

  boardEl.querySelectorAll("button[data-del]").forEach(btn => {
    btn.addEventListener("click", onDeleteTask);
  });
}

function cardHtml(task, status, dotClass) {
  const statusName = status?.name ?? "—";

  const options = statuses.map(s => {
    const selected = s.statusId === task.statusId ? "selected" : "";
    return `<option value="${s.statusId}" ${selected}>${escapeHtml(s.name)}</option>`;
  }).join("");

  return `
    <article class="card" data-taskid="${task.taskId}">
      <div class="card-top">
        <h3 class="card-title">${escapeHtml(task.name)}</h3>

        <div class="card-actions">
          <button class="icon-btn" type="button" title="Edit name" data-edit="true" data-taskid="${task.taskId}">✎</button>
          <button class="icon-btn" type="button" title="Delete" data-del="true" data-taskid="${task.taskId}">🗑</button>
        </div>
      </div>

      <div class="meta">
        <span class="badge"><span class="dot ${dotClass}"></span>${escapeHtml(statusName)}</span>

        <select class="select" data-move="true" data-taskid="${task.taskId}" title="Move to another status">
          ${options}
        </select>
      </div>
    </article>
  `;
}

// === Card actions ===
function onCardAction(e) {
  // placeholder (handlers are wired specifically)
}

async function onMoveTask(e) {
  const taskId = Number(e.target.getAttribute("data-taskid"));
  const newStatusId = Number(e.target.value);

  const t = tasks.find(x => x.taskId === taskId);
  if (!t) return;

  // Optimistic UI
  const prev = t.statusId;
  t.statusId = newStatusId;
  render();

  try {
    await api(`/api/tasks/${taskId}`, {
      method: "PATCH",
      body: JSON.stringify({ statusId: newStatusId }),
    });
    toast("Moved", "Task updated", "good");
  } catch (err) {
    // rollback
    t.statusId = prev;
    render();
    toast("Error", err.message || "Could not move task", "bad");
  }
}

async function onEditTask(e) {
  const taskId = Number(e.currentTarget.getAttribute("data-taskid"));
  const t = tasks.find(x => x.taskId === taskId);
  if (!t) return;

  const newName = prompt("Edit task name:", t.name ?? "");
  if (newName == null) return;

  const trimmed = newName.trim();
  if (!trimmed) return toast("Heads up", "Name cannot be empty", "bad");
  if (trimmed.length > 80) return toast("Heads up", "Max 80 characters", "bad");

  const prev = t.name;
  t.name = trimmed;
  render();

  try {
    await api(`/api/tasks/${taskId}`, {
      method: "PATCH",
      body: JSON.stringify({ name: trimmed }),
    });
    toast("Saved", "Name updated", "good");
  } catch (err) {
    t.name = prev;
    render();
    toast("Error", err.message || "Could not update name", "bad");
  }
}

async function onDeleteTask(e) {
  const taskId = Number(e.currentTarget.getAttribute("data-taskid"));
  const t = tasks.find(x => x.taskId === taskId);
  if (!t) return;

  const ok = confirm(`Delete task "${t.name}"?`);
  if (!ok) return;

  // Optimistic remove
  const prev = tasks.slice();
  tasks = tasks.filter(x => x.taskId !== taskId);
  render();

  try {
    await api(`/api/tasks/${taskId}`, { method: "DELETE" });
    toast("Deleted", "Task removed", "good");
  } catch (err) {
    tasks = prev;
    render();
    toast("Error", err.message || "Could not delete task", "bad");
  }
}

// === Modal (create) ===
function openModal() {
  modalTitle.textContent = "New task";
  btnSubmit.innerHTML = `<span class="ic">＋</span> Create task`;

  taskName.value = "";
  taskStatus.value = statuses[0]?.statusId ?? "";
  modalEl.classList.remove("hidden");
  setTimeout(() => taskName.focus(), 0);
}

function closeModal() {
  modalEl.classList.add("hidden");
}

modalEl.addEventListener("click", (e) => {
  if (e.target?.dataset?.close === "true") closeModal();
});

btnOpenCreate.addEventListener("click", openModal);
btnOpenCreateTop.addEventListener("click", openModal);
btnCloseModal.addEventListener("click", closeModal);
btnCancel.addEventListener("click", closeModal);

taskForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const name = taskName.value.trim();
  const statusId = Number(taskStatus.value);

  if (!name) return toast("Heads up", "Please enter a name", "bad");
  if (name.length > 80) return toast("Heads up", "Max 80 characters", "bad");
  if (!statusId) return toast("Heads up", "Please select a status", "bad");

  setLoading(true);
  try {
    const created = await api("/api/tasks", {
      method: "POST",
      body: JSON.stringify({ name, statusId }),
    });

    // API returns TaskResponse -> { taskId, name, statusId, ... }
    tasks.unshift(created);
    render();
    closeModal();
    toast("Created", "New task added", "good");
  } catch (err) {
    toast("Error", err.message || "Could not create task", "bad");
  } finally {
    setLoading(false);
  }
});

// === Search ===
searchInput.addEventListener("input", (e) => {
  searchQuery = e.target.value || "";
  render();
});

document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    if (!modalEl.classList.contains("hidden")) closeModal();
    searchInput.value = "";
    searchQuery = "";
    render();
  }
});

// === Refresh ===
btnRefresh.addEventListener("click", loadAll);

// === Init ===
loadAll();