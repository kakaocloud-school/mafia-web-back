package com.gg.mafia.domain.comment.domain;


import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Profile;

import java.sql.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor

public class Comment extends BaseEntity{

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    //profile
    private ProfileEntity profile;

    @Column(columnDefinition="TEXT", nullable = false)
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="reg_date", nullable = false)
    // 방명록 작성 날짜를 받기 위한 column 생성.
    private Date regDate;
}