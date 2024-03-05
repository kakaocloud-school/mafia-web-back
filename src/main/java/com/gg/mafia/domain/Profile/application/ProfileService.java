package com.gg.mafia.domain.Profile.application;

import com.gg.mafia.domain.Profile.dao.ProfileDao;
import com.gg.mafia.domain.Profile.domain.ProfileEntity;
import com.gg.mafia.domain.Profile.dto.ProfileMapper;
import com.gg.mafia.domain.Profile.dto.ProfileRequest;
import com.gg.mafia.domain.Profile.dto.ProfileResponse;
import com.gg.mafia.domain.member.domain.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileDao profileDao;
    private final ProfileMapper profileMapper;

    public ProfileResponse getByUserId(Long id) {

        ProfileEntity profile = findById(id);
        return profileMapper.toProfileResponse(profile);

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
}
