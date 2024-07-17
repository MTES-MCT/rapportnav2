package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.UpdateMissionService
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.generalInfo.MissionServiceInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@SpringBootTest(classes = [UpdateMissionService::class])
class UpdateMissionServiceTest {
    @Autowired
    private lateinit var updateMissionService: UpdateMissionService

    @MockBean
    private lateinit var serviceRepo: IServiceRepository;

    @MockBean
    private lateinit var missionCrewRepo: IMissionCrewRepository;

    @MockBean
    private lateinit var infoRepo: IMissionGeneralInfoRepository;

    @MockBean
    private lateinit var agentServiceRepo: IAgentServiceRepository;

    private val oldMissionCrews: List<MissionCrewModel> = listOf(
        MissionCrewModel(
            id = 3,
            missionId = 761,
            agent = AgentModel(id = 1, firstName = "", lastName = ""),
            role = AgentRoleModel(id = 1, title = ""),
            comment = ""
        )
    )

    private val newMissionCrews: List<AgentServiceModel> = listOf(
        AgentServiceModel(
            id = 3,
            agent = AgentModel(id = 1, firstName = "", lastName = ""),
            role = AgentRoleModel(id = 1, title = ""),
            serviceId = 3
        )
    )

    @Test
    fun `execute update mission service and mission crew member`() {
        val missionGeneralInfo = MissionGeneralInfoModel(
            id = 1,
            missionId = 761,
            serviceId = 2,
            consumedFuelInLiters = 2.7f,
            consumedGOInLiters = 2.5f,
            distanceInNauticalMiles = 1.9f

        );
        val input = MissionServiceInput(missionId = 761, serviceId = 3)
        val serviceModel = ServiceModel(id = 3, name="Themis_A");
        Mockito.`when`(serviceRepo.findById(3)).thenReturn(Optional.of(serviceModel));
        Mockito.`when`(agentServiceRepo.findByServiceId(3)).thenReturn(newMissionCrews);
        Mockito.`when`(missionCrewRepo.findByMissionId(761)).thenReturn(oldMissionCrews);
        Mockito.`when`(infoRepo.findByMissionId(761)).thenReturn(Optional.of(missionGeneralInfo));

        val response = updateMissionService.execute(input);
        assertThat(response).isNotNull();
        assertThat(response?.id).isEqualTo(3);
    }

    @Test
    fun `execute update mission service event if generalInfo is null`() {
        val input = MissionServiceInput(missionId = 761, serviceId = 3)
        val serviceModel = ServiceModel(id = 3, name="Themis_A");
        Mockito.`when`(serviceRepo.findById(3)).thenReturn(Optional.of(serviceModel));
        Mockito.`when`(agentServiceRepo.findByServiceId(3)).thenReturn(newMissionCrews);
        Mockito.`when`(missionCrewRepo.findByMissionId(761)).thenReturn(oldMissionCrews);
        Mockito.`when`(infoRepo.findByMissionId(761)).thenReturn(Optional.ofNullable(null));

        val response = updateMissionService.execute(input);
        assertThat(response).isNotNull();
        assertThat(response?.id).isEqualTo(3);
    }
}
