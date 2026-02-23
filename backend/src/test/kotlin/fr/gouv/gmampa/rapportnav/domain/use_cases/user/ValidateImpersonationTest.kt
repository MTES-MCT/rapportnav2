package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.ValidateImpersonation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [ValidateImpersonation::class])
@ContextConfiguration(classes = [ValidateImpersonation::class])
class ValidateImpersonationTest {

    @Autowired
    private lateinit var validateImpersonation: ValidateImpersonation

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    @Test
    fun `should return service when admin impersonates valid active service`() {
        val adminUser = User(
            id = 1,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN)
        )
        val targetService = ServiceEntity(
            id = 200,
            name = "Target Service",
            serviceType = ServiceTypeEnum.PAM
        )

        `when`(getServiceById.execute(200)).thenReturn(targetService)

        val result = validateImpersonation.execute(adminUser, 200)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(200)
        assertThat(result.name).isEqualTo("Target Service")
    }

    @Test
    fun `should throw exception when non-admin tries to impersonate`() {
        val regularUser = User(
            id = 1,
            firstName = "Regular",
            lastName = "User",
            email = "user@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        val exception = assertThrows<IllegalArgumentException> {
            validateImpersonation.execute(regularUser, 200)
        }

        assertThat(exception.message).isEqualTo("Only ADMIN users can impersonate services")
    }

    @Test
    fun `should throw exception when service does not exist`() {
        val adminUser = User(
            id = 1,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN)
        )

        `when`(getServiceById.execute(999)).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            validateImpersonation.execute(adminUser, 999)
        }

        assertThat(exception.message).isEqualTo("Service with id 999 does not exist")
    }

    @Test
    fun `should throw exception when service is deleted`() {
        val adminUser = User(
            id = 1,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN)
        )
        val deletedService = ServiceEntity(
            id = 200,
            name = "Deleted Service",
            serviceType = ServiceTypeEnum.PAM,
            deletedAt = Instant.now()
        )

        `when`(getServiceById.execute(200)).thenReturn(deletedService)

        val exception = assertThrows<IllegalArgumentException> {
            validateImpersonation.execute(adminUser, 200)
        }

        assertThat(exception.message).isEqualTo("Service with id 200 has been deleted")
    }
}
