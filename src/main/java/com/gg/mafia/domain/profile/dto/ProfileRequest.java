package com.gg.mafia.domain.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {
    private Long id;
    private Long userId;
    private String userName;
    private String description;
    private int rating;
    private int ranking;
    private float mafiaOdd;
    private float citizenOdd;
    private float policeOdd;
    private float doctorOdd;
    private float averageOdd;
}
