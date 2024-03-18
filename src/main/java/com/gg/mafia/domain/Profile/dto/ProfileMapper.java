package com.gg.mafia.domain.Profile.dto;

import com.gg.mafia.domain.Profile.dao.ProfileDao;
import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;


public interface ProfileMapper {

    //user.id를 userId에 맵핑한다는것빼고 필드가 같음 이렇게 해도 될것같음
    @Mapping(source = "user.id", target = "userId")
    ProfileResponse toProfileResponse(ProfileEntity profileEntity);

    //    @Mapping(target = "user.id", qualifiedByName = "find")
    @Mapping(source = "userId", target = "user", qualifiedByName = "idToUser")
    ProfileEntity toEntity(ProfileRequest request);


    @Named("idToUser")
    default User idToUser(Long id) {
        Optional<User> Ouser = userdao.findById(id);
        if (Ouser.isPresent()) {
            return Ouser.get();
        }
        throw new EntityNotFoundException("id에맞는 유저가없음");
    }

    @Mapping(source = "user.id", target = "userId")
    List<ProfileResponse> toProfileResponseList(List<ProfileEntity> profileEntityList);

    @Autowired
    UserDao userdao = null;


}
