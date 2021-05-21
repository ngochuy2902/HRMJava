package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillsDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty(value = "updated_by", access = JsonProperty.Access.WRITE_ONLY)
    private Integer updatedBy;

    @JsonProperty("name")
    @NotBlank(message = "name must not be blank")
    private String name;

    public SkillsDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
