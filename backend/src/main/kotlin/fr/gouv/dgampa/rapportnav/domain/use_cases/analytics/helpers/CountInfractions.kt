package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.FishActionEntity

class CountInfractions {

    fun countFishInfractions(actions: List<FishActionEntity>, infractionType: InfractionType): Map<String, Int> {
        return mapOf(
            "infractions" to actions.sumOf { action ->
                action.fishInfractions?.count { it.infractionType == infractionType }?.or(0) ?: 0
            },
        )
    }

    fun countOtherFishInfractions(
        actions: List<FishActionEntity>,
        infractionType: InfractionType
    ): Int =
        actions.sumOf { action ->
            action.fishInfractions
                ?.count { it.infractionType == infractionType }
                ?: 0
        }

    fun countNavInfractions(
        actions: List<ActionEntity>,
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
        actions: List<EnvActionEntity>,
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
