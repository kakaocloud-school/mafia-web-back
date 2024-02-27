package com.gg.mafia.domain.board.dto;

import com.gg.mafia.domain.board.domain.Sample;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-27T13:34:11+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class SampleMapperImpl implements SampleMapper {

    @Override
    public Sample toEntity(SampleCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Sample.SampleBuilder sample = Sample.builder();

        sample.title( request.getTitle() );
        sample.content( request.getContent() );

        return sample.build();
    }

    @Override
    public SampleResponse toResponse(Sample sample) {
        if ( sample == null ) {
            return null;
        }

        SampleResponse.SampleResponseBuilder sampleResponse = SampleResponse.builder();

        sampleResponse.id( sample.getId() );
        sampleResponse.createdAt( sample.getCreatedAt() );
        sampleResponse.updatedAt( sample.getUpdatedAt() );
        sampleResponse.title( sample.getTitle() );
        sampleResponse.content( sample.getContent() );

        return sampleResponse.build();
    }

    @Override
    public void updateSampleFromDto(SampleUpdateRequest request, Sample sample) {
        if ( request == null ) {
            return;
        }

        if ( request.getTitle() != null ) {
            sample.setTitle( request.getTitle() );
        }
        if ( request.getContent() != null ) {
            sample.setContent( request.getContent() );
        }
    }
}
