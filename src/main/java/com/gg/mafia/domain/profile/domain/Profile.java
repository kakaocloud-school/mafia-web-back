package com.gg.mafia.domain.profile.domain;

import com.gg.mafia.domain.comment.domain.Comment;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Profile extends BaseEntity {
    @Column(nullable = false)
    private String description = "안녕하세요";

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;
    @OneToMany(mappedBy = "profile", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private int rating = 0;

    @Column(nullable = false)
    private float mafiaWinningRate = 0;

    @Column(nullable = false)
    private float citizenWinningRate = 0;

    @Column(nullable = false)
    private float policeWinningRate = 0;

    @Column(nullable = false)
    private float doctorWinningRate = 0;

    @Column(nullable = false)
    private float winningRate = 0;

    @Builder
    public Profile(User user, String userName) {
        setUser(user);
        this.userName = userName;
    }

    public Long getUserId() {
        return user.getId();
    }

    private void setUser(User user) {
        if (this.user != null) {
            user.setProfile(null);
        }
        this.user = user;
        user.setProfile(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}