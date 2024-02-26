package com.gg.mafia.domain.member.dao;


import com.gg.mafia.domain.member.domain.User;
import java.util.Optional;

public interface UserDaoCustom {
    Optional<User> findByEmail(String email);
}
