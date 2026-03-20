package com.mancholita.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mancholita.backend.api.dto.CategoryDto;
import com.mancholita.backend.api.dto.GenderDto;
import com.mancholita.backend.application.CategoryService;
import com.mancholita.backend.application.GenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PublicCatalogControllersTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private GenderService genderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new PublicCategoryController(categoryService),
                        new PublicGenderController(genderService)
                )
                .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }

    @Test
    void publicCategoriesReturnsFlatList() throws Exception {
        when(categoryService.getPublicCategories()).thenReturn(List.of(
                new CategoryDto(1L, "Blusas", true),
                new CategoryDto(2L, "Jeans", true)
        ));

        mockMvc.perform(get("/api/public/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Blusas"))
                .andExpect(jsonPath("$[1].name").value("Jeans"));
    }

    @Test
    void publicGendersReturnsFlatList() throws Exception {
        when(genderService.getPublicGenders()).thenReturn(List.of(
                new GenderDto(1L, "hombre", true),
                new GenderDto(2L, "mujer", true)
        ));

        mockMvc.perform(get("/api/public/genders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("hombre"))
                .andExpect(jsonPath("$[1].name").value("mujer"));
    }
}
