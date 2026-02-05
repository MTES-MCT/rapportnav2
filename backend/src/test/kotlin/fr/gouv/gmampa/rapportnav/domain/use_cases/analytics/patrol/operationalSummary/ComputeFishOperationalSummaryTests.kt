package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary.ComputeFishingOperationalSummary
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.*
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ThemeEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEnvEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ComputeFishOperationalSummaryTests {

    val computeFishingOperationalSummary = ComputeFishingOperationalSummary()

    @Nested
    inner class FishActionsSummary {

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

        private val expectedEmpty = mapOf(
            "FR" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbInfractionsWithoutPv" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "ES" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbInfractionsWithoutPv" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "BE" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbInfractionsWithoutPv" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "NL" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbInfractionsWithoutPv" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "IE" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbInfractionsWithoutPv" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "GB" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbInfractionsWithoutPv" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "autres ue" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbInfractionsWithoutPv" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "non ue" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbInfractionsWithoutPv" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "total" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbInfractionsWithoutPv" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            )
        )

        @Test
        fun `should return values equal to 0 because actions`() {
            val mission = MissionEntityMock.create(actions = listOf())
            val summary = computeFishingOperationalSummary.getProFishingSeaSummary(
                actions = mission.actions!!
            )
            assertEquals(summary, expectedEmpty)
        }

        @Test
        fun `should return values equal to 0 because no control action`() {
            val fishActions = listOf(
                MissionFishActionEntity.fromFishAction(action = FishActionControlMock.create(actionType = MissionActionType.LAND_CONTROL)),
                MissionFishActionEntity.fromFishAction(action = FishActionControlMock.create(actionType = MissionActionType.LAND_CONTROL)),
            )
            val mission = MissionEntityMock.create(actions = fishActions)
            val summary = computeFishingOperationalSummary.getProFishingSeaSummary(
                actions = mission.actions!!
            )

            assertEquals(summary, expectedEmpty)
        }

        @Test
        fun `getProFishingSeaSummary - should return 2 actions but other values to 0`() {
            val fishActions = listOf(
                MissionFishActionEntity.fromFishAction(action = FishActionControlMock.create()),
                MissionFishActionEntity.fromFishAction(action = FishActionControlMock.create(flagState = CountryCode.GR)),
            )

            val mission = MissionEntityMock.create(actions = fishActions)
            val summary = computeFishingOperationalSummary.getProFishingSeaSummary(
                actions = mission.actions!!
            )
            val expected = linkedMapOf(
                "FR" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "ES" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "BE" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "NL" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "IE" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "GB" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "autres ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "non ue" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "total" to mapOf(
                    "nbActions" to 2,
                    "nbControls" to 2,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                )
            )


            // Assert the expected result matches the actual summary
            assertEquals(expected, summary)

        }

        @Test
        fun `getProFishingSeaSummary - should return all data`() {
            val mission = MissionEntityMock.create(actions = getActions(MissionActionType.SEA_CONTROL))
            val summary = computeFishingOperationalSummary.getProFishingSeaSummary(
                actions = mission.actions!!
            )
            val expected = mapOf(
                "FR" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbInfractionsWithoutPv" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to 3,
                    "nbSeizureAndDiversion" to null
                ),
                "ES" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "BE" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 4,
                    "nbInfractionsWithoutPv" to 4,
                    "nbPvSecuAndAdmin" to 2,
                    "nbPVGensDeMer" to 1,
                    "nbPvNav" to 1,
                    "nbSeizureAndDiversion" to 1
                ),
                "NL" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "IE" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "GB" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "autres ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbInfractionsWithoutPv" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to 3,
                    "nbSeizureAndDiversion" to null
                ),
                "non ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbInfractionsWithoutPv" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to 3,
                    "nbSeizureAndDiversion" to 1
                ),
                "total" to mapOf(
                    "nbActions" to 4,
                    "nbControls" to 4,
                    "nbPvFish" to 7,
                    "nbInfractionsWithoutPv" to 7,
                    "nbPvSecuAndAdmin" to 2,
                    "nbPVGensDeMer" to 1,
                    "nbPvNav" to 10,
                    "nbSeizureAndDiversion" to 2
                )
            )

            assertEquals(expected, summary)

        }

        @Test
        fun `getProFishingLandSummary - should return all data`() {
            val mission = MissionEntityMock.create(actions = getActions(MissionActionType.LAND_CONTROL))
            val summary = computeFishingOperationalSummary.getProFishingLandSummary(
                actions = mission.actions!!
            )
            val expected = mapOf(
                "FR" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbInfractionsWithoutPv" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "ES" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "BE" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 4,
                    "nbInfractionsWithoutPv" to 4,
                    "nbPvSecuAndAdmin" to 2,
                    "nbPVGensDeMer" to 1
                ),
                "NL" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "IE" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "GB" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbInfractionsWithoutPv" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "autres ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbInfractionsWithoutPv" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "non ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbInfractionsWithoutPv" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "total" to mapOf(
                    "nbActions" to 4,
                    "nbControls" to 4,
                    "nbPvFish" to 7,
                    "nbInfractionsWithoutPv" to 7,
                    "nbPvSecuAndAdmin" to 2,
                    "nbPVGensDeMer" to 1
                )
            )

            assertEquals(summary, expected)
        }
    }



    @Nested
    inner class EnvActionsSummary {

        private val actions = listOf(
            // action control Peche de loisir with infraction
            MissionEnvActionEntityMock.create(
                actionNumberOfControls = 3,
                themes = listOf(ThemeEntityMock.create(id = 112)),
                envInfractions = listOf(InfractionEnvEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT)),
                targets = listOf(
                    TargetEntityMock.create(
                        controls = listOf(
                            ControlEntityMock.create(
                                controlType = ControlType.SECURITY,
                                infractions = listOf(
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
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
                envInfractions = listOf(InfractionEnvEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT))
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
        fun `getLeisureFishingSummary should return data`() {
            val summary = computeFishingOperationalSummary.getLeisureFishingSummary(
                actions = actions
            )
            val expected = mapOf(
                "nbControls" to 6,
                "nbPv" to 1,
            )
            assertEquals(summary, expected)
        }
    }
}
