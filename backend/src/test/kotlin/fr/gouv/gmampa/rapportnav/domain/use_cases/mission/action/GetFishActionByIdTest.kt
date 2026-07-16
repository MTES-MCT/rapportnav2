package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessFishAction
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionModelMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetFishActionById::class])
@ContextConfiguration(classes = [GetFishActionById::class])
class GetFishActionByIdTest {

    @Autowired
    private lateinit var getFishActionById: GetFishActionById

    @MockitoBean
    private lateinit var getFishActionListByMissionId: GetFishActionListByMissionId

    @MockitoBean
    private lateinit var processFishAction: ProcessFishAction

    @MockitoBean
    private lateinit var missionNavRepository: IMissionNavRepository

    @Test
    fun `test execute get Fish action by id`() {

        val missionId = 123
        val actionId = UUID.randomUUID().hashCode()
        val action = FishActionControlMock.create(
            id = actionId,
        )

        val response = MissionFishActionEntity(
            id = actionId,
            ownerId = UUID.randomUUID(),
            fishActionType = MissionActionType.SEA_CONTROL,
            actionDatetimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            actionEndDatetimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            speciesQuantitySeized = 4
        )

        val missionUUID = UUID.randomUUID()
        val missionModel = MissionModelMock.create(id = missionUUID)
        `when`(missionNavRepository.findByExternalId(missionId.toString())).thenReturn(Optional.of(missionModel))
        `when`(processFishAction.execute(anyOrNull(), anyOrNull())).thenReturn(response)
        `when`(getFishActionListByMissionId.execute(missionId)).thenReturn(listOf(action))

        getFishActionById = GetFishActionById(
            processFishAction = processFishAction,
            getFishActionListByMissionId = getFishActionListByMissionId,
            missionNavRepository = missionNavRepository
        )
        val missionEnvAction = getFishActionById.execute(missionId = missionId, actionId = actionId.toString())

        assertThat(missionEnvAction).isNotNull
        assertThat(missionEnvAction?.getActionId()).isEqualTo(actionId.toString())
    }
}
