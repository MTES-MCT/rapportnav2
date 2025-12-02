package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary

import fr.gouv.dgampa.rapportnav.domain.entities.analytics.ThemeStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary.ComputeEnvOperationalSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock2
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntity2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.*
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ThemeEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.EnvInfractionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntity2Mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [ComputeEnvOperationalSummary::class, ComputeDurations::class])
class ComputeEnvOperationalSummaryTest {

    @Autowired
    private lateinit var computeEnvOperationalSummary: ComputeEnvOperationalSummary


        private val actions = listOf(
            // action control Peche de loisir with infraction
            MissionEnvActionEntityMock.create(
                actionNumberOfControls = 3,
                themes = listOf(ThemeEntityMock.create(id = 112)),
                envInfractions = listOf(EnvInfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT)),
                targets = listOf(
                    TargetEntity2Mock.create(
                        controls = listOf(
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

            // action control Peche de loisir without infraction
            MissionEnvActionEntityMock.create(
                actionNumberOfControls = 3,
                themes = listOf(ThemeEntityMock.create(id = 112)),
                envInfractions = listOf(),
            ),


            // action control other theme with 2 infractions
            MissionEnvActionEntityMock.create(
                themes = listOf(ThemeEntityMock.create(id = 1)),
                envInfractions = listOf(EnvInfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT, natinf = listOf("123")))
            ),
            // action surveillance peche loisir
            MissionEnvActionEntityMock.create(
                envActionType = ActionTypeEnum.SURVEILLANCE,
                themes = listOf(ThemeEntityMock.create(id = 112)),
            ),
            // action surveillance other theme
            MissionEnvActionEntityMock.create(
                envActionType = ActionTypeEnum.SURVEILLANCE,
                themes = listOf(ThemeEntityMock.create(id = 1)),
            ),
        )

        @Test
        fun `envActionSummary should return empty map`() {
            val mission =
                MissionEntityMock2.create(actions = listOf())
            val summary = computeEnvOperationalSummary.execute(
                actions = mission.actions!!
            )
            val expected = mapOf(
                "nbSurveillances" to 0,
                "totalSurveillanceDurationInHours" to 0,
                "nbControls" to 0,
                "nbInfractionsWithRecord" to 0,
                "nbInfractionsWithoutRecord" to 0,
                "controlThemes" to listOf<ThemeStats>(),
                "surveillanceThemes" to listOf<ThemeStats>()
            )
            assertEquals(summary, expected)
        }

        @Test
        fun `envActionSummary should return data`() {

            val mission =
                MissionEntityMock2.create(actions = actions)
            val summary = computeEnvOperationalSummary.execute(
                actions = mission.actions!!
            )
            val expected = mapOf(
                "nbSurveillances" to 2,
                "totalSurveillanceDurationInHours" to 2,
                "nbControls" to 3,
                "nbInfractionsWithRecord" to 1,
                "nbInfractionsWithoutRecord" to 1,
                "controlThemes" to listOf<ThemeStats>(
                    ThemeStats(id=1, theme="Pêche loisir (autre que PAP)", nbActions=1, durationInHours=1.0),
                    ThemeStats(id=112, theme="Pêche loisir (autre que PAP)", nbActions=2, durationInHours=2.0),
                    ThemeStats(id=113,theme="Peche embarquée", nbActions=3, durationInHours=3.0)
                ),
                "surveillanceThemes" to listOf<ThemeStats>(
                    ThemeStats(id=1, theme="Pêche loisir (autre que PAP)", nbActions=1, durationInHours=1.0),
                    ThemeStats(id=112, theme="Pêche loisir (autre que PAP)", nbActions=1, durationInHours=1.0),
                    ThemeStats(id=113, theme="Peche embarquée", nbActions=2, durationInHours=2.0)
                )
            )
            assertEquals(summary, expected)
        }


}
