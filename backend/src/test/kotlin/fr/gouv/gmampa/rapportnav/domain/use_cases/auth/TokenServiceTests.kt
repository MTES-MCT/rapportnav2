package fr.gouv.gmampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthoritiesEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.entities.user.toAuthority
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
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
        fun `should include roles in claims`() {
            val claims = tokenService.getClaims(user)

            assertThat(claims.getClaim<List<RoleTypeEnum>>("roles")).isEqualTo(user.roles)
        }

        @Test
        fun `should include authorities in claims`() {
            val claims = tokenService.getClaims(user)

            val expectedAuthorities = user.roles.map { it.toAuthority() }
            assertThat(claims.getClaim<List<AuthoritiesEnum>>("authorities")).isEqualTo(expectedAuthorities)
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
        fun `should set expiration to 30 days from now`() {
            val expectedExpiration = Instant.now().plus(30L, ChronoUnit.DAYS)
            val claims = tokenService.getClaims(user)

            // Allow 1 second tolerance
            assertThat(claims.expiresAt).isCloseTo(expectedExpiration, org.assertj.core.api.Assertions.within(1, ChronoUnit.SECONDS))
        }

        @Test
        fun `should handle user with multiple roles`() {
            val multiRoleUser = UserMock.create(
                id = 5,
                roles = listOf(RoleTypeEnum.USER_ULAM, RoleTypeEnum.ADMIN)
            )

            val claims = tokenService.getClaims(multiRoleUser)

            assertThat(claims.getClaim<List<RoleTypeEnum>>("roles")).hasSize(2)
            assertThat(claims.getClaim<List<RoleTypeEnum>>("roles")).containsExactlyInAnyOrder(
                RoleTypeEnum.USER_ULAM,
                RoleTypeEnum.ADMIN
            )
        }

        @Test
        fun `should handle user with no roles`() {
            val noRoleUser = UserMock.create(
                id = 6,
                roles = emptyList()
            )

            val claims = tokenService.getClaims(noRoleUser)

            assertThat(claims.getClaim<List<RoleTypeEnum>>("roles")).isEmpty()
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
            assertThat(result?.id).isEqualTo(user.id)
        }

        @Test
        fun `should return null when token decoding fails`() {
            `when`(jwtDecoder.decode("invalid-token")).thenThrow(JwtException("Invalid token"))

            val result = tokenService.parseToken("invalid-token")

            assertThat(result).isNull()
        }

        @Test
        fun `should return null when user not found`() {
            val mockJwt = Jwt.withTokenValue("valid-token")
                .header("alg", "HS256")
                .claim("userId", 999)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .build()

            `when`(jwtDecoder.decode("valid-token")).thenReturn(mockJwt)
            `when`(userRepository.findById(999)).thenReturn(null)

            val result = tokenService.parseToken("valid-token")

            assertThat(result).isNull()
        }

        @Test
        fun `should return null when userId claim is missing`() {
            val mockJwt = Jwt.withTokenValue("no-userid-token")
                .header("alg", "HS256")
                .claim("other", "value")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .build()

            `when`(jwtDecoder.decode("no-userid-token")).thenReturn(mockJwt)

            val result = tokenService.parseToken("no-userid-token")

            assertThat(result).isNull()
        }
    }
}


