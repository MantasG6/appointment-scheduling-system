package com.mantas.appointments.service.utils;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static com.mantas.appointments.service.utils.ServiceUtils.extractUserIdFromAuthentication;
import static com.mantas.appointments.service.utils.ServiceUtils.getEntityFromRepoById;
import static com.mantas.appointments.utils.TestSecurityUtils.DEFAULT_OWNER_ID;
import static com.mantas.appointments.utils.TestUtils.entityNotFoundMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceUtilsTest {

    private static final Long INVALID_ID = -1L;
    private static final Long VALID_ID = 1L;

    private record DummyEntity(Long id, String name) {
    }

    @Mock
    private JpaRepository<DummyEntity, Long> dummyRepository;

    @Test
    void givenInvalidId_whenGetEntityFromRepoById_thenThrowsEntityNotFoundException() {
        when(dummyRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> getEntityFromRepoById(INVALID_ID, dummyRepository)
        );
        assertEquals(exception.getMessage(), entityNotFoundMessage(INVALID_ID));
    }

    @Test
    void givenValidId_whenGetEntityFromRepoById_thenReturnsEntity() {
        DummyEntity expectedEntity = new DummyEntity(VALID_ID, "Test Name");
        when(dummyRepository.findById(VALID_ID)).thenReturn(Optional.of(expectedEntity));

        DummyEntity result = getEntityFromRepoById(VALID_ID, dummyRepository);
        assertEquals(expectedEntity, result);
    }

    @Test
    void givenValidJwt_whenExtractUserIdFromAuthentication_thenReturnsUserId() {
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getSubject()).thenReturn(DEFAULT_OWNER_ID);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertEquals(DEFAULT_OWNER_ID, extractUserIdFromAuthentication(authentication));
    }

}
