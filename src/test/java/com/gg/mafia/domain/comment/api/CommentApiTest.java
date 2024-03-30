package com.gg.mafia.domain.comment.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gg.mafia.domain.comment.application.CommentService;
import com.gg.mafia.domain.comment.dto.CommentRequest;
import com.gg.mafia.domain.member.domain.User;
import java.security.Principal;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CommentApiTest {
    @InjectMocks
    CommentApi commentApi;
    @Mock
    CommentService commentService;
    @Mock
    Principal principal;
    MockMvc mockMvc;
    User user;
    CommentRequest commentRequest;

    @BeforeEach
    public void setUp() {
        user = createUser("TEST@naver.com", "123");
        commentRequest = createCommentRequest();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentApi).build();
    }

    @Test
    void showCommentsByProfile() throws Exception {
        mockMvc.perform(get("/comments")
                        .param("profileId", String.valueOf(commentRequest.getProfileId())))
                .andExpect(status().isOk());
    }

    @Test
    void saveComment() throws Exception {
        given(principal.getName()).willReturn(user.getEmail());

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"profileId\" : \"%d\", \"content\" : \"%s\"}", commentRequest.getProfileId(),
                                commentRequest.getContent()))
                        .principal(principal)
                )
                .andExpect(status().isOk());

    }

    @Test
    void updateComment() throws Exception {
        given(principal.getName()).willReturn(user.getEmail());

        mockMvc.perform(patch("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"commentId\" : \"%d\", \"updateContent\" : \"%s\"}",
                                commentRequest.getCommentId(), commentRequest.getUpdateContent()))
                        .principal(principal)
                )
                .andExpect(status().isOk());
    }

    @Test
    void removeComment() throws Exception {
        given(principal.getName()).willReturn(user.getEmail());

        mockMvc.perform(delete("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"commentId\" : \"%d\"}", commentRequest.getCommentId()))
                        .principal(principal)
                )
                .andExpect(status().isOk());
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }

    private CommentRequest createCommentRequest() {
        Random rand = new Random();
        CommentRequest request = new CommentRequest();
        request.setCommentId((long) rand.nextInt());
        request.setProfileId((long) rand.nextInt());
        request.setContent("TEST_CONTENT");
        request.setUpdateContent("UPDATE_TEST_CONTENT");

        return request;
    }
}