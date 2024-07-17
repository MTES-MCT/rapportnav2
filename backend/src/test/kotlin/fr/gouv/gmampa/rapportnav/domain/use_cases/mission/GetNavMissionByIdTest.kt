package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetNavMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [GetNavMissionById::class])
class GetNavMissionByIdTest {

    @Autowired
    private lateinit var getMissionNavById: GetNavMissionById

    @MockBean
    private lateinit var getServiceByControlUnit: GetServiceByControlUnit

    private val controlUnits: List<LegacyControlUnitEntity> = listOf(
        LegacyControlUnitEntity(
            id = 3,
            administration = "Administration",
            isArchived = false, name = "Themis",
            resources = listOf(),
        )
    )

    @MockBean
    private lateinit var navActionControlRepository: INavActionControlRepository;

    @MockBean
    private lateinit var navStatusRepository: INavActionStatusRepository;

    @MockBean
    private lateinit var navFreeNoteRepository: INavActionFreeNoteRepository;

    @MockBean
    private lateinit var attachControlsToActionControl: AttachControlsToActionControl;

    @MockBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId;

    @MockBean
    private lateinit var getAgentsCrewByMissionId: GetAgentsCrewByMissionId;

    @MockBean
    private lateinit var navRescueRepository: INavActionRescueRepository;

    @MockBean
    private lateinit var navNauticalEventRepository: INavActionNauticalEventRepository;

    @MockBean
    private lateinit var navVigimerRepository: INavActionVigimerRepository;

    @MockBean
    private lateinit var navAntiPollutionRepository: INavActionAntiPollutionRepository;

    @MockBean
    private lateinit var navBAAEMRepository: INavActionBAAEMRepository;

    @MockBean
    private lateinit var navPublicOrderRepository: INavActionPublicOrderRepository;

    @MockBean
    private lateinit var navRepresentationRepository: INavActionRepresentationRepository;

    @MockBean
    private lateinit var navIllegalImmigrationRepository: INavActionIllegalImmigrationRepository

    private val serviceEntities: List<ServiceEntity> = listOf(
        ServiceEntity(
            id = 3,
            name = "firstService",
            controlUnits = listOf(1, 3)
        ), ServiceEntity(
            id = 4,
            name = "SecondService",
            controlUnits = listOf(3, 4)
        )
    );

    @Test
    fun `execute should have services in nav mission entity`() {
        Mockito.`when`(getServiceByControlUnit.execute(controlUnits)).thenReturn(serviceEntities);
        val response = getMissionNavById.execute(2, controlUnits)

        assertThat(response).isNotNull();
        assertThat(response.services).isNotNull();
        assertThat(response.services?.size).isEqualTo(2);
        assertThat(response.services?.map { service -> service.id }).containsAll(listOf(3, 4));
    }

}
