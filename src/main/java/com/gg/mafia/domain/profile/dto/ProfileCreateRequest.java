package com.gg.mafia.domain.profile.dto;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ProfileCreateRequest {
    private Long userId;

    private String userName;

    private String description;

    @URL
    private String imageUrl;
}
