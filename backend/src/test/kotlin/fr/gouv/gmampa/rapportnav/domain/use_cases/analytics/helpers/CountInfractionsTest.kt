package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.helpers

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishInfraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers.CountInfractions
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlEntityMock
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
            fishInfractions = listOf(
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1),
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1),
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 2)
            )
        )

        val result = countInfractions.countFishInfractions(listOf(action1), InfractionType.WITH_RECORD)

        assertEquals(3, result["infractions"])
    }

    @Test
    fun `countOtherFishInfractions should count only matching infractionType`() {
        val action = MissionFishActionEntityMock.create(
            fishInfractions = listOf(
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1),
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 1),
                FishInfraction(infractionType = InfractionType.WITH_RECORD, natinf = 2)
            )
        )

        val result = countInfractions.countOtherFishInfractions(listOf(action), InfractionType.WITH_RECORD)
        assertEquals(3, result)
    }

    @Test
    fun `countNavInfractions should sum controls with correct type and infraction`() {
        val control1 = ControlEntityMock.create(
            controlType = ControlType.SECURITY,
            hasBeenDone = true,
            amountOfControls = 3,
            infractions = listOf(InfractionEntity(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITH_REPORT))
        )
        val control2 = ControlEntityMock.create(
            controlType = ControlType.NAVIGATION,
            hasBeenDone = true,
            amountOfControls = 1,
            infractions = listOf(InfractionEntity(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITHOUT_REPORT))
        )

        val target = TargetEntityMock.create(controls = listOf(control1, control2))
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
        val target1 = TargetEntityMock.create(
            source = MissionSourceEnum.MONITORENV,
            controls = listOf(
                ControlEntityMock.create(
                    hasBeenDone = true,
                    infractions = listOf(InfractionEntity(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITH_REPORT))
                )
            )
        )
        val target2 = TargetEntityMock.create(
            source = MissionSourceEnum.RAPPORTNAV,
            controls = listOf(
                ControlEntityMock.create(
                    hasBeenDone = true,
                    infractions = listOf(InfractionEntity(UUID.randomUUID(), infractionType = InfractionTypeEnum.WITH_REPORT))
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
