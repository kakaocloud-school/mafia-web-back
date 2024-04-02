package com.gg.mafia.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FollowingResponse {
    private Long followingId;
    private String imageUrl;
    private Long followUserId;
    private String followUserNickname;

    @QueryProjection
    public FollowingResponse(
            Long followUserId,
            String imageUrl,
            String followUserNickname
    ) {
        this.followUserId = followUserId;
        this.imageUrl = imageUrl;
        this.followUserNickname = followUserNickname;
    }

    @QueryProjection
    public FollowingResponse(
            String imageUrl,
            String followUserNickname
    ) {
        this.imageUrl = imageUrl;
        this.followUserNickname = followUserNickname;
    }

    @QueryProjection
    public FollowingResponse(
            Long followingId,
            Long followUserId
    ) {
        this.followingId = followingId;
        this.followUserId = followUserId;
    }

}
