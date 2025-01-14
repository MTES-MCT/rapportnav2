package fr.gouv.gmampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TokenService::class])
class TokenServiceTests {

    @MockitoBean
    private lateinit var jwtDecoder: JwtDecoder

    @MockitoBean
    private lateinit var jwtEncoder: JwtEncoder

    @MockitoBean
    private lateinit var userRepository: IUserRepository


    private val user: User = User(
        id = 3,
        firstName = "Jean",
        lastName = "Dupont",
        email = "jean.dupont@mer.gouv.fr",
        password = "MyBeautifulPassword",
        serviceId = 6,
        roles = listOf(RoleTypeEnum.USER_ULAM)
    )


    @Test
    fun `execute should have roles with claim user id and user role`() {
        val tokenService = TokenService(jwtDecoder, jwtEncoder, userRepository)
        val claims = tokenService.getClaims(user)
        assertThat(claims).isNotNull()

        assertThat(claims.getClaim<Int>("userId")).isEqualTo(user.id)
        assertThat(claims.getClaim<List<RoleTypeEnum>>("roles")).isEqualTo(user.roles)
    }

}


