package com.gg.mafia.domain.member.dao;


import java.util.Optional;
import com.gg.mafia.domain.member.domain.Role;
import com.gg.mafia.domain.member.model.RoleEnum;

public interface RoleDaoCustom {
    Optional<Role> findByValue(RoleEnum value);
}
