package com.mantas.appointments.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * Converter interface for converting JWT claims to a collection of GrantedAuthority.
 * This interface is used to extract roles from JWT claims and convert them into Spring Security authorities.
 */
public interface AuthoritiesConverter extends Converter<Map<String, Object>, Collection<GrantedAuthority>> {
}