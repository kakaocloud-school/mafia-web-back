package com.gg.mafia.domain.member.dao;

import java.util.Optional;
import com.gg.mafia.domain.member.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserDao extends JpaRepository<User, Long>, UserDaoCustom {
    @Override
    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<User> findById(@NonNull Long id);

    @Override
    Optional<User> findByEmail(String email);
}
