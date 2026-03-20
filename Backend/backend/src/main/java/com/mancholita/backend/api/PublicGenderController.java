package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.GenderDto;
import com.mancholita.backend.application.GenderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/genders")
public class PublicGenderController {

    private final GenderService genderService;

    public PublicGenderController(GenderService genderService) {
        this.genderService = genderService;
    }

    @GetMapping
    public List<GenderDto> list() {
        return genderService.getPublicGenders();
    }
}
