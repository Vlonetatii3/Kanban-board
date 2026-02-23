package mmach.tasks.dto.statuses;

import jakarta.validation.constraints.Size;

public class StatusUpdateRequest {

    @Size(max = 80)
    private String name;

    public StatusUpdateRequest(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
