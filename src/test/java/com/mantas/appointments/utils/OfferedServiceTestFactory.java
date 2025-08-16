package com.mantas.appointments.utils;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.entity.Category;
import com.mantas.appointments.entity.OfferedService;

import java.math.BigDecimal;

public final class OfferedServiceTestFactory {

    public static final Long DEFAULT_ID = null; // ID is auto-generated, so we can use null for default
    public static final String DEFAULT_NAME = "test";
    public static final String DEFAULT_DESCRIPTION = "test description";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(100);
    public static final Category DEFAULT_CATEGORY = Category.OTHER;

    // fields used for update tests
    public static final String UPDATED_NAME = "testUpdate";
    public static final String UPDATED_DESCRIPTION = "updated description";
    public static final BigDecimal UPDATED_PRICE = BigDecimal.valueOf(200);
    public static final Category UPDATED_CATEGORY = Category.DIET;

    private OfferedServiceTestFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static OfferedServiceDTO buildDefaultOfferedServiceDTO() {
        return OfferedServiceDTO.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .build();
    }

    public static OfferedServiceDTO buildNoNameOfferedServiceDTO() {
        return OfferedServiceDTO.builder()
                .name("")
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .build();
    }

    public static OfferedServiceDTO buildNoDescOfferedServiceDTO() {
        return OfferedServiceDTO.builder()
                .name(DEFAULT_NAME)
                .description("")
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .build();
    }

    public static OfferedServiceDTO buildPriceNullOfferedServiceDTO() {
        return OfferedServiceDTO.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(null)
                .category(DEFAULT_CATEGORY)
                .build();
    }

    public static OfferedServiceDTO buildNegativePriceOfferedServiceDTO() {
        return OfferedServiceDTO.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(BigDecimal.valueOf(-1))
                .category(DEFAULT_CATEGORY)
                .build();
    }

    public static OfferedServiceDTO buildCategoryNullOfferedServiceDTO() {
        return OfferedServiceDTO.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(null)
                .build();
    }

    public static OfferedServiceDTO buildPartialUpdateOfferedServiceDTO() {
        return OfferedServiceDTO.builder()
                .name(UPDATED_NAME)
                .description(null)
                .price(null)
                .category(null)
                .build();
    }

    public static OfferedServiceDTO buildFullUpdateOfferedServiceDTO() {
        return OfferedServiceDTO.builder()
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .price(UPDATED_PRICE)
                .category(UPDATED_CATEGORY)
                .build();
    }

    public static OfferedService buildDefaultOfferedService() {
        return OfferedService.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .build();
    }
}
