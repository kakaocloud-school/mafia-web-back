package com.gg.mafia.domain.member.dto;

import com.gg.mafia.domain.member.domain.User;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-27T13:34:12+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDetailsImpl toPrinciple(User user) {
        if ( user == null ) {
            return null;
        }

        String username = null;
        Collection<? extends GrantedAuthority> authorities = null;
        String password = null;

        username = user.getEmail();
        authorities = getAuthorities( user.getUserToRoles() );
        password = user.getPassword();

        UserDetailsImpl userDetailsImpl = new UserDetailsImpl( username, password, authorities );

        return userDetailsImpl;
    }

    @Override
    public User toEntity(SignupRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( request.getEmail() );
        user.password( request.getPassword() );

        return user.build();
    }
}
