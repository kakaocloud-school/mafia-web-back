package com.gg.mafia.domain.profile.application;

import static java.lang.Math.ceil;

import com.gg.mafia.domain.member.dao.UserDao;
import com.gg.mafia.domain.member.domain.User;
import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.profile.dto.ProfileCreateRequest;
import com.gg.mafia.domain.profile.dto.ProfileMapper;
import com.gg.mafia.domain.profile.dto.ProfileResponse;
import com.gg.mafia.domain.profile.dto.ProfileSearchRequest;
import com.gg.mafia.domain.profile.dto.ProfileUpdateRequest;
import com.gg.mafia.domain.profile.dto.RankResponse;
import com.gg.mafia.domain.profile.dto.RatingRequest;
import com.gg.mafia.global.common.request.SearchQuery;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileDao profileDao;
    private final UserDao userDao;
    private final ProfileMapper profileMapper;

    public Page<RankResponse> getAllRanks(Pageable pageable) {
        Page<RankResponse> result = profileDao.findAllRanks(pageable)
                .map(profileMapper::toRankResponse);
        List<RankResponse> content = result.getContent();
        IntStream.range(0, content.size())
                .forEach(idx -> content.get(idx).setRank(pageable.getOffset() + idx + 1));
        return result;
    }

    public Page<ProfileResponse> search(ProfileSearchRequest request, SearchQuery searchQuery, Pageable pageable) {
        return profileDao.search(request, searchQuery, pageable)
                .map(profileMapper::toResponse);
    }

    public ProfileResponse getByUserId(Long id) {
        Profile profile = findByUserIdOrCreate(id);
        return profileMapper.toResponse(profile);
    }

    public Long save(ProfileCreateRequest request) {
        Profile profile = profileMapper.toEntity(request);
        return profile.getId();
    }

    @Transactional
    public void updateProfile(Long userId, ProfileUpdateRequest request) {
        Profile profile = findByUserIdOrCreate(userId);
        profileMapper.updateProfileFromDto(request, profile);
    }

    public void patchRating(RatingRequest ratingRequest) {
        List<Long> winnerTeamId = ratingRequest.getWinnerTeamId();
        List<Long> loserTeamId = ratingRequest.getLoserTeamId();
        ratingCal(winnerTeamId, loserTeamId);
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


    private void ratingCal(List<Long> winnerTeamId, List<Long> loserTeamId) {
        List<Profile> winnerTeamEntities = new ArrayList<Profile>();
        List<Profile> loserTeamEntities = new ArrayList<Profile>();
        int winnerTeamRating = 0;
        int loserTeamRating = 0;
        for (Long id : winnerTeamId) {
            Profile pro = findByUserIdOrCreate(id);
            winnerTeamEntities.add(pro);
            winnerTeamRating += pro.getRating();
        }
        for (Long id : loserTeamId) {
            Profile pro = findByUserIdOrCreate(id);
            loserTeamEntities.add(pro);
            loserTeamRating += pro.getRating();
        }
        winnerTeamRating /= winnerTeamId.size();
        loserTeamRating /= loserTeamId.size();

        //엘로레이팅의 기대 승률
        double winnerTeamExpectedWinningRate =
                1 / ((Math.pow(10, loserTeamRating - winnerTeamRating / 400) + 1));
        double loserExpectedWinningRate = 1 / ((Math.pow(10, (double) (winnerTeamRating - loserTeamRating) / 400)) + 1);
        double myChangeRating = 0;
        double opChangeRating = 0;
        //엘로레이팅의 변화하는 레이팅
        double winnerTeamChangingRating = ceil(winnerTeamRating + 20 * (1 - winnerTeamExpectedWinningRate));
        double loserTeamChangingRating = ceil(loserTeamRating + 20 * (0 - loserExpectedWinningRate));
        for (Profile winner : winnerTeamEntities) {
            winner.setRating((int) winnerTeamChangingRating);
            profileDao.save(winner);
        }
        for (Profile loser : loserTeamEntities) {
            loser.setRating((int) loserTeamChangingRating);
            profileDao.save(loser);
        }
    }

    private Profile findByUserIdOrCreate(Long id) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
        Optional<Profile> optionalProfile = profileDao.findByUserId(id);
        return optionalProfile.orElseGet(() -> {
            Profile profile = Profile.builder()
                    .user(optionalUser.get())
                    .userName(optionalUser.get().getEmail())
                    .build();
            profileDao.save(profile);
            return profile;
        });
    }
}