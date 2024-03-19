package com.gg.mafia.domain.record.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameSearchRequest {
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    private Integer roundGte;

    private Integer roundLte;

    private Boolean mafiaWon;

    private Integer cureSuccessCountGte;

    private Integer cureSuccessCountLte;

    private Integer killSuccessCountGte;

    private Integer killSuccessCountLte;

    private Integer detectSuccessCountGte;

    private Integer detectSuccessCountLte;

    private Long userId;
}