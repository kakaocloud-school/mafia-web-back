package com.gg.mafia.domain.Profile.dto;

import com.gg.mafia.domain.member.domain.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileRequest {
    private Long id;
    @NotBlank
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
