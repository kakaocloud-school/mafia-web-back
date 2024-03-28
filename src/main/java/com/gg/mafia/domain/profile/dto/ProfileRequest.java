package com.gg.mafia.domain.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {
    private Long userId;
    private String description;
}
