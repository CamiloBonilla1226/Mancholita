package com.mancholita.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mancholita.backend.api.dto.ProductActiveRequest;
import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.application.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminProductControllerTest {

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdminProductController(productService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void createAcceptsGenderIdInRequest() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest();
        request.name = "Jean Regular";
        request.description = "Clasico";
        request.price = new BigDecimal("99900");
        request.imageUrl = "https://img.test/jean.jpg";
        request.active = true;
        request.categoryId = 10L;
        request.genderId = 20L;

        when(productService.create(org.mockito.ArgumentMatchers.any(ProductCreateRequest.class)))
                .thenReturn(new ProductAdminDto(
                        1L, "Jean Regular", "Clasico", "https://img.test/jean.jpg",
                        new BigDecimal("99900"), true, 10L, "Jeans", 20L, "Hombre"
                ));

        mockMvc.perform(post("/api/admin/products")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genderId").value(20))
                .andExpect(jsonPath("$.categoryName").value("Jeans"));
    }

    @Test
    void listPassesGenderFilterToService() throws Exception {
        when(productService.listAdmin(eq(3L), eq(2L), eq(true), eq("jean"), eq(PageRequest.of(0, 20))))
                .thenReturn(new PageImpl<>(List.of(
                        new ProductAdminDto(5L, "Jean Skinny", "Azul", "img", new BigDecimal("120000"),
                                true, 3L, "Jeans", 2L, "Mujer")
                ), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/admin/products")
                        .param("page", "0")
                        .param("size", "20")
                        .param("categoryId", "3")
                        .param("genderId", "2")
                        .param("active", "true")
                        .param("q", "jean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].genderName").value("Mujer"));

        verify(productService).listAdmin(3L, 2L, true, "jean", PageRequest.of(0, 20));
    }
}
