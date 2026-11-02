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
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
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
        val action = createAction(id = 761, actionType = MissionActionType.SEA_CONTROL)

        val dbSati = createSatiEntity(actionId = actionId)
        whenever(satiRepo.findByActionId(actionId.toString())).thenReturn(dbSati)

        val result = useCase.execute(action)

        assertThat(result).isNotNull
        assertThat(result?.actionId).isEqualTo(actionId.toString())
        assertThat(result?.module).isEqualTo(dbSati.module)
    }

    @Test
    fun `execute should return null for AIR_CONTROL fish action`() {
        whenever(enableSati.execute()).thenReturn(true)
        val useCase = GetComputeSati(enableSati, satiRepo, controlResourceRepo)

        val result = useCase.execute(createAction(id = 761, actionType = MissionActionType.AIR_CONTROL))

        assertThat(result).isNull()
        verifyNoInteractions(satiRepo)
    }

    @Test
    fun `execute nav should return null when sati is disabled`() {
        whenever(enableSati.execute()).thenReturn(false)
        val useCase = GetComputeSati(enableSati, satiRepo, controlResourceRepo)

        assertThat(useCase.execute(createNavAction(ActionType.CONTROL_ROADSIDE))).isNull()
        verifyNoInteractions(satiRepo)
    }

    @Test
    fun `execute nav should return null for a non eligible nav action type`() {
        whenever(enableSati.execute()).thenReturn(true)
        val useCase = GetComputeSati(enableSati, satiRepo, controlResourceRepo)

        assertThat(useCase.execute(createNavAction(ActionType.CONTROL))).isNull()
        verifyNoInteractions(satiRepo)
    }

    @Test
    fun `execute nav should create sati with M4 for CONTROL_SECTOR_FISHING`() {
        whenever(enableSati.execute()).thenReturn(true)
        val useCase = GetComputeSati(enableSati, satiRepo, controlResourceRepo)
        val action = createNavAction(ActionType.CONTROL_SECTOR_FISHING)
        whenever(satiRepo.findByActionId(action.getActionId())).thenReturn(null)
        whenever(satiRepo.save(any())).thenAnswer { it.arguments[0] as SatiEntity }

        val result = useCase.execute(action)

        assertThat(result?.module).isEqualTo(SatiModuleType.M4)
        assertThat(result?.establishment).isNotNull
        assertThat(result?.actionId).isEqualTo(action.getActionId())
        assertThat(result?.startDatetimeUtc).isEqualTo(action.startDateTimeUtc)
        assertThat(result?.endDatetimeUtc).isEqualTo(action.endDateTimeUtc)
    }

    @Test
    fun `execute nav should reuse existing sati with M5 for CONTROL_ROADSIDE`() {
        whenever(enableSati.execute()).thenReturn(true)
        val useCase = GetComputeSati(enableSati, satiRepo, controlResourceRepo)
        val action = createNavAction(ActionType.CONTROL_ROADSIDE)
        val existing = SatiEntity(id = UUID.randomUUID(), module = SatiModuleType.M5, actionId = action.getActionId())
        whenever(satiRepo.findByActionId(action.getActionId())).thenReturn(existing)

        val result = useCase.execute(action)

        assertThat(result?.id).isEqualTo(existing.id)
        assertThat(result?.module).isEqualTo(SatiModuleType.M5)
        verify(satiRepo, never()).save(any())
    }

    @Test
    fun `execute nav should create no establishment for a module without one`() {
        whenever(enableSati.execute()).thenReturn(true)
        val useCase = GetComputeSati(enableSati, satiRepo, controlResourceRepo)
        val action = createNavAction(ActionType.CONTROL_ROADSIDE)
        whenever(satiRepo.findByActionId(action.getActionId())).thenReturn(null)
        whenever(satiRepo.save(any())).thenAnswer { it.arguments[0] as SatiEntity }

        val result = useCase.execute(action)

        assertThat(SatiModuleType.M5.hasEstablishment()).isFalse()
        assertThat(result?.establishment).isNull()
    }

    @Test
    fun `hasEstablishment should only be true for M4 for now`() {
        assertThat(SatiModuleType.entries.filter { it.hasEstablishment() }).containsExactly(SatiModuleType.M4)
    }

    private fun createNavAction(actionType: ActionType) = MissionNavActionEntity(
        id = UUID.randomUUID(),
        missionId = 761,
        actionType = actionType,
        startDateTimeUtc = Instant.parse("2026-03-24T10:15:30Z"),
        endDateTimeUtc = Instant.parse("2026-03-24T11:15:30Z")
    )

    private fun createAction(
        id: Int? = null,
        actionType: MissionActionType
    ): MissionAction {
        return MissionAction(
            id = id,
            missionId = 761,
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
