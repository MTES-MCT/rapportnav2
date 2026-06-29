package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.Completion
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetComputeSati::class])
@ContextConfiguration(classes = [GetComputeSati::class])
class GetComputeSatiTest {

    @MockitoBean
    private lateinit var enableSati: EnableSati

    @MockitoBean
    private lateinit var satiRepo: ISatiRepository

    @MockitoBean
    private lateinit var controlResourceRepo: IEnvControlUnitResourceRepository

    @Test
    fun `execute should throw IllegalArgumentException when action id is null`() {
        whenever(enableSati.execute()).thenReturn(true)
        val useCase = GetComputeSati(enableSati, satiRepo, controlResourceRepo)
        val action = createAction(id = null, actionType = MissionActionType.AIR_CONTROL)
        assertThrows(IllegalArgumentException::class.java) {
            useCase.execute(action)
        }

        verifyNoInteractions(satiRepo)
    }

    @Test
    fun `execute should return null when action type is not a control`() {
        whenever(enableSati.execute()).thenReturn(true)
        val useCase = GetComputeSati(enableSati, satiRepo, controlResourceRepo)
        val action = createAction(id = 761, actionType = MissionActionType.AIR_SURVEILLANCE)

        val result = useCase.execute(action)

        assertThat(result).isNull()
        verifyNoInteractions(satiRepo)
    }

    @Test
    fun `execute should merge db sati with action for control actions`() {
        whenever(enableSati.execute()).thenReturn(true)
        val actionId = 761
        val useCase = GetComputeSati(enableSati, satiRepo, controlResourceRepo)
        val action = createAction(id = 761, actionType = MissionActionType.AIR_CONTROL)

        val dbSati = createSatiEntity(actionId = actionId)
        whenever(satiRepo.findByActionId(actionId.toString())).thenReturn(dbSati)

        val result = useCase.execute(action)

        assertThat(result).isNotNull
        assertThat(result?.actionId).isEqualTo(actionId.toString())
        assertThat(result?.module).isEqualTo(dbSati.module)
    }

    private fun createAction(
        id: Int? = null,
        actionType: MissionActionType
    ): MissionAction {
        return MissionAction(
            id = id,
            missionId = 123,
            actionType = actionType,
            actionDatetimeUtc = Instant.parse("2026-03-24T10:15:30Z"),
            actionEndDatetimeUtc = Instant.parse("2026-03-24T11:15:30Z"),
            observationsByUnit = "Test observations",
            userTrigram = "Test user trigram",
            isFromPoseidon = false,
            hasSomeGearsSeized = false,
            hasSomeSpeciesSeized = false,
            completion = Completion.TO_COMPLETE
        )
    }

    private fun createSatiEntity(
        actionId: Int
    ): SatiEntity {
        return SatiEntity(
            id = UUID.randomUUID(),
            module = SatiModuleType.M1,
            actionId = actionId.toString(),
            startDatetimeUtc = Instant.parse("2026-03-24T09:15:30Z")
        )
    }
}
