package com.gg.mafia.domain.record.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gg.mafia.domain.record.domain.JobEnum;
import com.gg.mafia.domain.record.domain.JobEnumDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionSuccessCountDto {
    private Long userId;

    @JsonDeserialize(using = JobEnumDeserializer.class)
    private JobEnum job;

    @JsonDeserialize(using = JobEnumDeserializer.class)
    private JobEnum actionBy;
}