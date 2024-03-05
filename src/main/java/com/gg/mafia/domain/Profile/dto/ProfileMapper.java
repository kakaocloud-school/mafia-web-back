package com.gg.mafia.domain.Profile.dto;

import com.gg.mafia.domain.Profile.dao.ProfileDao;
import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
//    @Named("find")
//    default User findUserByUserId(Long userId) {
//        Optional<User> Ouser = userdao.findById(userId);
//        if (Ouser.isPresent()) {
//            return Ouser.get();
//        }
//        throw new EntityNotFoundException("userid에 맞는 user가없음");
//    }

    //user.id를 userId에 맵핑한다는것빼고 필드가 같음 이렇게 해도 될것같음
    @Mapping(source = "user.id", target = "userId")
    ProfileResponse toProfileResponse(ProfileEntity profileEntity);

    //    @Mapping(target = "user.id", qualifiedByName = "find")
    ProfileEntity toEntity(ProfileRequest request);

    @Autowired
    UserDao userdao = null;

    //유저아이디로 유저찾음 맵핑위한 목적

}
