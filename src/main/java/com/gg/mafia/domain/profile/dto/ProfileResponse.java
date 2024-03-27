package com.gg.mafia.domain.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ProfileResponse {
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
