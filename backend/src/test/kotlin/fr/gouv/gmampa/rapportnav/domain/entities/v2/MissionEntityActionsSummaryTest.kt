package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MissionEntityActionsSummaryTest {

    @Test
    fun `computeActionsSummary returns empty list when there are no actions`() {
        assertTrue(MissionEntity(actions = listOf()).computeActionsSummary().isEmpty())
        assertTrue(MissionEntity(actions = null).computeActionsSummary().isEmpty())
    }

    @Test
    fun `computeActionsSummary classifies RapportNav controls, surveillances and autres (INQUIRY counts as control)`() {
        val mission = MissionEntity(
            actions = listOf(
                MissionNavActionEntityMock.create(actionType = ActionType.CONTROL),
                MissionNavActionEntityMock.create(actionType = ActionType.CONTROL_SECTOR),
                MissionNavActionEntityMock.create(actionType = ActionType.INQUIRY), // control
                MissionNavActionEntityMock.create(actionType = ActionType.MARITIME_SURVEILLANCE),
                MissionNavActionEntityMock.create(actionType = ActionType.NAUTICAL_EVENT),
                MissionNavActionEntityMock.create(actionType = ActionType.NOTE), // autre
                MissionNavActionEntityMock.create(actionType = ActionType.RESCUE), // autre
            )
        )

        val summary = mission.computeActionsSummary().single { it.source == MissionSourceEnum.RAPPORT_NAV }

        assertEquals(3, summary.nbControls)
        assertEquals(2, summary.nbSurveillances)
        assertEquals(2, summary.nbOtherActions)
    }

    @Test
    fun `computeActionsSummary classifies RapportNav SURVEILLANCE and FISHING_SURVEILLANCE as autres (not surveillances)`() {
        // Only NAUTICAL_EVENT / LAND_SURVEILLANCE / MARITIME_SURVEILLANCE count as ULAM surveillances.
        val mission = MissionEntity(
            actions = listOf(
                MissionNavActionEntityMock.create(actionType = ActionType.SURVEILLANCE),
                MissionNavActionEntityMock.create(actionType = ActionType.FISHING_SURVEILLANCE),
                MissionNavActionEntityMock.create(actionType = ActionType.LAND_SURVEILLANCE), // real surveillance
            )
        )

        val summary = mission.computeActionsSummary().single { it.source == MissionSourceEnum.RAPPORT_NAV }

        assertEquals(0, summary.nbControls)
        assertEquals(1, summary.nbSurveillances)
        assertEquals(2, summary.nbOtherActions)
    }

    @Test
    fun `computeActionsSummary counts MonitorEnv controls and surveillances (1 per action, NOTE is autre)`() {
        val mission = MissionEntity(
            actions = listOf(
                // actionNumberOfControls is intentionally ignored: this still counts as 1 control
                MissionEnvActionEntityMock.create(envActionType = ActionTypeEnum.CONTROL, actionNumberOfControls = 5),
                MissionEnvActionEntityMock.create(envActionType = ActionTypeEnum.SURVEILLANCE),
                MissionEnvActionEntityMock.create(envActionType = ActionTypeEnum.SURVEILLANCE),
                MissionEnvActionEntityMock.create(envActionType = ActionTypeEnum.NOTE),
            )
        )

        val summary = mission.computeActionsSummary().single { it.source == MissionSourceEnum.MONITORENV }

        assertEquals(1, summary.nbControls)
        assertEquals(2, summary.nbSurveillances)
        assertEquals(1, summary.nbOtherActions)
    }

    @Test
    fun `computeActionsSummary counts MonitorFish controls by fishActionType`() {
        val mission = MissionEntity(
            actions = listOf(
                MissionFishActionEntityMock.create(fishActionType = MissionActionType.SEA_CONTROL),
                MissionFishActionEntityMock.create(fishActionType = MissionActionType.LAND_CONTROL),
                MissionFishActionEntityMock.create(fishActionType = MissionActionType.AIR_CONTROL),
                MissionFishActionEntityMock.create(fishActionType = MissionActionType.AIR_SURVEILLANCE),
                MissionFishActionEntityMock.create(fishActionType = MissionActionType.OBSERVATION),
            )
        )

        val summary = mission.computeActionsSummary().single { it.source == MissionSourceEnum.MONITORFISH }

        assertEquals(3, summary.nbControls)
        assertEquals(1, summary.nbSurveillances)
        assertEquals(1, summary.nbOtherActions)
    }

    @Test
    fun `computeActionsSummary emits one entry per source with at least one action`() {
        val mission = MissionEntity(
            actions = listOf(
                MissionNavActionEntityMock.create(actionType = ActionType.CONTROL),
                MissionFishActionEntityMock.create(fishActionType = MissionActionType.SEA_CONTROL),
            )
        )

        val result = mission.computeActionsSummary()

        assertEquals(2, result.size)
        assertTrue(result.any { it.source == MissionSourceEnum.RAPPORT_NAV })
        assertTrue(result.any { it.source == MissionSourceEnum.MONITORFISH })
        assertNull(result.firstOrNull { it.source == MissionSourceEnum.MONITORENV })
    }
}
