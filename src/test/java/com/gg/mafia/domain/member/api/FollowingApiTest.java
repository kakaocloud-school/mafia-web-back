package com.gg.mafia.domain.member.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gg.mafia.domain.member.application.FollowingService;
import com.gg.mafia.domain.member.domain.Following;
import com.gg.mafia.domain.member.domain.User;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
class FollowingApiTest {
    @InjectMocks
    FollowingApi followingApi;
    @Mock
    FollowingService followingService;
    @Mock
    Principal principal;
    MockMvc mockMvc;
    User user;

    @BeforeEach
    public void setUp() {
        user = createUser("TEST@naver.com", "123");
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(followingApi).build();
    }

    @Test
    void showFollowees() throws Exception {
        mockMvc.perform(get("/member/follow/followees")
                        .param("email", user.getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    void showFollowers() throws Exception {
        mockMvc.perform(get("/member/follow/followers")
                        .param("email", user.getEmail()))
                .andExpect(status().isOk());
    }

    @Test
    void addFollow() throws Exception {
        given(principal.getName()).willReturn(user.getEmail());

        mockMvc.perform(post("/member/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"followeeEmail\" : \"%s\"}", user.getEmail()))
                        .principal(principal)
                )
                .andExpect(status().isOk());
    }

    @Test
    void removeFollow() throws Exception {
        mockMvc.perform(delete("/member/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"followeeEmail\" : \"%s\"}", user.getEmail()))
                        .principal(principal)
                )
                .andExpect(status().isOk());
    }

    private User createUser(String email, String pw) {
        return User.builder().email(email).password(pw).build();
    }

    private List<Following> createFollowingsFromFollower(User follower, int count) {
        Random rand = new Random();
        List<Following> followings = new ArrayList<>();
        for (int i = 1; i < count; i++) {
            User randomFollowee = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
            if (rand.nextInt(1, 10) > 5) {
                followings.add(createFollowing(follower, randomFollowee));
            } else {
                User randomFollower = createUser(UUID.randomUUID().toString().substring(0, 5), "123");
                followings.add(createFollowing(randomFollower, randomFollowee));
            }
        }
        return followings;
    }

    private Following createFollowing(User follower, User followee) {
        return Following.builder().follower(follower).followee(followee).build();
    }
}