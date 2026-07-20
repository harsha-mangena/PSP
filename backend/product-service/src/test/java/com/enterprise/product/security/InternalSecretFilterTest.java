package com.enterprise.product.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * The filter authenticates a request only when it carries the correct internal
 * secret; SecurityConfig then turns any unauthenticated request into a 401.
 * The filter always continues the chain — rejection is Spring Security's job.
 */
class InternalSecretFilterTest {

    private static final String SECRET = "the-secret";

    private InternalSecretFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new InternalSecretFilter();
        ReflectionTestUtils.setField(filter, "internalSecret", SECRET);
        request = new MockHttpServletRequest("GET", "/api/products");
        response = new MockHttpServletResponse();
        chain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private Authentication currentAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Test
    @DisplayName("the correct secret authenticates the request and continues the chain")
    void authenticatesWithCorrectSecret() throws Exception {
        request.addHeader(InternalSecretFilter.SECRET_HEADER, SECRET);

        filter.doFilter(request, response, chain);

        assertThat(currentAuth()).isNotNull();
        assertThat(currentAuth().isAuthenticated()).isTrue();
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("carries the resolved user as the principal")
    void usesTheForwardedUserAsPrincipal() throws Exception {
        request.addHeader(InternalSecretFilter.SECRET_HEADER, SECRET);
        request.addHeader(InternalSecretFilter.USER_HEADER, "root");

        filter.doFilter(request, response, chain);

        assertThat(currentAuth().getPrincipal()).isEqualTo("root");
    }

    @Test
    @DisplayName("falls back to a 'gateway' principal when no user is forwarded")
    void defaultsPrincipalWhenNoUserHeader() throws Exception {
        request.addHeader(InternalSecretFilter.SECRET_HEADER, SECRET);

        filter.doFilter(request, response, chain);

        assertThat(currentAuth().getPrincipal()).isEqualTo("gateway");
    }

    @Test
    @DisplayName("a wrong secret leaves the request unauthenticated")
    void rejectsWrongSecret() throws Exception {
        request.addHeader(InternalSecretFilter.SECRET_HEADER, "not-the-secret");

        filter.doFilter(request, response, chain);

        assertThat(currentAuth()).isNull();
        // The chain still runs; SecurityConfig rejects the unauthenticated request.
        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("a missing secret leaves the request unauthenticated")
    void rejectsMissingSecret() throws Exception {
        filter.doFilter(request, response, chain);

        assertThat(currentAuth()).isNull();
        verify(chain).doFilter(request, response);
    }
}
