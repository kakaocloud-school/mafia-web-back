package com.gg.mafia.domain.comment.dao;

import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.member.domain.Role;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.RoleEnum;

import java.util.Optional;

public interface CommentDaoCustom {
    Optional<Comment> findByUser(User user);
}
