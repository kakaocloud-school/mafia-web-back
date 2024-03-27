package com.gg.mafia.domain.profile.dto;

import com.gg.mafia.domain.profile.domain.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    RankResponse toRankResponse(Profile profile);
}
