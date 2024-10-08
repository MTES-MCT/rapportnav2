package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.GetMissionOperationalSummary
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.*
import fr.gouv.gmampa.rapportnav.mocks.mission.control.ControlAdministrativeEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.control.ControlGensDeMerEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.control.ControlNavigationEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.control.ControlSecurityEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.infraction.InfractionEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GetMissionOperationalSummaryTests {

    val getMissionOperationalSummary = GetMissionOperationalSummary()

    @Nested
    inner class FishActionsSummary {

        private fun getActions(actionType: MissionActionType): List<MissionActionEntity.FishAction> {
            return listOf(
                MissionActionEntity.FishAction(
                    ExtendedFishActionEntityMock.create(
                        controlAction = ExtendedFishActionControlEntityMock.create(
                            action = FishActionControlMock.create(
                                flagState = CountryCode.BE,
                                actionType = actionType,
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

                                ),
                            controlAdministrative = ControlAdministrativeEntityMock.create(
                                infractions = listOf(
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            ),
                            controlGensDeMer = ControlGensDeMerEntityMock.create(
                                infractions = listOf(
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            ),
                            controlNavigation = ControlNavigationEntityMock.create(
                                infractions = listOf(
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            ),
                            controlSecurity = ControlSecurityEntityMock.create(
                                infractions = listOf(
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                                )
                            )
                        ),
                    )
                ),
                MissionActionEntity.FishAction(
                    ExtendedFishActionEntityMock.create(
                        controlAction = ExtendedFishActionControlEntityMock.create(
                            action = FishActionControlMock.create(
                                actionType = actionType,
                                logbookInfractions = listOf(
                                    LogbookInfraction(infractionType = InfractionType.WITH_RECORD),
                                    LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                                    LogbookInfraction(infractionType = InfractionType.PENDING),
                                ),
                            ),
                            controlNavigation = ControlNavigationEntityMock.create(
                                infractions = listOf(
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                )
                            ),
                        ),
                    )
                ),
                MissionActionEntity.FishAction(
                    ExtendedFishActionEntityMock.create(
                        controlAction = ExtendedFishActionControlEntityMock.create(
                            action = FishActionControlMock.create(
                                flagState = CountryCode.AG, // Argentina, not EU
                                actionType = actionType,
                                logbookInfractions = listOf(
                                    LogbookInfraction(infractionType = InfractionType.WITH_RECORD),
                                    LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                                    LogbookInfraction(infractionType = InfractionType.PENDING),
                                ),
                                seizureAndDiversion = true,
                            ),
                            controlNavigation = ControlNavigationEntityMock.create(
                                infractions = listOf(
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                )
                            ),
                        ),
                    )
                ),
                MissionActionEntity.FishAction(
                    ExtendedFishActionEntityMock.create(
                        controlAction = ExtendedFishActionControlEntityMock.create(
                            action = FishActionControlMock.create(
                                flagState = CountryCode.GR, // Greece, other EU
                                actionType = actionType,
                                logbookInfractions = listOf(
                                    LogbookInfraction(infractionType = InfractionType.WITH_RECORD),
                                    LogbookInfraction(infractionType = InfractionType.WITHOUT_RECORD),
                                    LogbookInfraction(infractionType = InfractionType.PENDING),
                                ),
                            ),
                            controlNavigation = ControlNavigationEntityMock.create(
                                infractions = listOf(
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                    InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                )
                            ),
                        ),
                    )
                ),
            )
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
            val mission = MissionEntityMock.create(actions = null)
            val summary = getMissionOperationalSummary.getProFishingSeaSummary(
                mission = mission
            )
            assertEquals(summary, expectedEmpty)
        }

        @Test
        fun `should return values equal to 0 because no control action`() {
            val fishActions = listOf(
                MissionActionEntity.FishAction(ExtendedFishActionEntityMock.create()),
                MissionActionEntity.FishAction(ExtendedFishActionEntityMock.create()),
            )
            val mission = MissionEntityMock.create(actions = fishActions)
            val summary = getMissionOperationalSummary.getProFishingSeaSummary(
                mission = mission
            )

            assertEquals(summary, expectedEmpty)
        }

        @Test
        fun `getProFishingSeaSummary - should return 2 actions but other values to 0`() {
            val fishActions = listOf(
                MissionActionEntity.FishAction(
                    ExtendedFishActionEntityMock.create(
                        controlAction = ExtendedFishActionControlEntityMock.create(
                            FishActionControlMock.create()
                        )
                    )
                ),
                MissionActionEntity.FishAction(
                    ExtendedFishActionEntityMock.create(
                        controlAction = ExtendedFishActionControlEntityMock.create(
                            FishActionControlMock.create(
                                flagState = CountryCode.GR
                            )
                        )
                    )
                ),
            )

            val mission = MissionEntityMock.create(actions = fishActions)
            val summary = getMissionOperationalSummary.getProFishingSeaSummary(
                mission = mission
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
            val mission = MissionEntityMock.create(actions = getActions(MissionActionType.SEA_CONTROL))
            val summary = getMissionOperationalSummary.getProFishingSeaSummary(
                mission = mission
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
            val mission = MissionEntityMock.create(actions = getActions(MissionActionType.LAND_CONTROL))
            val summary = getMissionOperationalSummary.getProFishingLandSummary(
                mission = mission
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

        fun getAction(controlMethod: ControlMethod, vesselType: VesselTypeEnum): List<MissionActionEntity.NavAction> {
            val actions = listOf(
                MissionActionEntity.NavAction(
                    navAction = NavActionStatusMock.create().toNavActionEntity()
                ),
                MissionActionEntity.NavAction(
                    navAction = NavActionControlMock.create(vesselType = VesselTypeEnum.COMMERCIAL).toNavActionEntity()
                ),
                MissionActionEntity.NavAction(
                    navAction = NavActionControlMock.create(
                        vesselType = vesselType,
                        controlMethod = controlMethod,
                        controlAdministrative = ControlAdministrativeEntityMock.create(
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                            )
                        ),
                        controlGensDeMer = ControlGensDeMerEntityMock.create(
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                            )
                        ),
                        controlNavigation = ControlNavigationEntityMock.create(
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                            )
                        ),
                        controlSecurity = ControlSecurityEntityMock.create(
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITHOUT_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WAITING),
                            )
                        )
                    ).toNavActionEntity()
                ),
                MissionActionEntity.NavAction(
                    navAction = NavActionControlMock.create(
                        vesselType = vesselType,
                        controlMethod = controlMethod,
                        controlNavigation = ControlNavigationEntityMock.create(
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                            )
                        ),
                        controlSecurity = ControlSecurityEntityMock.create(
                            infractions = listOf(
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                                InfractionEntityMock.create(infractionType = InfractionTypeEnum.WITH_REPORT),
                            )
                        )
                    ).toNavActionEntity()
                ),
                MissionActionEntity.NavAction(
                    navAction = NavActionControlMock.create(
                        vesselType = vesselType,
                        controlMethod = controlMethod
                    ).toNavActionEntity()
                ),
                MissionActionEntity.NavAction(
                    navAction = NavActionControlMock.create(
                        vesselType = vesselType,
                        controlMethod = ControlMethod.AIR
                    ).toNavActionEntity()
                ),
            )
            return actions
        }


        @Test
        fun `getProSailingSeaSummary should return values equal to null because actions`() {
            val mission = MissionEntityMock.create(actions = null)
            val summary = getMissionOperationalSummary.getProSailingSeaSummary(
                mission = mission
            )
            val expected = mapOf(
                "nbActions" to 0,
                "nbPvSecuAndAdmin" to 0,
                "nbPVGensDeMer" to 0,
                "nbPvNav" to 0,
            )


//            val expected = mapOf(
//                CountryCode.FR to mapOf(
//                    "nbActions" to 1,
//                    "nbControls" to 1,
//                    "nbPvFish" to 0,
//                    "nbPvSecuAndAdmin" to 0,
//                    "nbPVGensDeMer" to 0,
//                    "nbPvNav" to 0,
//                    "nbSeizureAndDiversion" to 0
//                ),
//                "autres ue" to mapOf(
//                    "nbActions" to 1,
//                    "nbControls" to 1,
//                    "nbPvFish" to null,
//                    "nbPvSecuAndAdmin" to null,
//                    "nbPVGensDeMer" to null,
//                    "nbPvNav" to null,
//                    "nbSeizureAndDiversion" to null
//                ),
//                "non ue" to mapOf(
//                    "nbActions" to null,
//                    "nbControls" to null,
//                    "nbPvFish" to null,
//                    "nbPvSecuAndAdmin" to null,
//                    "nbPVGensDeMer" to null,
//                    "nbPvNav" to null,
//                    "nbSeizureAndDiversion" to null
//                ),
//                "total" to mapOf(
//                    "nbActions" to 2,
//                    "nbControls" to 2,
//                    "nbPvFish" to null,
//                    "nbPvSecuAndAdmin" to null,
//                    "nbPVGensDeMer" to null,
//                    "nbPvNav" to null,
//                    "nbSeizureAndDiversion" to null
//                )
//            )


            assertEquals(summary, expected)
        }

        @Test
        fun `getProSailingSeaSummary should return all data`() {
            val mission = MissionEntityMock.create(actions = getAction(ControlMethod.SEA, VesselTypeEnum.SAILING))
            val summary = getMissionOperationalSummary.getProSailingSeaSummary(
                mission = mission
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
            val mission = MissionEntityMock.create(actions = getAction(ControlMethod.LAND, VesselTypeEnum.SAILING))
            val summary = getMissionOperationalSummary.getProSailingLandSummary(
                mission = mission
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
                MissionEntityMock.create(actions = getAction(ControlMethod.SEA, VesselTypeEnum.SAILING_LEISURE))
            val summary = getMissionOperationalSummary.getLeisureSailingSeaSummary(
                mission = mission
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
                MissionEntityMock.create(actions = getAction(ControlMethod.LAND, VesselTypeEnum.SAILING_LEISURE))
            val summary = getMissionOperationalSummary.getLeisureSailingLandSummary(
                mission = mission
            )
            val expected = mapOf(
                "nbActions" to 3,
                "nbPVAdmin" to 1,
                "nbPvSecu" to 4,
            )
            assertEquals(summary, expected)
        }
    }

}
