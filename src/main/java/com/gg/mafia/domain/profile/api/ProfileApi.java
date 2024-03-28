package com.gg.mafia.domain.profile.api;

import com.gg.mafia.domain.profile.application.ProfileService;
import com.gg.mafia.domain.profile.dto.ProfileRequest;
import com.gg.mafia.domain.profile.dto.ProfileResponse;
import com.gg.mafia.domain.profile.dto.RatingRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class ProfileApi {
    @Autowired
    ProfileService profileService;

    @GetMapping(value = "/{id}/profile")
    public ProfileResponse userById(@PathVariable("id") Long id) {
        return profileService.getByUserId(id);
    }



    @PatchMapping(value = "{id}/profile")
    public ResponseEntity<Object> profileUpdate(@RequestBody ProfileRequest request) {
        profileService.descriptionUpdate(request);
        return ResponseEntity.ok().build();
    }

    //모든 유저rating으로 정렬
    @GetMapping(value = "/{page}")
    public Page<ProfileResponse> allUsers(@PathVariable("page") int page) {
        return profileService.getAllUserWithRank(page);
    }
    @GetMapping(value = "/find-by-user-name/{userName}/{page}")

    public Page<ProfileResponse> UsersSearch(@PathVariable("userName")String userName, @PathVariable("page") int page) {
        return profileService.getByUserName(userName, page);
    }

    //마피아 승률로 모든유저 정렬
    @PatchMapping(value = "/update-rating")
    public ResponseEntity<Object> patchRating(@RequestBody RatingRequest ratingRequest) {
        List<Long> winnerTeamId = ratingRequest.getWinnerTeamId();
        List<Long> loserTeamId = ratingRequest.getLoserTeamId();
        profileService.patchRating(winnerTeamId, loserTeamId);
        return ResponseEntity.ok().build();
    }

}
