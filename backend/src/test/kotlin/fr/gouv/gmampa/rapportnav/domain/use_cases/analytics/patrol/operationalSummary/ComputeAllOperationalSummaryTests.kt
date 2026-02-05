package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.OperationalSummaryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary.ComputeAllOperationalSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary.ComputeEnvOperationalSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary.ComputeFishingOperationalSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary.ComputeSailingOperationalSummary
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.*
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [
    ComputeAllOperationalSummary::class,
    ComputeEnvOperationalSummary::class,
    ComputeFishingOperationalSummary::class,
    ComputeSailingOperationalSummary::class,
])
class ComputeAllOperationalSummaryTests {

    @Autowired
    private lateinit var computeAllOperationalSummary: ComputeAllOperationalSummary

    @MockitoBean
    private lateinit var computeFishingOperationalSummary: ComputeFishingOperationalSummary
    @MockitoBean
    private lateinit var computeSailingOperationalSummary: ComputeSailingOperationalSummary
    @MockitoBean
    private lateinit var computeEnvOperationalSummary: ComputeEnvOperationalSummary


    private fun getActions(actionType: MissionActionType): List<MissionFishActionEntity> {

        val action1 = MissionFishActionEntityMock.create(
            flagState = CountryCode.BE,
            fishActionType = actionType,
            fishInfractions = listOf(
                FishInfraction(infractionType = InfractionType.WITH_RECORD),
                FishInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                FishInfraction(infractionType = InfractionType.PENDING),
                FishInfraction(infractionType = InfractionType.WITH_RECORD),
                FishInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                FishInfraction(infractionType = InfractionType.PENDING),
                FishInfraction(infractionType = InfractionType.WITH_RECORD),
                FishInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                FishInfraction(infractionType = InfractionType.PENDING),
                FishInfraction(infractionType = InfractionType.WITH_RECORD),
                FishInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                FishInfraction(infractionType = InfractionType.PENDING),
            ),
            seizureAndDiversion = true,
            targets = listOf(
                TargetEntityMock.create(
                    controls = listOf(
                        ControlEntityMock.create(
                            controlType = ControlType.ADMINISTRATIVE,
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                            )
                        ),
                        ControlEntityMock.create(
                            controlType = ControlType.GENS_DE_MER,
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                            )
                        ),
                        ControlEntityMock.create(
                            controlType = ControlType.NAVIGATION,
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                            )
                        ),
                        ControlEntityMock.create(
                            controlType = ControlType.SECURITY,
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                            )
                        )

                    )
                )
            )
        )

        val action2 = MissionFishActionEntityMock.create(
            flagState = CountryCode.FR,
            fishActionType = actionType,
            fishInfractions = listOf(
                FishInfraction(infractionType = InfractionType.WITH_RECORD),
                FishInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                FishInfraction(infractionType = InfractionType.PENDING),
            ),
            targets = listOf(
                TargetEntityMock.create(
                    controls = listOf(
                        ControlEntityMock.create(
                            controlType = ControlType.NAVIGATION,
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                            )
                        ),

                    )
                )
            )
        )

        val action3 = MissionFishActionEntityMock.create(
            flagState = CountryCode.AG, // Argentina, not EU
            fishActionType = actionType,
            fishInfractions = listOf(
                FishInfraction(infractionType = InfractionType.WITH_RECORD),
                FishInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                FishInfraction(infractionType = InfractionType.PENDING),
            ),
            seizureAndDiversion = true,
            targets = listOf(
                TargetEntityMock.create(
                    controls = listOf(
                        ControlEntityMock.create(
                            controlType = ControlType.NAVIGATION,
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                            )
                        ),

                        )
                )
            )
        )

        val action4 = MissionFishActionEntityMock.create(
            flagState = CountryCode.GR, // Greece, other EU
            fishActionType = actionType,
            fishInfractions = listOf(
                FishInfraction(infractionType = InfractionType.WITH_RECORD),
                FishInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                FishInfraction(infractionType = InfractionType.PENDING),
            ),
            targets = listOf(
                TargetEntityMock.create(
                    controls = listOf(
                        ControlEntityMock.create(
                            controlType = ControlType.NAVIGATION,
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                            )
                        ),

                        )
                )
            )
        )

        return listOf(action1, action2, action3, action4)
    }


    private val expectedEmpty = OperationalSummaryEntity(
        proFishingSeaSummary = LinkedHashMap(),
        proFishingLandSummary = LinkedHashMap(),
        proSailingSeaSummary = emptyMap(),
        proSailingLandSummary = emptyMap(),
        leisureSailingSeaSummary = emptyMap(),
        leisureSailingLandSummary = emptyMap(),
        leisureFishingSummary = emptyMap(),
        envSummary = emptyMap()
    )



    @Test
    fun `should return empty map because mission is null`() {
        val summary = computeAllOperationalSummary.execute(null)
        assertEquals(summary, expectedEmpty)
    }


    @Test
    fun `should return empty map because mission has no actions`() {
        val mission = MissionEntityMock.create(actions = listOf())
        val summary = computeAllOperationalSummary.execute(mission = mission)
        assertEquals(summary, expectedEmpty)
    }


    @Test
    fun `should return empty map`() {
        val mission = MissionEntityMock.create(actions = getActions(MissionActionType.SEA_CONTROL))
        val summary = computeAllOperationalSummary.execute(mission = mission)
        assertEquals(summary, expectedEmpty)
    }



}
