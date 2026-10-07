package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.Completion
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ComputeSati
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.sati.GetSatiById
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [GetSatiById::class])
@ContextConfiguration(classes = [GetSatiById::class])
class GetSatiByIdTest {

    @MockitoBean
    private lateinit var satiRepo: ISatiRepository

    @MockitoBean
    private lateinit var getFishActionListByMissionId: GetFishActionListByMissionId

    @MockitoBean
    private lateinit var computeSati: ComputeSati

    private lateinit var useCase: GetSatiById

    @BeforeEach
    fun setUp() {
        useCase = GetSatiById(satiRepo, getFishActionListByMissionId, computeSati)
    }

    @Test
    fun `execute should return null when sati is not found`() {
        val id = UUID.randomUUID()
        whenever(satiRepo.findById(id)).thenReturn(null)

        val result = useCase.execute(id)

        assertThat(result).isNull()
        verifyNoInteractions(getFishActionListByMissionId)
        verifyNoInteractions(computeSati)
    }

    @Test
    fun `execute should throw BackendInternalException when sati has no missionId`() {
        val id = UUID.randomUUID()
        val sati = createSati(id = id, missionId = null)
        whenever(satiRepo.findById(id)).thenReturn(sati)

        assertThrows<BackendInternalException> {
            useCase.execute(id)
        }

        verifyNoInteractions(getFishActionListByMissionId)
        verifyNoInteractions(computeSati)
    }

    @Test
    fun `execute should throw BackendInternalException when no FishAction matches the sati actionId`() {
        val id = UUID.randomUUID()
        val missionId = 42
        val sati = createSati(id = id, missionId = missionId, actionId = "761")
        val otherAction = createAction(id = 999, missionId = missionId)
        whenever(satiRepo.findById(id)).thenReturn(sati)
        whenever(getFishActionListByMissionId.execute(missionId)).thenReturn(listOf(otherAction))

        assertThrows<BackendInternalException> {
            useCase.execute(id)
        }

        verifyNoInteractions(computeSati)
    }

    @Test
    fun `execute should compute sati with the matching FishAction`() {
        val id = UUID.randomUUID()
        val missionId = 42
        val sati = createSati(id = id, missionId = missionId, actionId = "761")
        val action = createAction(id = 761, missionId = missionId)
        val computed = sati.copy(startDatetimeUtc = action.actionDatetimeUtc)
        whenever(satiRepo.findById(id)).thenReturn(sati)
        whenever(getFishActionListByMissionId.execute(missionId)).thenReturn(listOf(action))
        whenever(computeSati.execute(sati, action)).thenReturn(computed)

        val result = useCase.execute(id)

        assertThat(result).isEqualTo(computed)
    }

    private fun createSati(
        id: UUID,
        missionId: Int?,
        actionId: String = "761"
    ): SatiEntity {
        return SatiEntity(
            id = id,
            module = SatiModuleType.M1,
            actionId = actionId,
            missionId = missionId
        )
    }

    private fun createAction(id: Int, missionId: Int): MissionAction {
        return MissionAction(
            id = id,
            missionId = missionId,
            actionType = MissionActionType.SEA_CONTROL,
            actionDatetimeUtc = Instant.parse("2026-03-24T10:15:30Z"),
            userTrigram = "ABC",
            isFromPoseidon = false,
            hasSomeGearsSeized = false,
            hasSomeSpeciesSeized = false,
            completion = Completion.TO_COMPLETE
        )
    }
}
