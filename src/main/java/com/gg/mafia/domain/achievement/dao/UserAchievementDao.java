package com.gg.mafia.domain.achievement.dao;

import com.gg.mafia.domain.achievement.domain.UserAchievement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface UserAchievementDao extends JpaRepository<UserAchievement, Long> {
    @Override
    @NonNull
    Optional<UserAchievement> findById(@NonNull Long id);

    @Query(value = "SELECT a FROM UserAchievement a left join fetch a.user u where a.user.id = :user_id")
    List<UserAchievement> findByUserId(@Param("user_id") Long user_id);

    List<UserAchievement> findByAchievementId(@NonNull Long achievement_id);
}
