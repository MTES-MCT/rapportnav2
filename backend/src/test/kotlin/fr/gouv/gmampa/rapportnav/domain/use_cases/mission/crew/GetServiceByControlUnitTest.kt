package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [GetServiceByControlUnit::class])
class GetServiceByControlUnitTest {

    @MockitoBean
    private lateinit var serviceRepository: IServiceRepository


    private val serviceModels: List<ServiceModel> = listOf(
        ServiceEntityMock.create(
            id = 3,
            name = "firstService",
            controlUnits = listOf(1, 3)
        ).toServiceModel(),
        ServiceEntityMock.create(
            id = 4,
            name = "SecondService",
            controlUnits = listOf(3, 4)
        ).toServiceModel(),
    )

    private val controlUnits: List<LegacyControlUnitEntity> = listOf(
        LegacyControlUnitEntity(
            id = 3,
            administration = "Administration",
            isArchived = false, name = "Themis",
            resources = mutableListOf(),
        )
    )


    @Test
    fun `execute should retrieve serviceModel with control units list`() {
        Mockito.`when`(serviceRepository.findByControlUnitId(listOf(3))).thenReturn(serviceModels)
        val getServiceByControlUnit = GetServiceByControlUnit(serviceRepository)
        val response = getServiceByControlUnit.execute(controlUnits)

        assertThat(response).isNotNull()
        assertThat(response.size).isEqualTo(2)
        assertThat(response.map { service -> service.id }).containsAll(listOf(3, 4))
        assertThat(response.get(0).id).isEqualTo(3)
        assertThat(response.get(1).id).isEqualTo(4)

    }
}
