package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserServiceScope
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.util.*

class GetUserServiceScopeTest {

    private val getUserFromToken: GetUserFromToken = mock(GetUserFromToken::class.java)
    private val serviceRepo: IServiceRepository = mock(IServiceRepository::class.java)

    private val getUserServiceScope = GetUserServiceScope(
        getUserFromToken = getUserFromToken,
        serviceRepo = serviceRepo,
    )

    private fun service(id: Int, controlUnits: List<Int>? = null, serviceLinkedId: Int? = null) =
        ServiceModel(
            id = id,
            name = "service-$id",
            serviceType = ServiceTypeEnum.PAM,
            controlUnits = controlUnits,
            serviceLinkedId = serviceLinkedId,
        )

    @Test
    fun `resolves the own service plus forward and reverse linked services (symmetric)`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        // self (10) links forward to 20; 30 links back to 10 (reverse)
        whenever(serviceRepo.findById(10)).thenReturn(Optional.of(service(10, controlUnits = listOf(100), serviceLinkedId = 20)))
        whenever(serviceRepo.findById(20)).thenReturn(Optional.of(service(20, controlUnits = listOf(200))))
        whenever(serviceRepo.findByServiceLinkedId(10)).thenReturn(listOf(service(30, controlUnits = listOf(300), serviceLinkedId = 10)))

        val scope = getUserServiceScope.execute()

        assertThat(scope.serviceIds).containsExactlyInAnyOrder(10, 20, 30)
        assertThat(scope.controlUnitIds).containsExactlyInAnyOrder(100, 200, 300)
    }

    @Test
    fun `resolves just the own service when there are no links`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        whenever(serviceRepo.findById(10)).thenReturn(Optional.of(service(10, controlUnits = listOf(100))))
        whenever(serviceRepo.findByServiceLinkedId(10)).thenReturn(emptyList())

        val scope = getUserServiceScope.execute()

        assertThat(scope.serviceIds).containsExactly(10)
        assertThat(scope.controlUnitIds).containsExactly(100)
    }

    @Test
    fun `returns an empty scope when there is no authenticated user`() {
        whenever(getUserFromToken.execute()).thenReturn(null)

        val scope = getUserServiceScope.execute()

        assertThat(scope.serviceIds).isEmpty()
        assertThat(scope.controlUnitIds).isEmpty()
    }

    @Test
    fun `returns an empty scope when the user has no service`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = null))

        val scope = getUserServiceScope.execute()

        assertThat(scope.serviceIds).isEmpty()
        assertThat(scope.controlUnitIds).isEmpty()
    }

    @Test
    fun `keeps the user's own service in scope when its service row cannot be loaded`() {
        whenever(getUserFromToken.execute()).thenReturn(UserMock.create(id = 1, serviceId = 10))
        // The user's own service row is unreadable (soft-deleted / transient failure)...
        whenever(serviceRepo.findById(10)).thenReturn(Optional.empty())
        whenever(serviceRepo.findByServiceLinkedId(10)).thenReturn(emptyList())

        val scope = getUserServiceScope.execute()

        // ...but the authoritative serviceId from the token keeps the user able to reach their OWN data.
        assertThat(scope.serviceIds).containsExactly(10)
        assertThat(scope.controlUnitIds).isEmpty()
    }
}
