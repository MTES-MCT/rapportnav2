package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.helpers

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.GearInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.LogbookInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.OtherInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.SpeciesInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers.CountInfractions
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class CountInfractionsTest {

    private lateinit var countInfractions: CountInfractions

    @BeforeEach
    fun setup() {
        countInfractions = CountInfractions()
    }

    @Test
    fun `countFishInfractions should correctly count infractions by type`() {
        val action1 = MissionFishActionEntityMock.create(
            logbookInfractions = listOf(LogbookInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1)),
            gearInfractions = listOf(
                GearInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1),
                GearInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 2)
            ),
            speciesInfractions = listOf(),
        )

        val result = countInfractions.countFishInfractions(listOf(action1), InfractionType.WITH_RECORD)

        assertEquals(1, result["nbLogbookInfractions"])
        assertEquals(2, result["nbGearInfractions"])
        assertEquals(0, result["nbSpeciesInfractions"])
    }

    @Test
    fun `countOtherFishInfractions should count only matching infractionType`() {
        val action = MissionFishActionEntityMock.create(
            gearInfractions = listOf(GearInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1)),
            speciesInfractions = listOf(SpeciesInfraction(infractionType = InfractionType.WITHOUT_RECORD, natinf = 1)),
            otherInfractions = listOf(
                OtherInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1),
                OtherInfraction(infractionType = InfractionType.WITHOUT_RECORD, natinf = 1)
            ),
        )

        val result = countInfractions.countOtherFishInfractions(listOf(action), InfractionType.WITH_RECORD)
        assertEquals(1, result)
    }

    @Test
    fun `countNavInfractions should sum controls with correct type and infraction`() {
        val control1 = ControlMock.create(
            controlType = ControlType.SECURITY,
            hasBeenDone = true,
            amountOfControls = 3,
            infractions = listOf(InfractionEntity2(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITH_REPORT))
        )
        val control2 = ControlMock.create(
            controlType = ControlType.NAVIGATION,
            hasBeenDone = true,
            amountOfControls = 1,
            infractions = listOf(InfractionEntity2(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITHOUT_REPORT))
        )

        val target = TargetMissionMock.create(controls = listOf(control1, control2))
        val action = MissionNavActionEntityMock.create(targets = listOf(target))

        val result = countInfractions.countNavInfractions(
            actions = listOf(action),
            controlType = ControlType.SECURITY,
            infractionType = InfractionTypeEnum.WITH_REPORT
        )

        assertEquals(3, result)
    }

    @Test
    fun `countEnvInfractions should count 0 when no targets`() {
        val action = MissionEnvActionEntityMock.create(targets = listOf())

        val result = countInfractions.countEnvInfractions(
            listOf(action),
            InfractionTypeEnum.WITH_REPORT
        )

        assertEquals(0, result)
    }

    @Test
    fun `countEnvInfractions should count only MONITORENV sources and matching infraction type`() {
        val target1 = TargetMissionMock.create(
            source = MissionSourceEnum.MONITORENV,
            controls = listOf(
                ControlMock.create(
                    hasBeenDone = true,
                    infractions = listOf(InfractionEntity2(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITH_REPORT))
                )
            )
        )
        val target2 = TargetMissionMock.create(
            source = MissionSourceEnum.RAPPORTNAV,
            controls = listOf(
                ControlMock.create(
                    hasBeenDone = true,
                    infractions = listOf(InfractionEntity2(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITH_REPORT))
                )
            )
        )

        val action = MissionEnvActionEntityMock.create(targets = listOf(target1, target2))

        val result = countInfractions.countEnvInfractions(
            listOf(action),
            InfractionTypeEnum.WITH_REPORT
        )

        assertEquals(1, result)
    }
}
