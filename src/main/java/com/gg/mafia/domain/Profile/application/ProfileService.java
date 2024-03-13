package com.gg.mafia.domain.Profile.application;

import static java.lang.Math.ceil;

import com.gg.mafia.domain.Profile.dao.ProfileDao;
import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.Profile.dto.ProfileMapper;
import com.gg.mafia.domain.Profile.dto.ProfileRequest;
import com.gg.mafia.domain.Profile.dto.ProfileResponse;
import com.gg.mafia.domain.member.domain.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileDao profileDao;
    private final ProfileMapper profileMapper;

    //userId로 프로필을 가져옴
    public ProfileResponse getByUserId(Long id) {

        ProfileEntity profile = findById(id);

        //OddUpdate함수를 쓰기위해 리스트에넣음
        List<ProfileEntity> profileForUpdate = new ArrayList<>();
        profileForUpdate.add(profile);
        this.oddUpdate(profileForUpdate);

        return profileMapper.toProfileResponse(profile);

    }

    public ProfileResponse getByUserName(String name) {
        ProfileEntity profile = findByUserName(name);
        List<ProfileEntity> profileForUpdate = new ArrayList<>();
        profileForUpdate.add(profile);
        this.oddUpdate(profileForUpdate);

        return profileMapper.toProfileResponse(profile);
    }

    //모든 유저의 프로필을 랭킹별 오름차순정렬해서 가져옴
    public List<ProfileResponse> getAllUserWithRank() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        this.rankingUpdate(profileList);
        profileList.sort(Comparator.comparingInt(ProfileEntity::getRanking));
        return profileMapper.toProfileResponseList(profileList);
    }

    // 모든 유저의 프로필을 마피아 승률별 오름차순정렬해서 가져옴
    public List<ProfileResponse> getAllUserWithMafiaOdd() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        profileList.sort(Comparator.comparingDouble(ProfileEntity::getMafiaOdd).reversed());
        return profileMapper.toProfileResponseList(profileList);
    }

    public List<ProfileResponse> getAllUserWithDoctorOdd() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        profileList.sort(Comparator.comparingDouble(ProfileEntity::getDoctorOdd).reversed());
        return profileMapper.toProfileResponseList(profileList);
    }

    public List<ProfileResponse> getAllUserWithPoliceOdd() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        profileList.sort(Comparator.comparingDouble(ProfileEntity::getPoliceOdd).reversed());
        return profileMapper.toProfileResponseList(profileList);
    }

    public List<ProfileResponse> getAllUserWithCitizenOdd() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        profileList.sort(Comparator.comparingDouble(ProfileEntity::getCitizenOdd).reversed());
        return profileMapper.toProfileResponseList(profileList);
    }


    public void descriptionSave(ProfileRequest request) {
        profileDao.save(profileMapper.toEntity(request));
    }

    public void descriptionUpdate(ProfileRequest request) {
        profileDao.save(profileMapper.toEntity(request));
    }

    public void patchWinRating(String myName, String opName) {
        ratingCal(myName, opName, true);
    }

    public void patchLoseRating(String myName, String opName) {

        ratingCal(myName, opName, false);
    }

    private ProfileEntity findById(Long id) {
        Optional<ProfileEntity> OProfileEntity = profileDao.findById(id);
        if (OProfileEntity.isPresent()) {
            return OProfileEntity.get();
        } else {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
    }

    private ProfileEntity findByUserName(String userName) {
        Optional<ProfileEntity> OProfileEntity = profileDao.findByUserName(userName);
        if (OProfileEntity.isPresent()) {
            return OProfileEntity.get();
        } else {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
    }


    //평균 승률을 업데이트함
    private void oddUpdate(List<ProfileEntity> profileEntities) {
        for (ProfileEntity profile : profileEntities) {
            float c = profile.getCitizenOdd();
            float m = profile.getMafiaOdd();
            float d = profile.getDoctorOdd();
            float p = profile.getPoliceOdd();
            float oddsAverage = (c + m + d + p) / 4;
            profile.setAverageOdd(oddsAverage);
            profileDao.save(profile);
        }

    }

    //레이팅에 따라 랭킹업데이트
    private void rankingUpdate(List<ProfileEntity> profileEntities) {
        profileEntities.sort(Comparator.comparingInt(ProfileEntity::getRating).reversed());
        int ranking = 1;
        for (ProfileEntity profile : profileEntities) {
            profile.setRanking(ranking);
            ranking += 1;
            profileDao.save(profile);
        }

    }

    private void ratingCal(String myName, String opName, boolean winOrLose) {
        ProfileEntity myEntity = findByUserName(myName);
        ProfileEntity opEntity = findByUserName(opName);
        int mR = myEntity.getRating();
        int oR = opEntity.getRating();
        //엘로레이팅의 기대 승률
        double myExpectedOdd = 1 / ((Math.pow(10, (double) (oR - mR) / 400)) + 1);
        double opExpectedOdd = 1 / ((Math.pow(10, (double) (mR - oR) / 400)) + 1);
        int myNowRating = myEntity.getRating();
        int opNowRating = opEntity.getRating();
        double myChangeRating = 0;
        double opChangeRating = 0;
        if (winOrLose) {
            //엘로레이팅의 변화하는 레이팅
            myChangeRating = ceil(myNowRating + 20 * (1 - myExpectedOdd));
            opChangeRating = ceil(opNowRating + 20 * (0 - opExpectedOdd));

        } else {
            //엘로레이팅의 변화하는 레이팅
            myChangeRating = ceil(myNowRating + 20 * (0 - myExpectedOdd));
            opChangeRating = ceil(opNowRating + 20 * (1 - opExpectedOdd));
        }
        myEntity.setRating((int) myChangeRating);
        opEntity.setRating((int) opChangeRating);
        profileDao.save(myEntity);
        profileDao.save(opEntity);
    }


}
