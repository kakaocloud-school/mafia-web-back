package com.gg.mafia.domain.Profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {
    @NotBlank
    private String userId;
    private String description;
    private int rating;
}
