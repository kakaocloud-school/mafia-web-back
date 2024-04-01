package com.gg.mafia.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FollowingResponse {
    private Long followingId;
    private String imageUrl;
    private Long followeeId;
    private String followeeNickname;

    @QueryProjection
    public FollowingResponse(
            Long followeeId,
            String imageUrl,
            String followeeNickname
    ) {
        this.followeeId = followeeId;
        this.imageUrl = imageUrl;
        this.followeeNickname = followeeNickname;
    }

    @QueryProjection
    public FollowingResponse(
            String imageUrl,
            String followeeNickname
    ) {
        this.imageUrl = imageUrl;
        this.followeeNickname = followeeNickname;
    }

    @QueryProjection
    public FollowingResponse(
            Long followingId,
            Long followeeId
    ) {
        this.followingId = followingId;
        this.followeeId = followeeId;
    }

}
