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
    private String imageUrl;
    private int rating;
    private float mafiaWinningRate;
    private float citizenWinningRate;
    private float policeWinningRate;
    private float doctorWinningRate;
    private float winningRate;
}
