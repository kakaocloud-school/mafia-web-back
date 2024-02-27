package com.gg.mafia.domain.member.dao;


import com.gg.mafia.domain.member.domain.Role;
import com.gg.mafia.domain.model.RoleEnum;
import java.util.Optional;

public interface RoleDaoCustom {
    Optional<Role> findByValue(RoleEnum value);
}
