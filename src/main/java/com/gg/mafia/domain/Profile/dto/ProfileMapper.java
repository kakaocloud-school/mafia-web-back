package com.gg.mafia.domain.Profile.dto;

import com.gg.mafia.domain.Profile.dao.ProfileDao;
import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    //user.id를 userId에 맵핑한다는것빼고 필드가 같음 이렇게 해도 될것같음
    @Mapping(source = "user.id", target = "userId")
    ProfileResponse toProfileResponse(ProfileEntity profileEntity);
}
