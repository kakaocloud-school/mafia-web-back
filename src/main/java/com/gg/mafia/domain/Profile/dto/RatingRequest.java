package com.gg.mafia.domain.Profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RatingRequest {
    @NotBlank
    private Long myId;
    @NotBlank
    private Long opponentId;

}
