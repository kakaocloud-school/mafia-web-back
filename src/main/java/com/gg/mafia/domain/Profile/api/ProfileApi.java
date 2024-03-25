package com.gg.mafia.domain.Profile.api;

import com.gg.mafia.domain.Profile.application.ProfileService;
import com.gg.mafia.domain.Profile.dto.ProfileRequest;
import com.gg.mafia.domain.Profile.dto.ProfileResponse;
import com.gg.mafia.domain.Profile.dto.RatingRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @PostMapping(value = "{id}/profile")
    public ResponseEntity<Object> profileCreate(@RequestBody ProfileRequest request) {
        profileService.descriptionSave(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "{id}/profile")
    public ResponseEntity<Object> profileUpdate(@RequestBody ProfileRequest request) {
        profileService.descriptionUpdate(request);
        return ResponseEntity.ok().build();
    }

    //모든 유저rating으로 정렬
    @GetMapping(value = "/")
    public Page<ProfileResponse> allUsers(@RequestBody ProfileRequest request, @PathVariable("page") int page) {
        String name = request.getUserName();
        if (name != null) {
            return profileService.getByUserName(name, page);
        }

        return profileService.getAllUserWithRank(page);
    }

    //마피아 승률로 모든유저 정렬
    @GetMapping(value = "/mafiaRank")
    public Page<ProfileResponse> allUsersWithMO(@PathVariable("page") int page) {
        return profileService.getAllUserWithMafiaOdd(page);
    }

    //의사 승률로 모든유저 정렬
    @GetMapping(value = "/doctorRank")
    public Page<ProfileResponse> allUsersWithDO(@PathVariable("page") int page) {
        return profileService.getAllUserWithDoctorOdd(page);
    }

    //경찰 승률로 모든유저 정렬
    @GetMapping(value = "/policeRank")
    public Page<ProfileResponse> allUsersWithPO(@PathVariable("page") int page) {
        return profileService.getAllUserWithPoliceOdd(page);
    }

    //시민 승률로 모든 유저 정렬
    @GetMapping(value = "/citizenRank")
    public Page<ProfileResponse> allUsersWithCO(@PathVariable("page") int page) {
        return profileService.getAllUserWithCitizenOdd(page);
    }

    @PatchMapping(value = "/update-win")
    public ResponseEntity<Object> patchRatingWithWin(@RequestBody RatingRequest ratingRequest) {
        Long myId = ratingRequest.getMyId();
        Long opponentId = ratingRequest.getOpponentId();
        profileService.patchWinRating(myId, opponentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/update-lose")
    public ResponseEntity<Object> patchRatingWithLose(@RequestBody RatingRequest ratingRequest) {
        Long myId = ratingRequest.getMyId();
        Long opponentId = ratingRequest.getOpponentId();
        profileService.patchLoseRating(myId, opponentId);
        return ResponseEntity.ok().build();
    }

}
