package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.ImpersonationContext
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendForbiddenErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendForbiddenException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.ImpersonationContextHolder
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.LogImpersonationAudit
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.ProcessImpersonationRequest
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.ValidateImpersonation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ProcessImpersonationRequestTest {

    private lateinit var validateImpersonation: ValidateImpersonation
    private lateinit var logImpersonationAudit: LogImpersonationAudit
    private lateinit var impersonationContextHolder: ImpersonationContextHolder
    private lateinit var processImpersonationRequest: ProcessImpersonationRequest

    @BeforeEach
    fun setUp() {
        validateImpersonation = mock()
        logImpersonationAudit = mock()
        impersonationContextHolder = mock()
        processImpersonationRequest = ProcessImpersonationRequest(
            validateImpersonation,
            logImpersonationAudit,
            impersonationContextHolder
        )
    }

    @Test
    fun `should return original user when targetServiceId is null`() {
        val user = User(
            id = 1,
            serviceId = 100,
            firstName = "Test",
            lastName = "User",
            email = "test@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        val result = processImpersonationRequest.execute(user, null, "127.0.0.1")

        assertThat(result).isEqualTo(user)
        verify(validateImpersonation, never()).execute(any(), any())
        verify(logImpersonationAudit, never()).execute(any(), any(), any())
        verify(impersonationContextHolder, never()).set(any())
    }

    @Test
    fun `should throw BackendForbiddenException when user is not admin`() {
        val regularUser = User(
            id = 1,
            serviceId = 100,
            firstName = "Regular",
            lastName = "User",
            email = "user@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        val exception = assertThrows<BackendForbiddenException> {
            processImpersonationRequest.execute(regularUser, 200, "127.0.0.1")
        }

        assertThat(exception.code).isEqualTo(BackendForbiddenErrorCode.UNAUTHORIZED_IMPERSONATION)
        assertThat(exception.message).isEqualTo("Impersonation requires ADMIN role")
        verify(validateImpersonation, never()).execute(any(), any())
        verify(logImpersonationAudit, never()).execute(any(), any(), any())
        verify(impersonationContextHolder, never()).set(any())
    }

    @Test
    fun `should impersonate service for admin user`() {
        val adminUser = User(
            id = 1,
            serviceId = 100,
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

        whenever(validateImpersonation.execute(adminUser, 200)).thenReturn(targetService)

        val result = processImpersonationRequest.execute(adminUser, 200, "127.0.0.1")

        assertThat(result.serviceId).isEqualTo(200)
        assertThat(result.id).isEqualTo(adminUser.id)
        assertThat(result.email).isEqualTo(adminUser.email)
    }

    @Test
    fun `should remove ROLE_ADMIN from roles when impersonating`() {
        val adminUser = User(
            id = 1,
            serviceId = 100,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN, RoleTypeEnum.USER_PAM)
        )
        val targetService = ServiceEntity(
            id = 200,
            name = "Target Service",
            serviceType = ServiceTypeEnum.PAM
        )

        whenever(validateImpersonation.execute(adminUser, 200)).thenReturn(targetService)

        val result = processImpersonationRequest.execute(adminUser, 200, "127.0.0.1")

        assertThat(result.roles).doesNotContain(RoleTypeEnum.ADMIN)
        assertThat(result.roles).containsExactly(RoleTypeEnum.USER_PAM)
    }

    @Test
    fun `should set impersonation context when impersonating`() {
        val adminUser = User(
            id = 1,
            serviceId = 100,
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

        whenever(validateImpersonation.execute(adminUser, 200)).thenReturn(targetService)

        processImpersonationRequest.execute(adminUser, 200, "127.0.0.1")

        val contextCaptor = argumentCaptor<ImpersonationContext>()
        verify(impersonationContextHolder).set(contextCaptor.capture())

        val capturedContext = contextCaptor.firstValue
        assertThat(capturedContext.isActive).isTrue()
        assertThat(capturedContext.originalServiceId).isEqualTo(100)
        assertThat(capturedContext.impersonatedServiceId).isEqualTo(200)
        assertThat(capturedContext.impersonatedServiceName).isEqualTo("Target Service")
    }

    @Test
    fun `should log audit entry when impersonating`() {
        val adminUser = User(
            id = 1,
            serviceId = 100,
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

        whenever(validateImpersonation.execute(adminUser, 200)).thenReturn(targetService)

        processImpersonationRequest.execute(adminUser, 200, "192.168.1.1")

        verify(logImpersonationAudit).execute(
            adminUserId = 1,
            targetServiceId = 200,
            ipAddress = "192.168.1.1"
        )
    }

    @Test
    fun `should throw BackendForbiddenException when validation fails`() {
        val adminUser = User(
            id = 1,
            serviceId = 100,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN)
        )

        whenever(validateImpersonation.execute(adminUser, 999))
            .thenThrow(IllegalArgumentException("Service not found"))

        val exception = assertThrows<BackendForbiddenException> {
            processImpersonationRequest.execute(adminUser, 999, "127.0.0.1")
        }

        assertThat(exception.code).isEqualTo(BackendForbiddenErrorCode.UNAUTHORIZED_IMPERSONATION)
        assertThat(exception.message).contains("Invalid impersonation target")
        verify(logImpersonationAudit, never()).execute(any(), any(), any())
        verify(impersonationContextHolder, never()).set(any())
    }

    @Test
    fun `should handle null user id when logging audit`() {
        val adminUser = User(
            id = null,
            serviceId = 100,
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

        whenever(validateImpersonation.execute(adminUser, 200)).thenReturn(targetService)

        processImpersonationRequest.execute(adminUser, 200, "127.0.0.1")

        verify(logImpersonationAudit).execute(
            adminUserId = 0,
            targetServiceId = 200,
            ipAddress = "127.0.0.1"
        )
    }

    @Test
    fun `should preserve non-admin user properties when impersonating`() {
        val adminUser = User(
            id = 1,
            serviceId = 100,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN, RoleTypeEnum.USER_PAM, RoleTypeEnum.USER_ULAM)
        )
        val targetService = ServiceEntity(
            id = 200,
            name = "Target Service",
            serviceType = ServiceTypeEnum.PAM
        )

        whenever(validateImpersonation.execute(adminUser, 200)).thenReturn(targetService)

        val result = processImpersonationRequest.execute(adminUser, 200, "127.0.0.1")

        assertThat(result.id).isEqualTo(1)
        assertThat(result.firstName).isEqualTo("Admin")
        assertThat(result.lastName).isEqualTo("User")
        assertThat(result.email).isEqualTo("admin@test.com")
        assertThat(result.password).isEqualTo("password")
        assertThat(result.serviceId).isEqualTo(200)
        // ROLE_ADMIN should be removed, but other roles preserved
        assertThat(result.roles).containsExactlyInAnyOrder(RoleTypeEnum.USER_PAM, RoleTypeEnum.USER_ULAM)
        assertThat(result.roles).doesNotContain(RoleTypeEnum.ADMIN)
    }

    @Test
    fun `should throw BackendForbiddenException when audit logging fails (fail-closed)`() {
        val adminUser = User(
            id = 1,
            serviceId = 100,
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

        whenever(validateImpersonation.execute(adminUser, 200)).thenReturn(targetService)
        whenever(logImpersonationAudit.execute(any(), any(), any()))
            .thenThrow(BackendInternalException(BackendInternalErrorCode.AUDIT_LOGGING_FAILED, "Database connection failed"))

        val exception = assertThrows<BackendForbiddenException> {
            processImpersonationRequest.execute(adminUser, 200, "127.0.0.1")
        }

        assertThat(exception.code).isEqualTo(BackendForbiddenErrorCode.UNAUTHORIZED_IMPERSONATION)
        assertThat(exception.message).contains("audit system error")
        // Context should NOT be set if audit fails
        verify(impersonationContextHolder, never()).set(any())
    }
}
