package com.gg.mafia.domain.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameRoundSubQueryDto {
    private Long votedPlayerId;

    private Long killedPlayerId;

    private Long curedPlayerId;

    private Long detectedPlayerId;
}