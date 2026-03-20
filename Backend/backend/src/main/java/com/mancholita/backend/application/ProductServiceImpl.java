package com.mancholita.backend.application;

import com.mancholita.backend.api.dto.ProductAdminDto;
import com.mancholita.backend.api.dto.ProductCreateRequest;
import com.mancholita.backend.api.dto.ProductPublicDto;
import com.mancholita.backend.application.mapper.ProductMapper;
import com.mancholita.backend.domain.Category;
import com.mancholita.backend.domain.Gender;
import com.mancholita.backend.domain.Product;
import com.mancholita.backend.infrastructure.CategoryRepository;
import com.mancholita.backend.infrastructure.GenderRepository;
import com.mancholita.backend.infrastructure.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final GenderRepository genderRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              GenderRepository genderRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.genderRepository = genderRepository;
    }

    @Override
    @Transactional
    public ProductAdminDto create(ProductCreateRequest req) {
        boolean active = (req.active == null) ? true : req.active;
        Category category = findCategory(req.categoryId);
        Gender gender = findGender(req.genderId);

        Product product = new Product(
                req.name,
                req.description,
                req.price,
                active,
                req.imageUrl,
                category,
                gender
        );

        Product saved = productRepository.save(product);
        return ProductMapper.toAdminDto(saved);
    }

    @Override
    @Transactional
    public ProductAdminDto update(Long id, ProductCreateRequest req) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        Category category = findCategory(req.categoryId);
        Gender gender = findGender(req.genderId);

        product.update(
                req.name,
                req.description,
                req.price,
                req.imageUrl,
                category,
                gender
        );

        Product saved = productRepository.save(product);
        return ProductMapper.toAdminDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductAdminDto getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        return ProductMapper.toAdminDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductAdminDto> listAdmin(Long categoryId, Long genderId, Boolean active, String q, Pageable pageable) {
        String normalizedQ = normalizeQuery(q);
        return productRepository.findAdminProducts(categoryId, genderId, active, normalizedQ, pageable);
    }

    @Override
    @Transactional
    public ProductAdminDto setActive(Long id, boolean active) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.setActive(active);

        Product saved = productRepository.save(product);
        return ProductMapper.toAdminDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductPublicDto> listPublic(Long categoryId, Long genderId, String q, Pageable pageable) {
        String normalizedQ = normalizeQuery(q);
        return productRepository.findPublicProducts(categoryId, genderId, normalizedQ, pageable);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.setActive(false);
        productRepository.save(product);
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
    }

    private Gender findGender(Long genderId) {
        return genderRepository.findById(genderId)
                .orElseThrow(() -> new IllegalArgumentException("Gender not found: " + genderId));
    }

    private String normalizeQuery(String q) {
        return (q == null || q.trim().isEmpty()) ? null : q.trim();
    }
}
