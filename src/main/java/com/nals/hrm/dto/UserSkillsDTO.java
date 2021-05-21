package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSkillsDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("level")
    private String level;

    @JsonProperty("skill_id")
    private Integer skillId;

    @JsonProperty("userId")
    private Integer userId;
}
