package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import com.neovisionaries.i18n.CountryCode
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.utils.FlagsUtils


class GetMissionOperationalSummary {

    private val fishingCountryCodes = listOf(
        CountryCode.FR, // France
        CountryCode.ES, // Spain (SP)
        CountryCode.BE, // Belgium (BL)
        CountryCode.NL, // Netherlands
        CountryCode.IE, // Ireland
        CountryCode.GB  // Great Britain
    )

    fun getProFishingSeaSummary(mission: MissionEntity): LinkedHashMap<String, Map<String, Int?>> {
        val summary = getFishActionSummary(mission = mission, controlMethod = MissionActionType.SEA_CONTROL)
        val keysToKeep = setOf(
            "nbActions",
            "nbControls",
            "nbPvFish",
            "nbPvSecuAndAdmin",
            "nbPVGensDeMer",
            "nbPvNav",
            "nbSeizureAndDiversion"
        )

        // Filter each country's data with the keysToKeep
        val filteredSummary: Map<CountryCode, Map<String, Int>> = summary.mapValues { (_, countrySummary) ->
            countrySummary.filterKeys { it in keysToKeep }
        }.toMap()

        // Sort by country code and backfill missing keys
        return aggregateByCountries(
            summary = filteredSummary,
            countries = fishingCountryCodes,
            keysToKeep = keysToKeep
        )
    }


    fun getProFishingLandSummary(mission: MissionEntity): LinkedHashMap<String, Map<String, Int?>> {
        val summary = getFishActionSummary(mission = mission, controlMethod = MissionActionType.LAND_CONTROL)
        val keysToKeep = setOf(
            "nbActions",
            "nbControls",
            "nbPvFish",
            "nbPvSecuAndAdmin",
            "nbPVGensDeMer"
        )

        // Filter each country's data with the keysToKeep
        val filteredSummary: Map<CountryCode, Map<String, Int>> = summary.mapValues { (_, countrySummary) ->
            countrySummary.filterKeys { it in keysToKeep }
        }.toMap()

        // Sort by country code and backfill missing keys
        return aggregateByCountries(
            summary = filteredSummary,
            countries = fishingCountryCodes,
            keysToKeep = keysToKeep
        )
    }


    fun getProSailingSeaSummary(mission: MissionEntity): Map<String, Int> {
        val summary = getNavActionSummary(
            mission = mission,
            controlMethod = ControlMethod.SEA,
            vesselType = VesselTypeEnum.SAILING
        )
        val keysToKeep = setOf("nbActions", "nbPvSecuAndAdmin", "nbPVGensDeMer", "nbPvNav")
        return summary.filterKeys { it in keysToKeep }
    }

    fun getProSailingLandSummary(mission: MissionEntity): Map<String, Int> {
        val summary = getNavActionSummary(
            mission = mission,
            controlMethod = ControlMethod.LAND,
            vesselType = VesselTypeEnum.SAILING
        )
        val keysToKeep = setOf("nbActions", "nbPvSecuAndAdmin", "nbPVGensDeMer")
        val filteredMap = summary.filterKeys { it in keysToKeep }
        return filteredMap
    }

    fun getLeisureSailingSeaSummary(mission: MissionEntity): Map<String, Int> {
        val summary = getNavActionSummary(
            mission = mission,
            controlMethod = ControlMethod.SEA,
            vesselType = VesselTypeEnum.SAILING_LEISURE
        )
        val keysToKeep = setOf("nbActions", "nbPvSecu", "nbPVAdmin", "nbPvNav")
        return summary.filterKeys { it in keysToKeep }
    }

    fun getLeisureSailingLandSummary(mission: MissionEntity): Map<String, Int> {
        val summary = getNavActionSummary(
            mission = mission,
            controlMethod = ControlMethod.LAND,
            vesselType = VesselTypeEnum.SAILING_LEISURE
        )
        val keysToKeep = setOf("nbActions", "nbPvSecu", "nbPVAdmin")
        return summary.filterKeys { it in keysToKeep }
    }

    fun getEnvSummary(mission: MissionEntity): Map<String, Int> {
        val actions = mission.actions
            ?.filterIsInstance<MissionActionEntity.EnvAction>()
            ?: emptyList()
        val nbSurveillances = actions.count { it.envAction?.surveillanceAction != null }
        val nbControls = actions.count { it.envAction?.controlAction != null }
        val nbPv = actions.sumOf { action ->
            // Sum infractions of type WITH_REPORT from different control categories
            (action.envAction?.controlAction?.controlAdministrative?.infractions?.count { inf ->
                inf.infractionType == InfractionTypeEnum.WITH_REPORT
            } ?: 0) +
                (action.envAction?.controlAction?.controlGensDeMer?.infractions?.count { inf ->
                    inf.infractionType == InfractionTypeEnum.WITH_REPORT
                } ?: 0) +
                (action.envAction?.controlAction?.controlSecurity?.infractions?.count { inf ->
                    inf.infractionType == InfractionTypeEnum.WITH_REPORT
                } ?: 0) +
                (action.envAction?.controlAction?.controlNavigation?.infractions?.count { inf ->
                    inf.infractionType == InfractionTypeEnum.WITH_REPORT
                } ?: 0) +
                (action.envAction?.controlAction?.action?.infractions?.count { inf ->
                    inf.infractionType == InfractionTypeEnum.WITH_REPORT
                } ?: 0)
        }
        val summary = mapOf(
            "nbSurveillances" to nbSurveillances,
            "nbControls" to nbControls,
            "nbPv" to nbPv
        )


        return summary
    }

    fun getLeisureFishingSummary(mission: MissionEntity): Map<String, Int> {
        // specifically filter for theme Peche de loisir (autre que PAP), it has id 112
        val themeId = 112
        val actions = mission.actions
            ?.filterIsInstance<MissionActionEntity.EnvAction>()
            ?.filter { it.envAction?.controlAction != null }
            ?.filter { it.envAction?.controlAction?.action?.controlPlans?.any { theme -> theme.themeId == themeId } == true }
            ?: emptyList()
        val nbPv = actions.sumOf { action ->
            // Sum infractions of type WITH_REPORT from different control categories
            (action.envAction?.controlAction?.action?.infractions?.count { inf ->
                inf.infractionType == InfractionTypeEnum.WITH_REPORT
            } ?: 0)
        }
        val summary = mapOf(
            "nbControls" to actions.count(),
            "nbPv" to nbPv
        )
        return summary
    }

    private fun getFishActionSummary(
        mission: MissionEntity,
        controlMethod: MissionActionType
    ): Map<CountryCode, Map<String, Int>> {
        val actions = mission.actions
            ?.filterIsInstance<MissionActionEntity.FishAction>()
            ?.mapNotNull { it.fishAction.controlAction }
            ?.filter { it.action?.actionType === controlMethod }
            ?: emptyList()

        return getFishSummary(actions)
    }

    private fun getFishSummary(actions: List<ExtendedFishActionControlEntity>): Map<CountryCode, Map<String, Int>> {
        // Group actions by country code (flagState)
        val actionsByCountry: Map<CountryCode, List<ExtendedFishActionControlEntity>> = actions
            .filter { it.action?.flagState != null } // Filter out null flagState actions
            .groupBy { it.action!!.flagState }

        // Create a summary for each country
        return actionsByCountry.mapValues { (_, countryActions) ->
            mapOf(
                // Nbre Navires contrôlés
                "nbActions" to countryActions.size,
                // Nbre contrôles pêche sanitaire
                "nbControls" to countryActions.size,
                // Nbre Pv pêche sanitaire
                "nbPvFish" to countWithRecordInfractions(countryActions.mapNotNull { it.action }).values.sum(),
                // Nbre PV équipmt sécu. permis nav.
                "nbPvSecuAndAdmin" to countryActions.sumOf { action ->
                    (action.controlSecurity?.infractions?.count { infraction ->
                        infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                    } ?: 0) +
                        (action.controlAdministrative?.infractions?.count { infraction ->
                            infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                        } ?: 0)
                },
                // Nb PV dans "Security"
                "nbPvSecu" to countryActions.sumOf { action ->
                    action.controlSecurity?.infractions?.count { infraction ->
                        infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                    } ?: 0
                },
                // Nb PV dans "Administrative"
                "nbPVAdmin" to countryActions.sumOf { action ->
                    action.controlAdministrative?.infractions?.count { infraction ->
                        infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                    } ?: 0
                },
                // Nb PV dans "Gens de mer"
                "nbPVGensDeMer" to countryActions.sumOf { action ->
                    action.controlGensDeMer?.infractions?.count { infraction ->
                        infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                    } ?: 0
                },
                // Nb PV dans "Police de la navigation"
                "nbPvNav" to countryActions.sumOf { action ->
                    action.controlNavigation?.infractions?.count { infraction ->
                        infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                    } ?: 0
                },
                // Nbre navires déroutés
                "nbSeizureAndDiversion" to countryActions.count { it.action?.seizureAndDiversion == true }
            )
        }
    }


    private fun countWithRecordInfractions(actions: List<MissionAction>): Map<String, Int> {
        return mapOf(
            "nbLogbookInfractions" to actions.sumOf { action ->
                action.logbookInfractions.count { it.infractionType == InfractionType.WITH_RECORD }
            },
            "nbGearInfractions" to actions.sumOf { action ->
                action.gearInfractions.count { it.infractionType == InfractionType.WITH_RECORD }
            },
            "nbSpeciesInfractions" to actions.sumOf { action ->
                action.speciesInfractions.count { it.infractionType == InfractionType.WITH_RECORD }
            },
            "nbOtherInfractions" to actions.sumOf { action ->
                action.otherInfractions.count { it.infractionType == InfractionType.WITH_RECORD }
            }
        )
    }

    fun aggregateByCountries(
        summary: Map<CountryCode, Map<String, Int>>, // Keep as Map for flexibility
        countries: List<CountryCode> = listOf(CountryCode.FR),
        keysToKeep: Set<String>
    ): LinkedHashMap<String, Map<String, Int?>> {
        // Prepare a map for other EU and other Non-EU countries
        val otherEu = mutableMapOf<String, Int?>() // Use nullable Int to allow nulls
        val otherNonEu = mutableMapOf<String, Int?>()

        // Initialize the maps with nulls for keysToKeep
        keysToKeep.forEach { key ->
            otherEu[key] = null
            otherNonEu[key] = null
        }

        // Calculate totals for other EU and other Non-EU
        summary.forEach { (country, data) ->
            // Check if the country is in the countries list
            if (!countries.contains(country)) {
                // Count totals for other EU countries
                if (FlagsUtils().isEuMember(country)) { // Replace with the actual condition to check EU membership
                    keysToKeep.forEach { key ->
                        otherEu[key] = (otherEu[key] ?: 0) + (data[key] ?: 0)
                    }
                } else {
                    // Count totals for other non-EU countries
                    keysToKeep.forEach { key ->
                        otherNonEu[key] = (otherNonEu[key] ?: 0) + (data[key] ?: 0)
                    }
                }
            }
        }

        // Create the final sorted map
        val sortedSummary = LinkedHashMap<String, Map<String, Int?>>()

        // Add specified countries to the final summary
        countries.forEach { country ->
            // Get the data for the country, if present, otherwise backfill with nulls
            val data = summary[country] ?: emptyMap()

            // Add the country data to sortedSummary, backfilling missing keys with nulls
            sortedSummary[country.toString()] = keysToKeep.associateWith { key ->
                if (data[key] != null && data[key]!! > 0) {
                    data[key]
                } else {
                    null
                }
            }
        }

        // Add "autres ue" and "non ue" rows with null backfilling
        sortedSummary["autres ue"] = otherEu.mapValues { if (it.value == 0) null else it.value }
        sortedSummary["non ue"] = otherNonEu.mapValues { if (it.value == 0) null else it.value }

        // Calculate total row and add it to the sortedSummary
        val totalRow = calculateTotalRow(summary, countries, otherEu, otherNonEu)
        sortedSummary["total"] = totalRow.mapValues { if (it.value == 0) null else it.value }

        return sortedSummary
    }


    fun calculateTotalRow(
        summary: Map<CountryCode, Map<String, Int?>>,
        countries: List<CountryCode>,
        otherEu: Map<String, Int?>,
        otherNonEu: Map<String, Int?>
    ): MutableMap<String, Int?> { // Change return type to nullable Int
        // Prepare a mutable map to hold total values
        val totalRow = mutableMapOf<String, Int?>() // Use nullable Int for totalRow

        // Combine specified countries and other groups for calculation
        val allGroups = countries + listOf("autres ue", "non ue")

        allGroups.forEach { group ->
            when (group) {
                in countries -> summary[group]?.let { data ->
                    data.forEach { (key, value) ->
                        totalRow[key] = (totalRow[key] ?: 0) + (value ?: 0) // Sum totals
                    }
                }

                "autres ue" -> otherEu.forEach { (key, value) ->
                    totalRow[key] = (totalRow[key] ?: 0) + (value ?: 0) // Sum totals
                }

                "non ue" -> otherNonEu.forEach { (key, value) ->
                    totalRow[key] = (totalRow[key] ?: 0) + (value ?: 0) // Sum totals
                }
            }
        }

        // Post-process to convert totals of 0 to null
        totalRow.replaceAll { _, value -> if (value == 0) null else value }

        return totalRow
    }


    private fun getNavActionSummary(
        mission: MissionEntity,
        vesselType: VesselTypeEnum,
        controlMethod: ControlMethod
    ): Map<String, Int> {
        val actions = mission.actions
            ?.filterIsInstance<MissionActionEntity.NavAction>()
            ?.mapNotNull { it.navAction.controlAction }
            ?.filter { it.vesselType == vesselType && it.controlMethod == controlMethod }
            ?: emptyList()

        return getNavSummary(actions)
    }

    private fun getNavSummary(actions: List<ActionControlEntity>): Map<String, Int> {
        return mapOf(
            // Nbre Navires contrôlés
            "nbActions" to actions.size,
            // Nbre PV équipmt sécu. permis nav.
            // Nb PV dans "équipement sécu" + "doc administratif navire" ajouté par unité dans Rap Nav
            "nbPvSecuAndAdmin" to actions.sumOf { action ->
                (action.controlSecurity?.infractions?.count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                } ?: 0) +
                    (action.controlAdministrative?.infractions?.count { infraction ->
                        infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                    } ?: 0)
            },
            // Nb PV dans "Administrative" ajouté par unité dans Rap Nav
            "nbPVAdmin" to actions.sumOf { action ->
                action.controlAdministrative?.infractions?.count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                } ?: 0
            },
            // Nb PV dans "Security" ajouté par unité dans Rap Nav
            "nbPvSecu" to actions.sumOf { action ->
                action.controlSecurity?.infractions?.count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                } ?: 0
            },
            // Nbre PV titre navig. rôle/déc. eff
            // Nb PV dans "Gens de mer" ajouté par unité dans Rap Nav
            "nbPVGensDeMer" to actions.sumOf { action ->
                action.controlGensDeMer?.infractions?.count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                } ?: 0
            },
            // Nbre PV police navig.
            // Nb PV dans "Police de la navigation" ajouté par unité dans Rap Nav
            "nbPvNav" to actions.sumOf { action ->
                action.controlNavigation?.infractions?.count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                } ?: 0
            },
        )
    }
}
