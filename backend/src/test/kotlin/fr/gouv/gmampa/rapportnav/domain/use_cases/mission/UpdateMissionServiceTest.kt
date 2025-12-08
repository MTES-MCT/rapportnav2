package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import com.github.dockerjava.api.model.ServiceMode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.UpdateMissionService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.generalInfo.MissionServiceInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [UpdateMissionService::class])
class UpdateMissionServiceTest {
    @Autowired
    private lateinit var updateMissionService: UpdateMissionService

    @MockitoBean
    private lateinit var serviceRepo: IServiceRepository

    @MockitoBean
    private lateinit var missionCrewRepo: IMissionCrewRepository

    @MockitoBean
    private lateinit var infoRepo: IMissionGeneralInfoRepository

    @MockitoBean
    private lateinit var getActiveCrewForService: GetActiveCrewForService

    private val oldMissionCrews: List<MissionCrewModel> = listOf(
        MissionCrewModel(
            id = 3,
            missionId = 761,
            agent = AgentModel(id = 1, firstName = "", lastName = ""),
            role = AgentRoleModel(id = 1, title = ""),
            comment = ""
        )
    )

    private val newMissionCrews: List<AgentServiceEntity> = listOf(
        AgentServiceEntity(
            id = 3,
            agent = AgentEntity(id = 1, firstName = "", lastName = ""),
            role = AgentRoleEntity(id = 1, title = ""),
            service = ServiceEntityMock.create(id = 1, name = "")
        )
    )

    @Test
    fun `execute update mission service and mission crew member`() {
        val missionGeneralInfo = MissionGeneralInfoEntityMock.create(
            id = 1,
            missionId = 761,
            consumedFuelInLiters = 2.7f,
            consumedGOInLiters = 2.5f,
            distanceInNauticalMiles = 1.9f

        ).toMissionGeneralInfoModel()
        val input = MissionServiceInput(missionId = 761, serviceId = 3)
        val serviceModel = ServiceEntityMock.create(id = 3, name = "Themis_A").toServiceModel()
        Mockito.`when`(serviceRepo.findById(3)).thenReturn(Optional.of(serviceModel))
        Mockito.`when`(getActiveCrewForService.execute(3)).thenReturn(newMissionCrews)
        Mockito.`when`(missionCrewRepo.findByMissionId(761)).thenReturn(oldMissionCrews)
        Mockito.`when`(infoRepo.findByMissionId(761)).thenReturn(Optional.of(missionGeneralInfo))

        val response = updateMissionService.execute(input)
        assertThat(response).isNotNull()
        assertThat(response?.id).isEqualTo(3)
    }

    @Test
    fun `execute update mission service event if generalInfo is null`() {
        val input = MissionServiceInput(missionId = 761, serviceId = 3)
        val serviceModel = ServiceEntityMock.create(id = 3, name = "Themis_A").toServiceModel()
        Mockito.`when`(serviceRepo.findById(3)).thenReturn(Optional.of(serviceModel))
        Mockito.`when`(getActiveCrewForService.execute(3)).thenReturn(newMissionCrews)
        Mockito.`when`(missionCrewRepo.findByMissionId(761)).thenReturn(oldMissionCrews)
        Mockito.`when`(infoRepo.findByMissionId(761)).thenReturn(Optional.ofNullable(null))

        val response = updateMissionService.execute(input)
        assertThat(response).isNotNull()
        assertThat(response?.id).isEqualTo(3)
    }
}
