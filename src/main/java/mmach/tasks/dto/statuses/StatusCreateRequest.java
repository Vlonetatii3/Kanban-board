package mmach.tasks.dto.statuses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StatusCreateRequest {

    @NotBlank
    @Size(max = 80)
    private String name;

    public StatusCreateRequest() {}

    public StatusCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
