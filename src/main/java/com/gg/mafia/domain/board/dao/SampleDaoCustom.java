package com.gg.mafia.domain.board.dao;

import com.gg.mafia.domain.board.domain.Sample;
import com.gg.mafia.domain.board.dto.SampleSearchRequest;
import com.gg.mafia.global.common.request.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleDaoCustom {
    Page<Sample> search(SampleSearchRequest request, SearchQuery searchQuery, Pageable pageable);
}
