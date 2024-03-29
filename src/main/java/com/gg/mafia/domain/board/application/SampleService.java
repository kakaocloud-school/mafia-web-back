package com.gg.mafia.domain.board.application;

import com.gg.mafia.domain.board.dao.SampleDao;
import com.gg.mafia.domain.board.domain.Sample;
import com.gg.mafia.domain.board.dto.SampleCreateRequest;
import com.gg.mafia.domain.board.dto.SampleMapper;
import com.gg.mafia.domain.board.dto.SampleResponse;
import com.gg.mafia.domain.board.dto.SampleSearchRequest;
import com.gg.mafia.domain.board.dto.SampleUpdateRequest;
import com.gg.mafia.global.common.request.SearchFilter;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SampleService {
    private final SampleDao sampleDao;
    private final SampleMapper sampleMapper;

    public Page<SampleResponse> getAll(Pageable pageable) {
        return sampleDao.findAll(pageable).map(sampleMapper::toResponse);
    }

    public Page<SampleResponse> search(SampleSearchRequest request, SearchFilter filter, Pageable pageable) {
        return sampleDao.search(request, filter, pageable).map(sampleMapper::toResponse);
    }

    public SampleResponse getById(Long id) {
        Sample sample = findById(id);
        return sampleMapper.toResponse(sample);
    }

    public Long save(SampleCreateRequest request) {
        Sample sample = sampleDao.save(sampleMapper.toEntity(request));
        return sample.getId();
    }

    @Transactional
    public void update(Long id, SampleUpdateRequest request) {
        Sample sample = findById(id);
        sampleMapper.updateSampleFromDto(request, sample);
        sampleDao.save(sample);
    }

    @Transactional
    public void delete(Long id) {
        Sample sample = findById(id);
        sampleDao.delete(sample);
    }

    private Sample findById(Long id) {
        Optional<Sample> sample = sampleDao.findById(id);
        if (sample.isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
        return sample.get();
    }
}
