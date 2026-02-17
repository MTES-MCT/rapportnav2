package fr.gouv.gmampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.jwt.*
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.time.temporal.ChronoUnit

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TokenService::class])
class TokenServiceTests {

    @MockitoBean
    private lateinit var jwtDecoder: JwtDecoder

    @MockitoBean
    private lateinit var jwtEncoder: JwtEncoder

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    private lateinit var tokenService: TokenService

    private val user: User = UserMock.create(
        id = 3,
        email = "test@example.com",
        roles = listOf(RoleTypeEnum.USER_ULAM)
    )

    @BeforeEach
    fun setUp() {
        tokenService = TokenService(jwtDecoder, jwtEncoder, userRepository)
    }

    @Nested
    inner class GetClaims {

        @Test
        fun `should include userId in claims`() {
            val claims = tokenService.getClaims(user)

            assertThat(claims.getClaim<Int>("userId")).isEqualTo(user.id)
        }

        @Test
        fun `should include roles in claims for frontend UI convenience`() {
            val claims = tokenService.getClaims(user)

            // Roles are included for frontend routing/UI only
            // Backend authorization always loads fresh roles from database (see parseToken)
            assertThat(claims.getClaim<List<RoleTypeEnum>>("roles")).isEqualTo(user.roles)
        }

        @Test
        fun `should NOT include authorities in claims`() {
            val claims = tokenService.getClaims(user)

            // Authorities are not needed in JWT - backend loads them from DB
            assertThat(claims.getClaim<Any>("authorities")).isNull()
        }

        @Test
        fun `should set subject to user email`() {
            val claims = tokenService.getClaims(user)

            assertThat(claims.subject).isEqualTo(user.email)
        }

        @Test
        fun `should set issuedAt to current time`() {
            val before = Instant.now().minusSeconds(1)
            val claims = tokenService.getClaims(user)
            val after = Instant.now().plusSeconds(1)

            assertThat(claims.issuedAt).isAfterOrEqualTo(before)
            assertThat(claims.issuedAt).isBeforeOrEqualTo(after)
        }

        @Test
        fun `should set expiration to 15 days from now`() {
            val expectedExpiration = Instant.now().plus(15L, ChronoUnit.DAYS)
            val claims = tokenService.getClaims(user)

            // Allow 1 second tolerance
            assertThat(claims.expiresAt).isCloseTo(expectedExpiration, org.assertj.core.api.Assertions.within(1, ChronoUnit.SECONDS))
        }
    }

    @Nested
    inner class CreateToken {

        @Test
        fun `should call encoder with claims`() {
            val mockJwt = Jwt.withTokenValue("mock-token")
                .header("alg", "HS256")
                .claim("userId", user.id)
                .build()

            `when`(jwtEncoder.encode(any())).thenReturn(mockJwt)

            val token = tokenService.createToken(user)

            assertThat(token).isEqualTo("mock-token")
        }
    }

    @Nested
    inner class ParseToken {

        @Test
        fun `should return user when token is valid`() {
            val mockJwt = Jwt.withTokenValue("valid-token")
                .header("alg", "HS256")
                .claim("userId", user.id)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .build()

            `when`(jwtDecoder.decode("valid-token")).thenReturn(mockJwt)
            `when`(userRepository.findById(user.id!!)).thenReturn(user)

            val result = tokenService.parseToken("valid-token")

            assertThat(result).isNotNull
            assertThat(result.id).isEqualTo(user.id)
        }

        @Test
        fun `should throw INVALID_TOKEN_EXCEPTION when token decoding fails`() {
            `when`(jwtDecoder.decode("invalid-token")).thenThrow(JwtException("Invalid token"))

            assertThatThrownBy { tokenService.parseToken("invalid-token") }
                .isInstanceOf(BackendUsageException::class.java)
                .extracting("code")
                .isEqualTo(BackendUsageErrorCode.INVALID_TOKEN_EXCEPTION)
        }

        @Test
        fun `should throw USER_NOT_FOUND_EXCEPTION when user not found`() {
            val mockJwt = Jwt.withTokenValue("valid-token")
                .header("alg", "HS256")
                .claim("userId", 999)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .build()

            `when`(jwtDecoder.decode("valid-token")).thenReturn(mockJwt)
            `when`(userRepository.findById(999)).thenReturn(null)

            assertThatThrownBy { tokenService.parseToken("valid-token") }
                .isInstanceOf(BackendUsageException::class.java)
                .extracting("code")
                .isEqualTo(BackendUsageErrorCode.USER_NOT_FOUND_EXCEPTION)
        }

        @Test
        fun `should throw INVALID_TOKEN_EXCEPTION when userId claim is missing`() {
            val mockJwt = Jwt.withTokenValue("no-userid-token")
                .header("alg", "HS256")
                .claim("other", "value")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .build()

            `when`(jwtDecoder.decode("no-userid-token")).thenReturn(mockJwt)

            assertThatThrownBy { tokenService.parseToken("no-userid-token") }
                .isInstanceOf(BackendUsageException::class.java)
                .extracting("code")
                .isEqualTo(BackendUsageErrorCode.INVALID_TOKEN_EXCEPTION)
        }

        @Test
        fun `should throw USER_ACCOUNT_DISABLED_EXCEPTION when user is inactive`() {
            val inactiveUser = UserMock.create(
                id = 3,
                email = "test@example.com",
                roles = listOf(RoleTypeEnum.USER_ULAM),
                disabledAt = Instant.now()
            )

            val mockJwt = Jwt.withTokenValue("valid-token")
                .header("alg", "HS256")
                .claim("userId", inactiveUser.id)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .build()

            `when`(jwtDecoder.decode("valid-token")).thenReturn(mockJwt)
            `when`(userRepository.findById(inactiveUser.id!!)).thenReturn(inactiveUser)

            assertThatThrownBy { tokenService.parseToken("valid-token") }
                .isInstanceOf(BackendUsageException::class.java)
                .extracting("code")
                .isEqualTo(BackendUsageErrorCode.USER_ACCOUNT_DISABLED_EXCEPTION)
        }
    }
}


