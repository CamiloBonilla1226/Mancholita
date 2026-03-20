package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.GenderDto;

import java.util.List;

public interface GenderService {

    List<GenderDto> getPublicGenders();
}
