package com.gg.mafia.domain.Profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ProfileResponse {
    private String description;
    private int rating;
    private Long Id;
    private String user_id;
}
