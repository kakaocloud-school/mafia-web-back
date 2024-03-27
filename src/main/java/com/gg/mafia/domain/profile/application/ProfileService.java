package com.gg.mafia.domain.profile.application;

import static java.lang.Math.ceil;

import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.ProfileEntity;
import com.gg.mafia.domain.profile.dto.ProfileMapper;
import com.gg.mafia.domain.profile.dto.ProfileRequest;
import com.gg.mafia.domain.profile.dto.ProfileResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
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


    private ProfileEntity findById(Long id) {
        Optional<ProfileEntity> OProfileEntity = profileDao.findById(id);
        if (OProfileEntity.isPresent()) {
            return OProfileEntity.get();
        } else {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
    }

    //userId로 프로필을 가져옴
    public ProfileResponse getByUserId(Long id) {

        ProfileEntity profile = findById(id);

        //OddUpdate함수를 쓰기위해 리스트에넣음
        this.oddUpdateOne(profile);

        return profileMapper.toProfileResponse(profile);

    }

    public Page<ProfileResponse> getByUserName(String name, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ProfileEntity> profile = profileDao.findByUserName(name, pageable);
        this.oddUpdate(profile);

        return profileMapper.toProfileResponsePage(profile);
    }

    //모든 유저의 프로필을 랭킹별 오름차순정렬해서 가져옴
    public Page<ProfileResponse> getAllUserWithRank(int page) {
        Sort sort = Sort.by(Direction.DESC, "ranking");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<ProfileEntity> profilePage = profileDao.findAll(pageable);
        this.oddUpdate(profilePage);
        this.rankingUpdate(profilePage);
        return profileMapper.toProfileResponsePage(profilePage);
    }

    // 모든 유저의 프로필을 마피아 승률별 오름차순정렬해서 가져옴
    public Page<ProfileResponse> getAllUserWithMafiaOdd(Pageable pageable) {
        Page<ProfileEntity> profileList = profileDao.findAll(pageable);
        this.oddUpdate(profileList);
        return profileMapper.toProfileResponsePage(profileList);
    }

    public Page<ProfileResponse> getAllUserWithDoctorOdd(Pageable pageable) {
        Page<ProfileEntity> profileList = profileDao.findAll(pageable);
        this.oddUpdate(profileList);
        return profileMapper.toProfileResponsePage(profileList);
    }

    public Page<ProfileResponse> getAllUserWithPoliceOdd(Pageable pageable) {
        Page<ProfileEntity> profileList = profileDao.findAll(pageable);
        this.oddUpdate(profileList);
        return profileMapper.toProfileResponsePage(profileList);
    }

    public Page<ProfileResponse> getAllUserWithCitizenOdd(Pageable pageable) {
        Page<ProfileEntity> profileList = profileDao.findAll(pageable);
        this.oddUpdate(profileList);
        return profileMapper.toProfileResponsePage(profileList);
    }


    public void descriptionSave(ProfileRequest request) {
        profileDao.save(profileMapper.toEntity(request));
    }

    public void descriptionUpdate(ProfileRequest request) {
        ProfileEntity profile = findById(request.getId());
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
    private void oddUpdate(Page<ProfileEntity> profileEntities) {
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

    private void oddUpdateOne(ProfileEntity profile) {
        float c = profile.getCitizenOdd();
        float m = profile.getMafiaOdd();
        float d = profile.getDoctorOdd();
        float p = profile.getPoliceOdd();
        float oddsAverage = (c + m + d + p) / 4;
        profile.setAverageOdd(oddsAverage);
            profileDao.save(profile);
    }


    //레이팅에 따라 랭킹업데이트
    private void rankingUpdate(Page<ProfileEntity> profileEntityPage) {
        ;
        List<ProfileEntity> profileEntities = profileEntityPage.getContent();

        profileEntities.sort(Comparator.comparingInt(ProfileEntity::getRating).reversed());
        int ranking = 1;
        for (ProfileEntity profile : profileEntities) {
            profile.setRanking(ranking);
            ranking += 1;
            profileDao.save(profile);
        }

    }

    private void ratingCal(Long myId, Long opId, boolean winOrLose) {
        ProfileEntity myEntity = findById(myId);
        ProfileEntity opEntity = findById(opId);
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