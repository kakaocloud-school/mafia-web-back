package com.gg.mafia.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class RankResponse {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    private String title;

    private String content;

    private String description;

    private Long userId;

    private String userName;

    private int rating;

    private float mafiaWinningRate;

    private float citizenWinningRate;

    private float policeWinningRate;

    private float doctorWinningRate;

    private float winningRate;

    private Long rank;
}