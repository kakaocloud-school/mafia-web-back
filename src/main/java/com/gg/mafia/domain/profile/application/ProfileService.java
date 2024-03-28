package com.gg.mafia.domain.profile.application;

import static java.lang.Math.ceil;

import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.profile.dto.ProfileMapper;
import com.gg.mafia.domain.profile.dto.ProfileRequest;
import com.gg.mafia.domain.profile.dto.ProfileResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileDao profileDao;
    private final ProfileMapper profileMapper;


    private Profile findById(Long id) {
        Optional<Profile> OProfileEntity = profileDao.findById(id);
        if (OProfileEntity.isPresent()) {
            return OProfileEntity.get();
        } else {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
    }

    //userId로 프로필을 가져옴
    public ProfileResponse getByUserId(Long id) {

        Profile profile = findById(id);

        return profileMapper.toProfileResponse(profile);

    }

    public Page<ProfileResponse> getByUserName(String name, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Profile> profile = profileDao.findByUserName(name, pageable);

        return profileMapper.toProfileResponsePage(profile);
    }

    //모든 유저의 프로필을 랭킹별 오름차순정렬해서 가져옴
    public Page<ProfileResponse> getAllUserWithRank(int page) {
        Sort sort = Sort.by(Direction.DESC, "ranking");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Profile> profilePage = profileDao.findAll(pageable);
        return profileMapper.toProfileResponsePage(profilePage);
    }

    // 모든 유저의 프로필을 마피아 승률별 오름차순정렬해서 가져옴
    public Page<ProfileResponse> getAllUserWithMafiaOdd(Pageable pageable) {
        Page<Profile> profileList = profileDao.findAll(pageable);
        return profileMapper.toProfileResponsePage(profileList);
    }

    public Page<ProfileResponse> getAllUserWithDoctorOdd(Pageable pageable) {
        Page<Profile> profileList = profileDao.findAll(pageable);
        return profileMapper.toProfileResponsePage(profileList);
    }

    public Page<ProfileResponse> getAllUserWithPoliceOdd(Pageable pageable) {
        Page<Profile> profileList = profileDao.findAll(pageable);
        return profileMapper.toProfileResponsePage(profileList);
    }

    public Page<ProfileResponse> getAllUserWithCitizenOdd(Pageable pageable) {
        Page<Profile> profileList = profileDao.findAll(pageable);
        return profileMapper.toProfileResponsePage(profileList);
    }


    public void descriptionSave(ProfileRequest request) {
        profileDao.save(profileMapper.toEntity(request));
    }

    public void descriptionUpdate(ProfileRequest request) {
        Profile profile = findById(request.getId());
        profile.setDescription(request.getDescription());
        profile.setDescription(request.getUserName());
        profileDao.save(profile);
    }

    public void patchWinRating(Long myId, Long opId) {
        ratingCal(myId, opId, true);
    }

    public void patchLoseRating(Long myId, Long opId) {

        ratingCal(myId, opId, false);
    }


    //평균 승률을 업데이트함
    private void winningRateUpdate(Page<Profile> profileEntities) {
        for (Profile profile : profileEntities) {
            float c = profile.getCitizenWinningRate();
            float m = profile.getMafiaWinningRate();
            float d = profile.getDoctorWinningRate();
            float p = profile.getPoliceWinningRate();
            float winningRate = (c + m + d + p) / 4;
            profile.setWinningRate(winningRate);
            profileDao.save(profile);
        }

    }

    private void winningRateUpdateOne(Profile profile) {
        float c = profile.getCitizenWinningRate();
        float m = profile.getMafiaWinningRate();
        float d = profile.getDoctorWinningRate();
        float p = profile.getPoliceWinningRate();
        float winningRate = (c + m + d + p) / 4;
        profile.setWinningRate(winningRate);
            profileDao.save(profile);
    }


    private void ratingCal(Long myId, Long opId, boolean winOrLose) {
        Profile myEntity = findById(myId);
        Profile opEntity = findById(opId);
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