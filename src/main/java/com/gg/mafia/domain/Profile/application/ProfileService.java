package com.gg.mafia.domain.Profile.application;

import com.gg.mafia.domain.Profile.dao.ProfileDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileDao profileDao;

}
