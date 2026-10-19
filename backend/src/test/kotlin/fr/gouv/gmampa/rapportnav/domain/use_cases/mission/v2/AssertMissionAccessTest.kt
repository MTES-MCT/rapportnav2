package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiryById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.AssertMissionAccess
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserServiceScope
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.LegacyControlUnitEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionNavEntityMock
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.util.*

class AssertMissionAccessTest {

    private val getUserFromToken: GetUserFromToken = mock(GetUserFromToken::class.java)
    private val getNavMissionById: GetNavMissionById = mock(GetNavMissionById::class.java)
    private val getEnvMissionById: GetEnvMissionById = mock(GetEnvMissionById::class.java)
    private val getUserServiceScope: GetUserServiceScope = mock(GetUserServiceScope::class.java)
    private val getInquiryById: GetInquiryById = mock(GetInquiryById::class.java)

    private val assertMissionAccess = AssertMissionAccess(
        getUserFromToken = getUserFromToken,
        getNavMissionById = getNavMissionById,
        getEnvMissionById = getEnvMissionById,
        getUserServiceScope = getUserServiceScope,
        getInquiryById = getInquiryById,
    )

    private fun givenScope(serviceIds: Set<Int> = emptySet(), controlUnitIds: Set<Int> = emptySet()) {
        whenever(getUserServiceScope.execute())
            .thenReturn(GetUserServiceScope.Scope(serviceIds = serviceIds, controlUnitIds = controlUnitIds))
    }

    // ---- nav missions (UUID id, owned by serviceId) ----

    @Test
    fun `allows access to a nav mission of the user's own service`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(serviceIds = setOf(10))
        whenever(getNavMissionById.execute(id)).thenReturn(MissionNavEntityMock.create(id = id, serviceId = 10))

        // no exception thrown
        assertMissionAccess.execute(id.toString())
    }

    @Test
    fun `allows access to a nav mission of a linked service`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        // user's service 10 is linked to service 20 => scope contains both
        givenScope(serviceIds = setOf(10, 20))
        whenever(getNavMissionById.execute(id)).thenReturn(MissionNavEntityMock.create(id = id, serviceId = 20))

        assertMissionAccess.execute(id.toString())
    }

    @Test
    fun `denies access to a nav mission of another service`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(serviceIds = setOf(10))
        whenever(getNavMissionById.execute(id)).thenReturn(MissionNavEntityMock.create(id = id, serviceId = 99))

        assertThatThrownBy { assertMissionAccess.execute(id.toString()) }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION)
    }

    @Test
    fun `throws not found when the nav mission does not exist`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(serviceIds = setOf(10))
        whenever(getNavMissionById.execute(id)).thenReturn(null)

        assertThatThrownBy { assertMissionAccess.execute(id.toString()) }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
    }

    // ---- env missions (Int id, owned by control units) ----

    @Test
    fun `allows access to an env mission within the user's control units`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(controlUnitIds = setOf(100, 200))
        whenever(getEnvMissionById.execute(5)).thenReturn(
            EnvMissionMock.create(id = 5, controlUnits = listOf(LegacyControlUnitEntityMock.create(id = 200)))
        )

        assertMissionAccess.execute("5")
    }

    @Test
    fun `allows access to an env mission within a linked service's control units`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        // 300 belongs to the linked service, unioned into the scope's control units
        givenScope(controlUnitIds = setOf(100, 300))
        whenever(getEnvMissionById.execute(5)).thenReturn(
            EnvMissionMock.create(id = 5, controlUnits = listOf(LegacyControlUnitEntityMock.create(id = 300)))
        )

        assertMissionAccess.execute("5")
    }

    @Test
    fun `denies access to an env mission outside the user's control units`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(controlUnitIds = setOf(100, 200))
        whenever(getEnvMissionById.execute(5)).thenReturn(
            EnvMissionMock.create(id = 5, controlUnits = listOf(LegacyControlUnitEntityMock.create(id = 999)))
        )

        assertThatThrownBy { assertMissionAccess.execute("5") }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION)
    }

    @Test
    fun `treats an env-mirror nav row as an env mission and checks control units`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        // nav row carrying an external id => owned through control units, not serviceId
        whenever(getNavMissionById.execute(id))
            .thenReturn(MissionNavEntityMock.create(id = id, serviceId = 99, externalId = "7"))
        givenScope(controlUnitIds = setOf(100))
        whenever(getEnvMissionById.execute(7)).thenReturn(
            EnvMissionMock.create(id = 7, controlUnits = listOf(LegacyControlUnitEntityMock.create(id = 100)))
        )

        assertMissionAccess.execute(id.toString())
    }

    // ---- admin bypass & edge cases ----

    @Test
    fun `admin bypasses the ownership check`() {
        whenever(getUserFromToken.execute())
            .thenReturn(UserMock.create(id = 1, serviceId = 10, roles = listOf(RoleTypeEnum.ADMIN)))

        // Any id is allowed; mission lookups and scope are never consulted.
        assertMissionAccess.execute("5")
        assertMissionAccess.execute(UUID.randomUUID().toString())
    }

    @Test
    fun `denies when there is no authenticated user`() {
        whenever(getUserFromToken.execute()).thenReturn(null)

        assertThatThrownBy { assertMissionAccess.execute("5") }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION)
    }

    @Test
    fun `denies an env mission when the user has no control units`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(controlUnitIds = emptySet())
        whenever(getEnvMissionById.execute(5)).thenReturn(
            EnvMissionMock.create(id = 5, controlUnits = listOf(LegacyControlUnitEntityMock.create(id = 100)))
        )

        assertThatThrownBy { assertMissionAccess.execute("5") }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION)
    }

    @Test
    fun `rejects a malformed mission id`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(serviceIds = setOf(10))

        assertThatThrownBy { assertMissionAccess.execute("not-a-uuid-nor-int") }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION)
    }

    @Test
    fun `denies when the nav mission belongs to a null service and the user has a service`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(serviceIds = setOf(10))
        whenever(getNavMissionById.execute(id)).thenReturn(MissionNavEntityMock.create(id = id, serviceId = 0))

        // serviceId 0 not in scope {10} => denied (sanity on the membership check)
        assertThat(
            runCatching { assertMissionAccess.execute(id.toString()) }.exceptionOrNull()
        ).isInstanceOf(BackendUsageException::class.java)
    }

    // ---- inquiries (UUID id, owned by serviceId like nav missions) ----

    @Test
    fun `allows access to an inquiry of the user's own service`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(serviceIds = setOf(10))
        whenever(getInquiryById.execute(id)).thenReturn(InquiryEntity(id = id, serviceId = 10))

        // no exception thrown
        assertMissionAccess.executeForInquiry(id.toString())
    }

    @Test
    fun `allows access to an inquiry of a linked service`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(serviceIds = setOf(10, 20))
        whenever(getInquiryById.execute(id)).thenReturn(InquiryEntity(id = id, serviceId = 20))

        assertMissionAccess.executeForInquiry(id.toString())
    }

    @Test
    fun `denies access to an inquiry of another service`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(serviceIds = setOf(10))
        whenever(getInquiryById.execute(id)).thenReturn(InquiryEntity(id = id, serviceId = 99))

        assertThatThrownBy { assertMissionAccess.executeForInquiry(id.toString()) }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION)
    }

    @Test
    fun `throws not found when the inquiry does not exist`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        whenever(getInquiryById.execute(id)).thenReturn(null)

        assertThatThrownBy { assertMissionAccess.executeForInquiry(id.toString()) }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
    }

    @Test
    fun `admin bypasses the inquiry ownership check`() {
        whenever(getUserFromToken.execute())
            .thenReturn(UserMock.create(id = 1, serviceId = 10, roles = listOf(RoleTypeEnum.ADMIN)))

        // inquiry lookup and scope are never consulted
        assertMissionAccess.executeForInquiry(UUID.randomUUID().toString())
    }

    @Test
    fun `denies an inquiry with a null service even when the user has a service`() {
        val id = UUID.randomUUID()
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        givenScope(serviceIds = setOf(10))
        whenever(getInquiryById.execute(id)).thenReturn(InquiryEntity(id = id, serviceId = null))

        assertThatThrownBy { assertMissionAccess.executeForInquiry(id.toString()) }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION)
    }

    @Test
    fun `rejects a malformed inquiry id`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))

        assertThatThrownBy { assertMissionAccess.executeForInquiry("not-a-uuid") }
            .isInstanceOf(BackendUsageException::class.java)
            .extracting("code")
            .isEqualTo(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION)
    }
}
