package com.gg.mafia.domain.Profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RatingRequest {
    @NotBlank
    private String myName;
    @NotBlank
    private String opponentName;

}
