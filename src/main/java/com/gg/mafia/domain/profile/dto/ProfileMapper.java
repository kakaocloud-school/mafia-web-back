package com.gg.mafia.domain.profile.dto;

import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {

    //user.id를 userId에 맵핑한다는것빼고 필드가 같음 이렇게 해도 될것같음
    @Mapping(source = "user.id", target = "userId")
    ProfileResponse toProfileResponse(Profile profile);

    //    @Mapping(target = "user.id", qualifiedByName = "find")
    @Mapping(source = "userId", target = "user", qualifiedByName = "idToUser")
    Profile toEntity(ProfileRequest request);


    @Named("idToUser")
    default User idToUser(Long id) {
        Optional<User> Ouser = userdao.findById(id);
        if (Ouser.isPresent()) {
            return Ouser.get();
        }
        throw new EntityNotFoundException("id에맞는 유저가없음");
    }

    @Mapping(source = "user.id", target = "userId")
    ProfileResponse entityToResponse(Profile entity);

    default Page<ProfileResponse> toProfileResponsePage(Page<Profile> profileEntityPage) {
        List<ProfileResponse> profileResponses = profileEntityPage.getContent()
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(profileResponses, profileEntityPage.getPageable(), profileEntityPage.getTotalElements());
    }

    @Autowired
    UserDao userdao = null;


}
