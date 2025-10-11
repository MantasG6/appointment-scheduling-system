package com.mantas.appointments.utils;

import com.mantas.appointments.dto.OfferedServiceRequest;
import com.mantas.appointments.dto.OfferedServiceResponse;
import com.mantas.appointments.entity.Category;
import com.mantas.appointments.entity.OfferedService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Factory class for creating test instances of OfferedService, OfferedServiceRequest, and OfferedServiceResponse.
 */
public final class OfferedServiceTestFactory {

    public static final Long DEFAULT_ID = null; // ID is auto-generated, so we can use null for default
    public static final String DEFAULT_NAME = "test";
    public static final String DEFAULT_DESCRIPTION = "test description";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(100);
    public static final Category DEFAULT_CATEGORY = Category.OTHER;
    public static final String DEFAULT_OWNER_ID = "e1nj18771_!1uh7";
    public static final LocalDateTime DEFAULT_CREATED = LocalDateTime.now();
    public static final LocalDateTime DEFAULT_UPDATED = LocalDateTime.now();

    // fields used for update tests
    public static final String UPDATED_NAME = "testUpdate";
    public static final String UPDATED_DESCRIPTION = "updated description";
    public static final BigDecimal UPDATED_PRICE = BigDecimal.valueOf(200);
    public static final Category UPDATED_CATEGORY = Category.DIET;
    public static final LocalDateTime UPDATED_UPDATE_DATE = LocalDateTime.now().plusDays(1);

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private OfferedServiceTestFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Builds a default {@link OfferedServiceRequest} instance for testing.
     *
     * @return a default {@link OfferedServiceRequest}
     */
    public static OfferedServiceRequest buildDefaultOfferedServiceRequest() {
        return OfferedServiceRequest.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .build();
    }

    /**
     * Builds a default {@link OfferedServiceResponse} instance for testing.
     *
     * @return a default {@link OfferedServiceResponse}
     */
    public static OfferedServiceResponse buildDefaultOfferedServiceResponse() {
        return OfferedServiceResponse.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .ownerId(DEFAULT_OWNER_ID)
                .created(DEFAULT_CREATED)
                .updated(DEFAULT_UPDATED)
                .build();
    }

    /**
     * Builds an {@link OfferedServiceRequest} instance with no name for testing validation.
     *
     * @return an {@link OfferedServiceRequest} with no name
     */
    public static OfferedServiceRequest buildNoNameOfferedServiceRequest() {
        return OfferedServiceRequest.builder()
                .name("")
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .build();
    }

    /**
     * Builds an {@link OfferedServiceRequest} instance with no description for testing validation.
     *
     * @return an {@link OfferedServiceRequest} with no description
     */
    public static OfferedServiceRequest buildNoDescOfferedServiceRequest() {
        return OfferedServiceRequest.builder()
                .name(DEFAULT_NAME)
                .description("")
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .build();
    }

    /**
     * Builds an {@link OfferedServiceResponse} instance with no description for testing validation.
     *
     * @return an {@link OfferedServiceResponse} with no description
     */
    public static OfferedServiceResponse buildNoDescOfferedServiceResponse() {
        return OfferedServiceResponse.builder()
                .name(DEFAULT_NAME)
                .description("")
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .ownerId(DEFAULT_OWNER_ID)
                .created(DEFAULT_CREATED)
                .updated(DEFAULT_UPDATED)
                .build();
    }

    /**
     * Builds an {@link OfferedServiceRequest} instance with a null price for testing validation.
     *
     * @return an {@link OfferedServiceRequest} with a null price
     */
    public static OfferedServiceRequest buildPriceNullOfferedServiceRequest() {
        return OfferedServiceRequest.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(null)
                .category(DEFAULT_CATEGORY)
                .build();
    }

    /**
     * Builds an {@link OfferedServiceRequest} instance with a negative price for testing validation.
     *
     * @return an {@link OfferedServiceRequest} with a negative price
     */
    public static OfferedServiceRequest buildNegativePriceOfferedServiceRequest() {
        return OfferedServiceRequest.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(BigDecimal.valueOf(-1))
                .category(DEFAULT_CATEGORY)
                .build();
    }

    /**
     * Builds an {@link OfferedServiceRequest} instance with a null category for testing validation.
     *
     * @return an {@link OfferedServiceRequest} with a null category
     */
    public static OfferedServiceRequest buildCategoryNullOfferedServiceRequest() {
        return OfferedServiceRequest.builder()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(null)
                .build();
    }

    /**
     * Builds an {@link OfferedServiceRequest} instance for partial update testing.<br>
     * Only the name field is updated; other fields are set to null.
     *
     * @return an {@link OfferedServiceRequest} for partial update
     */
    public static OfferedServiceRequest buildPartialUpdateOfferedServiceRequest() {
        return OfferedServiceRequest.builder()
                .name(UPDATED_NAME)
                .description(null)
                .price(null)
                .category(null)
                .build();
    }

    /**
     * Builds an {@link OfferedServiceRequest} instance for full update testing.
     *
     * @return an {@link OfferedServiceRequest} for full update
     */
    public static OfferedServiceRequest buildFullUpdateOfferedServiceRequest() {
        return OfferedServiceRequest.builder()
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .price(UPDATED_PRICE)
                .category(UPDATED_CATEGORY)
                .build();
    }

    /**
     * Builds an {@link OfferedServiceResponse} instance for full update testing.
     *
     * @return an {@link OfferedServiceResponse} for full update
     */
    public static OfferedServiceResponse buildFullUpdateOfferedServiceResponse() {
        return OfferedServiceResponse.builder()
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .price(UPDATED_PRICE)
                .category(UPDATED_CATEGORY)
                .ownerId(DEFAULT_OWNER_ID)
                .created(DEFAULT_CREATED)
                .updated(UPDATED_UPDATE_DATE)
                .build();
    }

    /**
     * Builds a default {@link OfferedService} entity instance for testing.
     *
     * @return a default {@link OfferedService} entity
     */
    public static OfferedService buildDefaultOfferedService() {
        return OfferedService.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .ownerId(DEFAULT_OWNER_ID)
                .created(DEFAULT_CREATED)
                .updated(DEFAULT_UPDATED)
                .build();
    }
}
