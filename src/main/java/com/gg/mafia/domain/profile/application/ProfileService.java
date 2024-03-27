package com.gg.mafia.domain.profile.application;

import com.gg.mafia.domain.profile.dao.ProfileDao;
import com.gg.mafia.domain.profile.dto.ProfileMapper;
import com.gg.mafia.domain.profile.dto.RankResponse;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
