package com.mancholita.backend.api;

import com.mancholita.backend.api.dto.GenderCreateRequest;
import com.mancholita.backend.api.dto.GenderDto;
import com.mancholita.backend.domain.Gender;
import com.mancholita.backend.infrastructure.GenderRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/genders")
public class AdminGenderController {

    private final GenderRepository repo;

    public AdminGenderController(GenderRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<GenderDto> listAll() {
        return repo.findAllByOrderByNameAsc().stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping
    public GenderDto create(@Valid @RequestBody GenderCreateRequest req) {
        repo.findByNameIgnoreCase(req.name.trim())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Gender already exists: " + req.name.trim());
                });

        boolean active = (req.active == null) ? true : req.active;
        Gender gender = new Gender(req.name.trim(), active);
        return toDto(repo.save(gender));
    }

    @PatchMapping("/{id}/active")
    public GenderDto setActive(@PathVariable Long id, @RequestParam boolean value) {
        Gender gender = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gender not found: " + id));
        gender.setActive(value);
        return toDto(repo.save(gender));
    }

    @PutMapping("/{id}")
    public GenderDto rename(@PathVariable Long id, @Valid @RequestBody GenderCreateRequest req) {
        Gender gender = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gender not found: " + id));

        repo.findByNameIgnoreCase(req.name.trim())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Gender already exists: " + req.name.trim());
                });

        gender.setName(req.name.trim());
        if (req.active != null) {
            gender.setActive(req.active);
        }
        return toDto(repo.save(gender));
    }

    private GenderDto toDto(Gender gender) {
        return new GenderDto(gender.getId(), gender.getName(), gender.isActive());
    }
}
