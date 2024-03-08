package com.gg.mafia.domain.Profile.api;

import com.gg.mafia.domain.Profile.application.ProfileService;
import com.gg.mafia.domain.Profile.dto.ProfileResponse;
import com.gg.mafia.domain.member.domain.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class ProfileApi {
    @Autowired
    ProfileService profileService;

    @GetMapping(value = "/{id}")
    public ProfileResponse user(@PathVariable("id") Long id) {
        return profileService.getByUserId(id);
    }


    //모든 유저rating으로 정렬
    @GetMapping(value = "/all")
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
}
