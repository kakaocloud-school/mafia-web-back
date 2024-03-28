package com.gg.mafia.domain.profile.application;

import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.dto.ProfileMapper;
import com.gg.mafia.domain.profile.dto.RankResponse;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static java.lang.Math.ceil;

import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.domain.Profile;
import com.gg.mafia.domain.profile.dto.ProfileMapper;
import com.gg.mafia.domain.profile.dto.ProfileRequest;
import com.gg.mafia.domain.profile.dto.ProfileResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
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

    public Page<RankResponse> getAllRanks(Pageable pageable) {
        Page<RankResponse> result = profileDao.findAllRanks(pageable)
                .map(profileMapper::toRankResponse);
        List<RankResponse> content = result.getContent();
        IntStream.range(0, content.size())
                .forEach(idx -> content.get(idx).setRank(pageable.getOffset() + idx + 1));
        return result;
    }
}


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
        Sort sort = Sort.by(Direction.DESC, "rating");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Profile> profilePage = profileDao.findAll(pageable);
        return profileMapper.toProfileResponsePage(profilePage);
    }

    // 모든 유저의 프로필을 마피아 승률별 오름차순정렬해서 가져옴

    public void descriptionUpdate(ProfileRequest request) {
        Profile profile = findById(request.getUserId());
        profile.setDescription(request.getDescription());
        profileDao.save(profile);
    }

    public void patchRating(List<Long> winnerTeamId, List<Long> loserTeamId) {
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
        for(Long id:winnerTeamId){
            Profile pro = findById(id);
            winnerTeamEntities.add(pro);
            winnerTeamRating+=pro.getRating();
        }
        for(Long id:loserTeamId)
        {
            Profile pro = findById(id);
            loserTeamEntities.add(pro);
            loserTeamRating+=pro.getRating();
        }
        winnerTeamRating/=winnerTeamId.size();
        loserTeamRating/=loserTeamId.size();

        //엘로레이팅의 기대 승률
        double winnerTeamExpectedWinningRate = 1 / ((Math.pow(10, (double) (loserTeamRating - winnerTeamRating / 400)) + 1));
        double loserExpectedWinningRate = 1 / ((Math.pow(10, (double) (winnerTeamRating - loserTeamRating) / 400)) + 1);
        double myChangeRating = 0;
        double opChangeRating = 0;
            //엘로레이팅의 변화하는 레이팅
        double winnerTeamChangingRating = ceil(winnerTeamRating + 20 * (1 - winnerTeamExpectedWinningRate));
        double loserTeamChangingRating = ceil(loserTeamRating + 20 * (0 - loserExpectedWinningRate));
        for(Profile winner:winnerTeamEntities)
        {
            winner.setRating((int) winnerTeamChangingRating);
            profileDao.save(winner);
        }
        for(Profile loser:loserTeamEntities)
        {
            loser.setRating((int) loserTeamChangingRating);
            profileDao.save(loser);
        }
    }


}