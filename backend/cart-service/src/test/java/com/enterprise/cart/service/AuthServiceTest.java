package com.enterprise.cart.service;

import com.enterprise.cart.dto.LoginResponse;
import com.enterprise.cart.exception.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        // Credentials are @Value-injected from configuration in production.
        ReflectionTestUtils.setField(authService, "configuredUsername", "root");
        ReflectionTestUtils.setField(authService, "configuredPassword", "root1234");
        ReflectionTestUtils.setField(authService, "sessionTtlMinutes", 480L);
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        void issuesATokenForCorrectCredentials() {
            LoginResponse response = authService.login("root", "root1234");

            assertThat(response.getUsername()).isEqualTo("root");
            assertThat(response.getToken()).isNotBlank();
            assertThat(response.getExpiresAt()).isAfter(Instant.now());
        }

        @Test
        void issuesADistinctTokenEachTime() {
            String first = authService.login("root", "root1234").getToken();
            String second = authService.login("root", "root1234").getToken();

            assertThat(first).isNotEqualTo(second);
        }

        @Test
        void rejectsAWrongPassword() {
            assertThatThrownBy(() -> authService.login("root", "wrong"))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        void rejectsAnUnknownUsername() {
            assertThatThrownBy(() -> authService.login("admin", "root1234"))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        /**
         * The message must not reveal which half was wrong, or it becomes a
         * username oracle.
         */
        @Test
        void usesAnIdenticalMessageForBothFailureModes() {
            String wrongUser = catchMessage(() -> authService.login("admin", "root1234"));
            String wrongPass = catchMessage(() -> authService.login("root", "wrong"));

            assertThat(wrongUser).isEqualTo(wrongPass);
        }

        @Test
        void rejectsNullCredentials() {
            assertThatThrownBy(() -> authService.login(null, null))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        void isCaseSensitive() {
            assertThatThrownBy(() -> authService.login("ROOT", "root1234"))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        private String catchMessage(Runnable runnable) {
            try {
                runnable.run();
                return null;
            } catch (RuntimeException e) {
                return e.getMessage();
            }
        }
    }

    @Nested
    @DisplayName("resolveToken")
    class ResolveToken {

        @Test
        void resolvesAFreshToken() {
            String token = authService.login("root", "root1234").getToken();

            assertThat(authService.resolveToken(token)).contains("root");
        }

        @Test
        void rejectsAnUnknownToken() {
            assertThat(authService.resolveToken("not-a-real-token")).isEmpty();
        }

        @Test
        void rejectsNullAndBlankTokens() {
            assertThat(authService.resolveToken(null)).isEmpty();
            assertThat(authService.resolveToken("")).isEmpty();
            assertThat(authService.resolveToken("   ")).isEmpty();
        }

        @Test
        void rejectsAnExpiredToken() {
            // A negative TTL mints an already-expired session, which exercises the
            // expiry branch without making the test wait or depend on timing.
            ReflectionTestUtils.setField(authService, "sessionTtlMinutes", -1L);
            String token = authService.login("root", "root1234").getToken();

            assertThat(authService.resolveToken(token)).isEmpty();
        }

        @Test
        void purgesExpiredSessionsOnNextLogin() {
            ReflectionTestUtils.setField(authService, "sessionTtlMinutes", -1L);
            String stale = authService.login("root", "root1234").getToken();

            ReflectionTestUtils.setField(authService, "sessionTtlMinutes", 480L);
            authService.login("root", "root1234");

            @SuppressWarnings("unchecked")
            var sessions = (java.util.Map<String, ?>)
                    ReflectionTestUtils.getField(authService, "sessions");
            assertThat(sessions).doesNotContainKey(stale);
        }
    }

    @Nested
    @DisplayName("logout")
    class Logout {

        @Test
        void revokesTheToken() {
            String token = authService.login("root", "root1234").getToken();
            assertThat(authService.resolveToken(token)).isPresent();

            authService.logout(token);

            assertThat(authService.resolveToken(token)).isEmpty();
        }

        @Test
        void leavesOtherSessionsIntact() {
            String first = authService.login("root", "root1234").getToken();
            String second = authService.login("root", "root1234").getToken();

            authService.logout(first);

            assertThat(authService.resolveToken(second)).contains("root");
        }

        @Test
        void toleratesUnknownAndNullTokens() {
            authService.logout("never-existed");
            authService.logout(null);
            // Reaching here without an exception is the assertion.
            assertThat(Optional.empty()).isEmpty();
        }
    }
}
