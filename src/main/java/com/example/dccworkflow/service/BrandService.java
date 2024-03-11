package com.example.dccworkflow.service;

import com.example.dccworkflow.dto.BrandDto;
import com.example.dccworkflow.entity.Brand;
import com.example.dccworkflow.entity.QBrand;
import com.example.dccworkflow.repository.BrandRepository;
import com.example.dccworkflow.utils.LikeWrap;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BrandService {
    private BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Transactional
    public Brand addBrand(String name, String ref) {
        Brand brand = new Brand();
        brand.setName(name);
        brand.setRef(ref);
        return brandRepository.save(brand);
    }

    @Transactional
    public void removeBrand(Long id) {
        brandRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<BrandDto> getBrandDto(String search, Pageable pageable) {
        QBrand qBrand = QBrand.brand;

        BooleanBuilder whereCase = new BooleanBuilder();
        if (search != null && !search.isBlank()) {
            whereCase.or(qBrand.name.like(LikeWrap.like(search)))
                    .or(qBrand.ref.like(LikeWrap.like(search)));
        }

        Page<Brand> brandPage = brandRepository.findAll(whereCase, pageable);

        List<BrandDto> content = brandPage.getContent()
                .stream()
                .map(brand -> {
                    BrandDto dto = new BrandDto();
                    BeanUtils.copyProperties(brand, dto);
                    return dto;
                })
                .toList();

        return new PageImpl<>(content, pageable, brandPage.getTotalElements());
    }
}
