package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2

class CountInfractions {

    fun countFishInfractions(actions: List<MissionFishActionEntity>, infractionType: InfractionType): Map<String, Int> {
        return mapOf(
            "nbLogbookInfractions" to actions.sumOf { action ->
                action.logbookInfractions?.count { it.infractionType == infractionType }?.or(0) ?: 0
            },
            "nbGearInfractions" to actions.sumOf { action ->
                action.gearInfractions?.count { it.infractionType == infractionType }?.or(0) ?: 0
            },
            "nbSpeciesInfractions" to actions.sumOf { action ->
                action.speciesInfractions?.count { it.infractionType == infractionType }?.or(0) ?: 0
            }
        )
    }

    fun countOtherFishInfractions(
        actions: List<MissionFishActionEntity>,
        infractionType: InfractionType
    ): Int =
        actions.sumOf { action ->
            action.otherInfractions
                ?.count { it.infractionType == infractionType }
                ?: 0
        }

    fun countNavInfractions(
        actions: List<MissionActionEntity>,
        controlType: ControlType,
        infractionType: InfractionTypeEnum?
    ): Int =
        actions.asSequence()
            .flatMap { it.targets.orEmpty() }
            .flatMap { it.controls.orEmpty() }
            .filter { control ->
                control.controlType == controlType &&
                        control.hasBeenDone == true &&
                        control.infractions.orEmpty().any { it.infractionType == infractionType }
            }
            .sumOf { it.amountOfControls }

    fun countEnvInfractions(
        actions: List<MissionEnvActionEntity>,
        infractionType: InfractionTypeEnum?
    ): Int =
        actions.asSequence()
            .flatMap { it.targets.orEmpty() }
            .filter { it.source == MissionSourceEnum.MONITORENV }
            .flatMap { it.controls.orEmpty() }
            .filter { it.hasBeenDone == true }
            .flatMap { it.infractions.orEmpty() }
            .count { it.infractionType == infractionType }
}
