package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.GetMissionOperationalSummary2
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock2
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.*
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ThemeEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.EnvInfractionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntity2Mock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GetMissionOperationalSummary2Tests {

    val getMissionOperationalSummary = GetMissionOperationalSummary2()

    @Nested
    inner class FishActionsSummary {

        private fun getActions(actionType: MissionActionType): List<MissionFishActionEntity> {

            val action1 = MissionFishActionEntityMock.create(
                flagState = CountryCode.BE,
                fishActionType = actionType,
                logbookInfractions = listOf(
                    LogbookInfraction(infractionType = InfractionType.WITH_RECORD),
                    LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                    LogbookInfraction(infractionType = InfractionType.PENDING),
                ),
                speciesInfractions = listOf(
                    SpeciesInfraction(infractionType = InfractionType.WITH_RECORD),
                    SpeciesInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                    SpeciesInfraction(infractionType = InfractionType.PENDING),
                ),
                gearInfractions = listOf(
                    GearInfraction(infractionType = InfractionType.WITH_RECORD),
                    GearInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                    GearInfraction(infractionType = InfractionType.PENDING),
                ),
                otherInfractions = listOf(
                    OtherInfraction(infractionType = InfractionType.WITH_RECORD),
                    OtherInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                    OtherInfraction(infractionType = InfractionType.PENDING),
                ),
                seizureAndDiversion = true,
                targets = listOf(
                    TargetMissionMock.create(
                        controls = listOf(
                            ControlMock.create(
                                controlType = ControlType.ADMINISTRATIVE,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            ),
                            ControlMock.create(
                                controlType = ControlType.GENS_DE_MER,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            ),
                            ControlMock.create(
                                controlType = ControlType.NAVIGATION,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            ),
                            ControlMock.create(
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
            )

            val action2 = MissionFishActionEntityMock.create(
                flagState = CountryCode.FR,
                fishActionType = actionType,
                logbookInfractions = listOf(
                    LogbookInfraction(infractionType = InfractionType.WITH_RECORD),
                    LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                    LogbookInfraction(infractionType = InfractionType.PENDING),
                ),
                targets = listOf(
                    TargetMissionMock.create(
                        controls = listOf(
                            ControlMock.create(
                                controlType = ControlType.NAVIGATION,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                )
                            ),

                        )
                    )
                )
            )

            val action3 = MissionFishActionEntityMock.create(
                flagState = CountryCode.AG, // Argentina, not EU
                fishActionType = actionType,
                logbookInfractions = listOf(
                    LogbookInfraction(infractionType = InfractionType.WITH_RECORD),
                    LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                    LogbookInfraction(infractionType = InfractionType.PENDING),
                ),
                seizureAndDiversion = true,
                targets = listOf(
                    TargetMissionMock.create(
                        controls = listOf(
                            ControlMock.create(
                                controlType = ControlType.NAVIGATION,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                )
                            ),

                            )
                    )
                )
            )

            val action4 = MissionFishActionEntityMock.create(
                flagState = CountryCode.GR, // Greece, other EU
                fishActionType = actionType,
                logbookInfractions = listOf(
                    LogbookInfraction(infractionType = InfractionType.WITH_RECORD),
                    LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                    LogbookInfraction(infractionType = InfractionType.PENDING),
                ),
                targets = listOf(
                    TargetMissionMock.create(
                        controls = listOf(
                            ControlMock.create(
                                controlType = ControlType.NAVIGATION,
                                infractions = listOf(
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
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
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "ES" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "BE" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "NL" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "IE" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "GB" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "autres ue" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "non ue" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            ),
            "total" to mapOf(
                "nbActions" to null,
                "nbControls" to null,
                "nbPvFish" to null,
                "nbPvSecuAndAdmin" to null,
                "nbPVGensDeMer" to null,
                "nbPvNav" to null,
                "nbSeizureAndDiversion" to null
            )
        )

        @Test
        fun `should return values equal to 0 because actions`() {
            val mission = MissionEntityMock2.create(actions = listOf())
            val summary = getMissionOperationalSummary.getProFishingSeaSummary(
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
            val mission = MissionEntityMock2.create(actions = fishActions)
            val summary = getMissionOperationalSummary.getProFishingSeaSummary(
                actions = mission.actions!!
            )

            assertEquals(summary, expectedEmpty)
        }

        @Test
        fun `getProFishingSeaSummary - should return 2 actions but other values to 0`() {
            val fishActions = listOf(
                MissionFishActionEntity.fromFishAction(action = FishActionControlMock.create()),
                MissionFishActionEntity.fromFishAction(action = FishActionControlMock.create(flagState = CountryCode.GR)),
//                MissionActionEntity.FishAction(
//                    ExtendedFishActionEntityMock.create(
//                        controlAction = ExtendedFishActionControlEntityMock.create(
//                            FishActionControlMock.create()
//                        )
//                    )
//                ),
//                MissionActionEntity.FishAction(
//                    ExtendedFishActionEntityMock.create(
//                        controlAction = ExtendedFishActionControlEntityMock.create(
//                            FishActionControlMock.create(
//                                flagState = CountryCode.GR
//                            )
//                        )
//                    )
//                ),
            )

            val mission = MissionEntityMock2.create(actions = fishActions)
            val summary = getMissionOperationalSummary.getProFishingSeaSummary(
                actions = mission.actions!!
            )
            val expected = linkedMapOf(
                "FR" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "ES" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "BE" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "NL" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "IE" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "GB" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "autres ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "non ue" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "total" to mapOf(
                    "nbActions" to 2,
                    "nbControls" to 2,
                    "nbPvFish" to null,
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
            val mission = MissionEntityMock2.create(actions = getActions(MissionActionType.SEA_CONTROL))
            val summary = getMissionOperationalSummary.getProFishingSeaSummary(
                actions = mission.actions!!
            )
            val expected = mapOf(
                "FR" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to 3,
                    "nbSeizureAndDiversion" to null
                ),
                "ES" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "BE" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 4,
                    "nbPvSecuAndAdmin" to 2,
                    "nbPVGensDeMer" to 1,
                    "nbPvNav" to 1,
                    "nbSeizureAndDiversion" to 1
                ),
                "NL" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "IE" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "GB" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to null,
                    "nbSeizureAndDiversion" to null
                ),
                "autres ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to 3,
                    "nbSeizureAndDiversion" to null
                ),
                "non ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null,
                    "nbPvNav" to 3,
                    "nbSeizureAndDiversion" to 1
                ),
                "total" to mapOf(
                    "nbActions" to 4,
                    "nbControls" to 4,
                    "nbPvFish" to 7,
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
            val mission = MissionEntityMock2.create(actions = getActions(MissionActionType.LAND_CONTROL))
            val summary = getMissionOperationalSummary.getProFishingLandSummary(
                actions = mission.actions!!
            )
            val expected = mapOf(
                "FR" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "ES" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "BE" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 4,
                    "nbPvSecuAndAdmin" to 2,
                    "nbPVGensDeMer" to 1
                ),
                "NL" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "IE" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "GB" to mapOf(
                    "nbActions" to null,
                    "nbControls" to null,
                    "nbPvFish" to null,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "autres ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "non ue" to mapOf(
                    "nbActions" to 1,
                    "nbControls" to 1,
                    "nbPvFish" to 1,
                    "nbPvSecuAndAdmin" to null,
                    "nbPVGensDeMer" to null
                ),
                "total" to mapOf(
                    "nbActions" to 4,
                    "nbControls" to 4,
                    "nbPvFish" to 7,
                    "nbPvSecuAndAdmin" to 2,
                    "nbPVGensDeMer" to 1
                )
            )

            assertEquals(summary, expected)
        }
    }

    @Nested
    inner class NavActionsSummary {

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
                        TargetMissionMock.create(
                            controls = listOf(
                                ControlMock.create(
                                    controlType = ControlType.ADMINISTRATIVE,
                                    infractions = listOf(
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                    )
                                ),
                                ControlMock.create(
                                    controlType = ControlType.GENS_DE_MER,
                                    infractions = listOf(
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                    )
                                ),
                                ControlMock.create(
                                    controlType = ControlType.NAVIGATION,
                                    infractions = listOf(
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WAITING),
                                    )
                                ),
                                ControlMock.create(
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
                        TargetMissionMock.create(
                            controls = listOf(
                                ControlMock.create(
                                    controlType = ControlType.NAVIGATION,
                                    infractions = listOf(
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                        InfractionEntity2Mock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    )
                                ),
                                ControlMock.create(
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
            val summary = getMissionOperationalSummary.getProSailingSeaSummary(
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
            val summary = getMissionOperationalSummary.getProSailingSeaSummary(
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
            val summary = getMissionOperationalSummary.getProSailingLandSummary(
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
            val summary = getMissionOperationalSummary.getLeisureSailingSeaSummary(
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
            val summary = getMissionOperationalSummary.getLeisureSailingLandSummary(
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

    @Nested
    inner class EnvActionsSummary {

        private val actions = listOf(
            // action control Peche de loisir with infraction
            MissionEnvActionEntityMock.create(
                themes = listOf(ThemeEntityMock.create(id = 112)),
                envInfractions = listOf(EnvInfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT)),
                targets = listOf(
                    TargetMissionMock.create(
                        controls = listOf(
                            ControlMock.create(
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
                themes = listOf(ThemeEntityMock.create(id = 112)),
                envInfractions = listOf(),
            ),


            // action control other theme with 2 infractions
            MissionEnvActionEntityMock.create(
                themes = listOf(ThemeEntityMock.create(id = 1)),
                envInfractions = listOf(EnvInfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT))
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
            val summary = getMissionOperationalSummary.getEnvSummary(
                actions = mission.actions!!
            )
            val expected = mapOf(
                "nbSurveillances" to 0,
                "nbControls" to 0,
                "nbPv" to 0,
            )
            assertEquals(summary, expected)
        }

        @Test
        fun `envActionSummary should return data`() {

            val mission =
                MissionEntityMock2.create(actions = actions)
            val summary = getMissionOperationalSummary.getEnvSummary(
                actions = mission.actions!!
            )
            val expected = mapOf(
                "nbSurveillances" to 2,
                "nbControls" to 3,
                "nbPv" to 3,
            )
            assertEquals(summary, expected)
        }

        @Test
        fun `getLeisureFishingSummary should return data`() {
            val summary = getMissionOperationalSummary.getLeisureFishingSummary(
                actions = actions
            )
            val expected = mapOf(
                "nbControls" to 2,
                "nbPv" to 3,
            )
            assertEquals(summary, expected)
        }
    }
}
