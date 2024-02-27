package com.gg.mafia.domain.member.dto;

import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.member.domain.UserToRole;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "username", source = "email")
    @Mapping(target = "authorities", source = "userToRoles", qualifiedByName = "getAuthorities")
    UserDetailsImpl toPrinciple(User user);

    User toEntity(SignupRequest request);

    @Named("getAuthorities")
    default List<SimpleGrantedAuthority> getAuthorities(List<UserToRole> userToRoles) {
        return userToRoles.stream().map((userToRole -> {
            String roleName = userToRole.getRoleName();
            return new SimpleGrantedAuthority(roleName);
        })).toList();
    }
}
