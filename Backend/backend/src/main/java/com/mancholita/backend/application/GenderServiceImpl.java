package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.GenderDto;
import com.mancholita.backend.infrastructure.GenderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;

    public GenderServiceImpl(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    @Override
    public List<GenderDto> getPublicGenders() {
        return genderRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(gender -> new GenderDto(gender.getId(), gender.getName(), gender.isActive()))
                .toList();
    }
}
