package com.gg.mafia.domain.profile.dao;

import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.profile.dto.ProfileSearchRequest;
import com.gg.mafia.global.common.request.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfileDaoCustom {
    String RANK_CITIZEN = "citizen";
    String RANK_MAFIA = "mafia";
    String RANK_DOCTOR = "doctor";
    String RANK_POLICE = "police";
    String RANK_RATING = "rating";

    Page<Profile> findAllRanks(Pageable pageable);

    Page<Profile> search(ProfileSearchRequest dto, SearchQuery searchQuery, Pageable pageable);
}
