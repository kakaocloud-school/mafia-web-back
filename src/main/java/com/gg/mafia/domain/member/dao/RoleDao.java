package com.gg.mafia.domain.member.dao;

import com.gg.mafia.domain.member.domain.Role;
import com.gg.mafia.domain.model.RoleEnum;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface RoleDao extends JpaRepository<Role, Long>, RoleDaoCustom {
    @Override
    @NonNull
    Page<Role> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<Role> findById(@NonNull Long id);

    @Override
    Optional<Role> findByValue(RoleEnum value);
}
