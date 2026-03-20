package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.domain.Category;
import com.mancholita.backend.domain.Gender;
import com.mancholita.backend.domain.Product;
import com.mancholita.backend.infrastructure.CategoryRepository;
import com.mancholita.backend.infrastructure.GenderRepository;
import com.mancholita.backend.infrastructure.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GenderRepository genderRepository;

    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductServiceImpl(productRepository, categoryRepository, genderRepository);
    }

    @Test
    void createPersistsCategoryAndGenderSeparately() {
        Category category = new Category("Jeans", true);
        Gender gender = new Gender("Mujer", true);
        ReflectionTestUtils.setField(category, "id", 10L);
        ReflectionTestUtils.setField(gender, "id", 20L);

        ProductCreateRequest request = new ProductCreateRequest();
        request.name = "Jean Mom";
        request.description = "Denim azul";
        request.price = new BigDecimal("129900");
        request.imageUrl = "https://img.test/jean.jpg";
        request.categoryId = 10L;
        request.genderId = 20L;
        request.active = true;

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(genderRepository.findById(20L)).thenReturn(Optional.of(gender));
        when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    ReflectionTestUtils.setField(product, "id", 99L);
                    return product;
                });

        ProductAdminDto result = service.create(request);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();

        assertThat(saved.getCategory().getId()).isEqualTo(10L);
        assertThat(saved.getGender().getId()).isEqualTo(20L);
        assertThat(result.id).isEqualTo(99L);
        assertThat(result.categoryName).isEqualTo("Jeans");
        assertThat(result.genderName).isEqualTo("Mujer");
    }

    @Test
    void createFailsWhenGenderDoesNotExist() {
        Category category = new Category("Camisas", true);
        ReflectionTestUtils.setField(category, "id", 5L);

        ProductCreateRequest request = new ProductCreateRequest();
        request.name = "Camisa Oxford";
        request.price = new BigDecimal("89900");
        request.imageUrl = "https://img.test/camisa.jpg";
        request.categoryId = 5L;
        request.genderId = 999L;

        when(categoryRepository.findById(5L)).thenReturn(Optional.of(category));
        when(genderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Gender not found: 999");
    }

    @Test
    void listAdminDelegatesWithGenderFilterAndNormalizedQuery() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAdminProducts(1L, 2L, true, null, pageable))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0));

        service.listAdmin(1L, 2L, true, "   ", pageable);

        verify(productRepository).findAdminProducts(eq(1L), eq(2L), eq(true), isNull(), eq(pageable));
    }
}
