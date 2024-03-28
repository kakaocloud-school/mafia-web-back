package com.gg.mafia.domain.profile.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
public class RatingRequest {
    @NotBlank
    private List<Long> winnerTeamId;
    @NotBlank
    private List<Long> loserTeamId;

}
