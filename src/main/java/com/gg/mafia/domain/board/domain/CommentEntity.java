package com.gg.mafia.domain.board.domain;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="Comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id; //댓글 id

    @Column(nullable = false)
    private String comment; //방명록 댓글

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name ="Profile")
    private String profile_id; //프로필id

    @ManyToOne
    @JoinColumn(name = "User")
    private String User_id;//유저id

}
