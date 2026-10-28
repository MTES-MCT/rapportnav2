package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.isPamShip
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit.IEnvControlUnitRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetPamShipResource
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetPamShipResource::class])
class GetPamShipResourceTest {

    @Autowired
    private lateinit var useCase: GetPamShipResource

    @MockitoBean
    private lateinit var repository: IEnvControlUnitRepository

    private val pamIris = ServiceEntityMock.create(id = 5, serviceType = ServiceTypeEnum.PAM, controlUnits = listOf(10404))

    private val irisShip = ControlResourceEntity(id = 582, name = "Iris", type = ControlUnitResourceType.PATROL_BOAT)
    private val irisCar = ControlResourceEntity(id = 1116, name = "Voiture", type = ControlUnitResourceType.CAR)

    private fun controlUnit(id: Int, vararg resources: ControlResourceEntity) = ControlUnitEntity(
        id = id, administration = "DAM", isArchived = false, name = "unit $id", resources = resources.toList()
    )

    @Test
    fun `isPamShip only matches PATROL_BOAT`() {
        assertThat(ControlUnitResourceType.entries.filter { it.isPamShip() })
            .containsExactly(ControlUnitResourceType.PATROL_BOAT)
    }

    @Test
    fun `returns the single ship of the service's control unit`() {
        whenever(repository.findById(10404)).thenReturn(controlUnit(10404, irisCar, irisShip))

        assertThat(useCase.execute(pamIris)).isEqualTo(
            LegacyControlUnitResourceEntity(id = 582, controlUnitId = 10404, name = "Iris", type = ControlUnitResourceType.PATROL_BOAT)
        )
        assertThat(useCase.execute(pamIris)?.name).isEqualTo("Iris")
    }

    @Test
    fun `returns null when the control unit has no ship`() {
        whenever(repository.findById(10404)).thenReturn(controlUnit(10404, irisCar))

        assertThat(useCase.execute(pamIris)).isNull()
    }

    @Test
    fun `returns null when the control unit has several ships`() {
        whenever(repository.findById(10404)).thenReturn(controlUnit(10404, irisShip, irisShip.copy(id = 583)))

        assertThat(useCase.execute(pamIris)).isNull()
    }

    @Test
    fun `ignores archived ships`() {
        val archivedShip = irisShip.copy(id = 100, isArchived = true)
        whenever(repository.findById(10404)).thenReturn(controlUnit(10404, archivedShip, irisShip))

        assertThat(useCase.execute(pamIris)?.id).isEqualTo(582)
    }

    @Test
    fun `ignores a boat-type tender and resources of unknown type`() {
        val tender = ControlResourceEntity(id = 600, type = ControlUnitResourceType.SEMI_RIGID)
        val unknownType = ControlResourceEntity(id = 601, type = null)
        whenever(repository.findById(10404)).thenReturn(controlUnit(10404, irisShip, tender, unknownType))

        assertThat(useCase.execute(pamIris)?.id).isEqualTo(582)
    }

    @Test
    fun `fetches each control unit of the service once and keeps the one ship`() {
        val service = ServiceEntityMock.create(id = 9, serviceType = ServiceTypeEnum.PAM, controlUnits = listOf(10404, 99999, 10404))
        whenever(repository.findById(10404)).thenReturn(controlUnit(10404, irisShip, irisCar))
        whenever(repository.findById(99999)).thenReturn(controlUnit(99999))

        assertThat(useCase.execute(service)?.id).isEqualTo(582)
        verify(repository, times(1)).findById(10404)
        verify(repository, times(1)).findById(99999)
    }

    @Test
    fun `returns null without calling MonitorEnv for a non-PAM or missing service`() {
        val ulam = ServiceEntityMock.create(id = 20, serviceType = ServiceTypeEnum.ULAM, controlUnits = listOf(10176))

        assertThat(useCase.execute(ulam)).isNull()
        assertThat(useCase.execute(null)).isNull()
        assertThat(useCase.resolve(ulam)).isNull()

        verify(repository, never()).findById(any())
    }

    @Test
    fun `returns null without calling MonitorEnv when the PAM service has no control unit`() {
        val service = ServiceEntityMock.create(id = 5, serviceType = ServiceTypeEnum.PAM, controlUnits = emptyList())

        assertThat(useCase.execute(service)).isNull()

        verify(repository, never()).findById(any())
    }

    @Test
    fun `execute returns null when MonitorEnv fails`() {
        whenever(repository.findById(10404)).thenThrow(BackendInternalException("MonitorEnv down"))

        assertThat(useCase.execute(pamIris)).isNull()
    }

    @Test
    fun `resolve lets a MonitorEnv failure propagate`() {
        whenever(repository.findById(10404)).thenThrow(BackendInternalException("MonitorEnv down"))

        assertThrows<BackendInternalException> { useCase.resolve(pamIris) }
    }
}
