package com.gg.mafia.domain.comment.dto;

import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.member.domain.User;

import java.sql.Date;

public class CommentResponse {

    private Long id;
    private Long userId;
    private Long writeId;
    private String comment;
    private Date date;

}
