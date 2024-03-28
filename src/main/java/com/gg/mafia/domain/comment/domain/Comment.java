package com.gg.mafia.domain.comment.domain;


import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import com.gg.mafia.domain.profile.domain.Profile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {
    private String content;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "profile_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Profile profile;

    @Builder
    public Comment(String content, User user, Profile profile) {
        this.content = content;
        this.user = user;
        setProfile(profile);
    }

    public void updateComment(String updateComment) {
        this.content = updateComment;
    }

    public void setProfile(Profile profile) {
        if (this.profile != null) {
            this.profile.removeComment(this);
        }
        profile.addComment(this);
        this.profile = profile;
    }
}
