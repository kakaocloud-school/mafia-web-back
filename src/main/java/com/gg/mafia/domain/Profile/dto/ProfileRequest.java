package com.gg.mafia.domain.Profile.dto;

import com.gg.mafia.domain.member.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {
    @NotBlank
    private User user;
    private String description;
    private int rating;
}
