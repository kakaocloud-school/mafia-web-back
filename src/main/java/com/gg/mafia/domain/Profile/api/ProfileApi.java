package com.gg.mafia.domain.Profile.api;

import com.gg.mafia.domain.Profile.application.ProfileService;
import com.gg.mafia.domain.Profile.dto.ProfileResponse;
import com.gg.mafia.domain.Profile.dto.RatingRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileApi {
    @Autowired
    ProfileService profileService;

    @GetMapping(value = "/users/{id}")
    public ProfileResponse userById(@PathVariable("id") Long id) {
        return profileService.getByUserId(id);
    }

    @GetMapping(value = "/users/findByEmail")
    public ProfileResponse userByName(@RequestBody Map<String, String> map) {
        String name = map.get("name");
        return profileService.getByUserName(name);
    }

    //모든 유저rating으로 정렬
    @GetMapping(value = "/users")
    public List<ProfileResponse> allUsers() {
        return profileService.getAllUserWithRank();
    }

    //마피아 승률로 모든유저 정렬
    @GetMapping(value = "/allMafia")
    public List<ProfileResponse> allUsersWithMO() {
        return profileService.getAllUserWithMafiaOdd();
    }

    //의사 승률로 모든유저 정렬
    @GetMapping(value = "/allDoctor")
    public List<ProfileResponse> allUsersWithDO() {
        return profileService.getAllUserWithDoctorOdd();
    }

    //경찰 승률로 모든유저 정렬
    @GetMapping(value = "/allPolice")
    public List<ProfileResponse> allUsersWithPO() {
        return profileService.getAllUserWithPoliceOdd();
    }

    //시민 승률로 모든 유저 정렬
    @GetMapping(value = "/allCitizen")
    public List<ProfileResponse> allUsersWithCO() {
        return profileService.getAllUserWithCitizenOdd();
    }

    @PatchMapping(value = "/winner")
    public ResponseEntity<Object> patchRatingWithWin(@RequestBody RatingRequest ratingRequest) {
        String myName = ratingRequest.getMyName();
        String opponentName = ratingRequest.getOpponentName();
        profileService.patchWinRating(myName, opponentName);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/loser")
    public ResponseEntity<Object> patchRatingWithLose(@RequestBody RatingRequest ratingRequest) {
        String myName = ratingRequest.getMyName();
        String opponentName = ratingRequest.getOpponentName();
        profileService.patchLoseRating(myName, opponentName);
        return ResponseEntity.ok().build();
    }
}
