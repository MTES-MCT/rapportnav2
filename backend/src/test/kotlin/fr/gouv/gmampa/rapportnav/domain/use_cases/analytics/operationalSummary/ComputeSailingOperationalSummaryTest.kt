package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.operationalSummary

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.operationalSummary.ComputeSailingOperationalSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock2
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.*
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntity2Mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ComputeSailingOperationalSummary::class, ComputeDurations::class])
class ComputeSailingOperationalSummaryTest {

    @Autowired
    private lateinit var computeSailingOperationalSummary: ComputeSailingOperationalSummary

    fun getAction(controlMethod: ControlMethod, vesselType: VesselTypeEnum): List<MissionNavActionEntity> {
        val actions = listOf(
            MissionNavActionEntityMock.create(actionType = ActionType.STATUS),
            MissionNavActionEntityMock.create(
                actionType = ActionType.CONTROL,
                vesselType = VesselTypeEnum.COMMERCIAL,
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.CONTROL,
                vesselType = vesselType,
                controlMethod = controlMethod,
                targets = listOf(
                    TargetEntity2Mock.create(
                        controls = listOf(
                            ControlEntity2Mock.create(
                                controlType = ControlType.ADMINISTRATIVE,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            ),
                            ControlEntity2Mock.create(
                                controlType = ControlType.GENS_DE_MER,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            ),
                            ControlEntity2Mock.create(
                                controlType = ControlType.NAVIGATION,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            ),
                            ControlEntity2Mock.create(
                                controlType = ControlType.SECURITY,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            )

                        )
                    )
                )
            ),

            MissionNavActionEntityMock.create(
                actionType = ActionType.CONTROL,
                vesselType = vesselType,
                controlMethod = controlMethod,
                targets = listOf(
                    TargetEntity2Mock.create(
                        controls = listOf(
                            ControlEntity2Mock.create(
                                controlType = ControlType.NAVIGATION,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                )
                            ),
                            ControlEntity2Mock.create(
                                controlType = ControlType.SECURITY,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                )
                            )

                        )
                    )
                )
            ),

            MissionNavActionEntityMock.create(
                actionType = ActionType.CONTROL,
                vesselType = vesselType,
                controlMethod = controlMethod
            ),

            MissionNavActionEntityMock.create(
                actionType = ActionType.CONTROL,
                vesselType = vesselType,
                controlMethod = ControlMethod.AIR
            ),
        )
        return actions
    }


    @Test
    fun `getProSailingSeaSummary should return values equal to null because actions`() {
        val mission = MissionEntityMock2.create(actions = listOf())
        val summary = computeSailingOperationalSummary.getProSailingSeaSummary(
            actions = mission.actions!!
        )
        val expected = mapOf(
            "nbActions" to 0,
            "nbPvSecuAndAdmin" to 0,
            "nbPVGensDeMer" to 0,
            "nbPvNav" to 0,
        )
        assertEquals(summary, expected)
    }

    @Test
    fun `getProSailingSeaSummary should return all data`() {
        val mission = MissionEntityMock2.create(actions = getAction(ControlMethod.SEA, VesselTypeEnum.SAILING))
        val summary = computeSailingOperationalSummary.getProSailingSeaSummary(
            actions = mission.actions!!
        )
        val expected = mapOf(
            "nbActions" to 3,
            "nbPvSecuAndAdmin" to 5,
            "nbPVGensDeMer" to 1,
            "nbPvNav" to 4,
        )
        assertEquals(summary, expected)
    }

    @Test
    fun `getProSailingLandSummary should return all data`() {
        val mission = MissionEntityMock2.create(actions = getAction(ControlMethod.LAND, VesselTypeEnum.SAILING))
        val summary = computeSailingOperationalSummary.getProSailingLandSummary(
            actions = mission.actions!!
        )
        val expected = mapOf(
            "nbActions" to 3,
            "nbPvSecuAndAdmin" to 5,
            "nbPVGensDeMer" to 1,
        )
        assertEquals(summary, expected)
    }

    @Test
    fun `getLeisureSailingSeaSummary should return all data`() {
        val mission =
            MissionEntityMock2.create(actions = getAction(ControlMethod.SEA, VesselTypeEnum.SAILING_LEISURE))
        val summary = computeSailingOperationalSummary.getLeisureSailingSeaSummary(
            actions = mission.actions!!
        )
        val expected = mapOf(
            "nbActions" to 3,
            "nbPvSecu" to 4,
            "nbPVAdmin" to 1,
            "nbPvNav" to 4,
        )
        assertEquals(summary, expected)
    }

    @Test
    fun `getLeisureSailingLandSummary should return all data`() {
        val mission =
            MissionEntityMock2.create(actions = getAction(ControlMethod.LAND, VesselTypeEnum.SAILING_LEISURE))
        val summary = computeSailingOperationalSummary.getLeisureSailingLandSummary(
            actions = mission.actions!!
        )
        val expected = mapOf(
            "nbActions" to 3,
            "nbPVAdmin" to 1,
            "nbPvSecu" to 4,
        )
        assertEquals(summary, expected)
    }
}
