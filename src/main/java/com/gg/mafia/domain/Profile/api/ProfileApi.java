package com.gg.mafia.domain.Profile.api;

import com.gg.mafia.domain.Profile.application.ProfileService;
import com.gg.mafia.domain.Profile.dto.ProfileResponse;
import com.gg.mafia.domain.member.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test1")
public class ProfileApi {
    @Autowired
    ProfileService profileService;

    @GetMapping(value = "/{id}")
    public ProfileResponse k(@PathVariable("id") Long id) {
        return profileService.getByUserId(id);
    }
}
