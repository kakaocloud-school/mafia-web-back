package com.gg.mafia.domain.profile.dto;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ProfileUpdateRequest {
    private String description;

    @URL
    private String imageUrl;
}
