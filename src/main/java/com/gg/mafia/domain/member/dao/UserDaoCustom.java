package com.gg.mafia.domain.member.dao;


import java.util.Optional;
import com.gg.mafia.domain.member.domain.User;

public interface UserDaoCustom {
    Optional<User> findByEmail(String email);
}
