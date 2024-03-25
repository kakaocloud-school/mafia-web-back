package com.gg.mafia.domain.comment.dto;

import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.Profile.dto.ProfileRequest;
import com.gg.mafia.domain.comment.domain.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


public interface CommentMapper  {
    @Mapping(source="user.id", target = "userId")
     CommentResponse toCommentResponse(Comment comment) ;
    @Mapping(source = "userId", target = "user", qualifiedByName = "idToUser")
    Comment toEntity(CommentRequest request);


}
