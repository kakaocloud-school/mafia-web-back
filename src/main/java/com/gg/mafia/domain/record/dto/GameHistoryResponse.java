package com.gg.mafia.domain.record.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gg.mafia.domain.record.domain.JobEnum;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class GameHistoryResponse {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    private Integer totalRound;

    private Boolean mafiaWon;

    private Boolean survival;

    private JobEnum job;

    @QueryProjection
    public GameHistoryResponse(
            Long id,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Integer totalRound,
            Boolean mafiaWon,
            Boolean survival,
            JobEnum job
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalRound = totalRound;
        this.mafiaWon = mafiaWon;
        this.survival = survival;
        this.job = job;
    }
}