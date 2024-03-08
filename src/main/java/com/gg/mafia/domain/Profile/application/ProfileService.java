package com.gg.mafia.domain.Profile.application;

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

    //모든 유저의 프로필을 랭킹별 오름차순정렬해서 가져옴
    public List<ProfileResponse> getAllUserWithRank() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        profileList.sort(Comparator.comparingInt(ProfileEntity::getRanking));
        return profileMapper.toProfileResponseList(profileList);
    }

    // 모든 유저의 프로필을 마피아 승률별 오름차순정렬해서 가져옴
    public List<ProfileResponse> getAllUserWithMafiaOdd() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        profileList.sort(Comparator.comparingDouble(ProfileEntity::getMafiaOdd));
        return profileMapper.toProfileResponseList(profileList);
    }

    public List<ProfileResponse> getAllUserWithDoctorOdd() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        profileList.sort(Comparator.comparingDouble(ProfileEntity::getDoctorOdd));
        return profileMapper.toProfileResponseList(profileList);
    }

    public List<ProfileResponse> getAllUserWithPoliceOdd() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        profileList.sort(Comparator.comparingDouble(ProfileEntity::getPoliceOdd));
        return profileMapper.toProfileResponseList(profileList);
    }

    public List<ProfileResponse> getAllUserWithCitizenOdd() {
        List<ProfileEntity> profileList = profileDao.findAll();
        this.oddUpdate(profileList);
        profileList.sort(Comparator.comparingDouble(ProfileEntity::getCitizenOdd));
        return profileMapper.toProfileResponseList(profileList);
    }


    public void descriptionSave(ProfileRequest request) {
        profileDao.save(profileMapper.toEntity(request));
    }

    public void descriptionUpdate(ProfileRequest request) {
        profileDao.save(profileMapper.toEntity(request));
    }

    private ProfileEntity findById(Long id) {
        Optional<ProfileEntity> OProfileEntity = profileDao.findById(id);
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

}
