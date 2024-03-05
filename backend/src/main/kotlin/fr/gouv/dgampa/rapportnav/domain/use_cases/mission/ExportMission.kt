package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GroupActionByDate
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import java.time.LocalDate

@UseCase
class ExportMission(
    private val exportRepository: IRpnExportRepository,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val agentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMissionById: GetMissionById,
    private val navActionStatus: INavActionStatusRepository,
    private val getStatusDurations: GetStatusDurations,
    private val groupActionByDate: GroupActionByDate,
) {

    private inline fun List<GetStatusDurations.ActionStatusWithDuration>.findDuration(predicate: (GetStatusDurations.ActionStatusWithDuration) -> Boolean): Int {
        return find(predicate)?.value?.toInt() ?: 0
    }

    fun computeDurations(mission: MissionEntity, statuses: List<ActionStatusEntity>): Map<String, Map<String, Int>> {
        val durations = getStatusDurations.computeActionDurations(
            missionStartDateTime = mission.startDateTimeUtc,
            missionEndDateTime = mission.endDateTimeUtc,
            actions = statuses,
        )

        val atSeaDurations = mapOf(
            "navigationEffective" to durations.findDuration { it.status == ActionStatusType.NAVIGATING },
            "mouillage" to durations.findDuration { it.status == ActionStatusType.ANCHORED },
            "total" to 0
        ).toMutableMap()
        atSeaDurations["total"] = atSeaDurations.values.sum()

        val dockingDurations = mapOf(
            "maintenance" to durations.findDuration { it.reason == ActionStatusReason.MAINTENANCE },
            "meteo" to durations.findDuration { it.reason == ActionStatusReason.WEATHER },
            "representation" to durations.findDuration { it.reason == ActionStatusReason.REPRESENTATION },
            "adminFormation" to durations.findDuration { it.reason == ActionStatusReason.ADMINISTRATION },
            "autre" to durations.findDuration { it.reason == ActionStatusReason.OTHER },
            "contrPol" to durations.findDuration { it.reason == ActionStatusReason.HARBOUR_CONTROL },
            "total" to 0
        ).toMutableMap()
        dockingDurations["total"] = dockingDurations.values.sum()

        val unavailabilityDurations = mapOf(
            "technique" to durations.findDuration { it.reason == ActionStatusReason.TECHNICAL },
            "personnel" to durations.findDuration { it.reason == ActionStatusReason.PERSONNEL },
            "total" to 0
        ).toMutableMap()
        unavailabilityDurations["total"] = unavailabilityDurations.values.sum()

        return mapOf(
            "atSeaDurations" to atSeaDurations.toMap(),
            "dockingDurations" to dockingDurations.toMap(),
            "unavailabilityDurations" to unavailabilityDurations.toMap()
        )
    }


    fun formatEnvControlForTimeline(action: EnvActionControlEntity): String {
        // Code to format EnvActionControlEntity
        return "EnvControlForTimeline"
    }

    fun formatFishControlForTimeline(action: MissionAction): String {
        // Code to format MissionActionEntity (Fish)
        return "FishControlForTimeline"
    }

    fun formatNavNoteForTimeline(action: NavActionEntity): String {
        // Code to format NavActionEntity with ActionType.NOTE
        return "NavNoteForTimeline"
    }

    fun formatNavStatusForTimeline(action: NavActionEntity): String {
        // Code to format NavActionEntity with ActionType.STATUS
        return "NavStatusForTimeline"
    }

    fun formatNavControlForTimeline(action: NavActionEntity): String {
        // Code to format NavActionEntity with ActionType.CONTROL
        return "NavControlForTimeline"
    }

    fun formatActionsForTimeline(actions: List<MissionActionEntity>?): Map<LocalDate, List<String>>? {

        if (actions.isNullOrEmpty()) {
            return null
        }
        // Group actions by date
        val groupedActions = groupActionByDate.execute(actions = actions)

        // Map each group to list of formatted strings
        val formattedActions = groupedActions?.mapValues { (_, actionsOnDate) ->
            actionsOnDate.mapNotNull { action ->
                when (action) {
                    is MissionActionEntity.EnvAction -> {
                        if (action.envAction?.controlAction?.action is EnvActionControlEntity) {
                            formatEnvControlForTimeline(action.envAction.controlAction.action)
                        } else null
                    }

                    is MissionActionEntity.FishAction -> {
                        if (action.fishAction.controlAction?.action is MissionAction) {
                            formatFishControlForTimeline(action.fishAction.controlAction.action)
                        } else null
                    }

                    is MissionActionEntity.NavAction -> {
                        val navAction: NavActionEntity = action.navAction
                        when (navAction.actionType) {
                            ActionType.NOTE -> formatNavNoteForTimeline(navAction)
                            ActionType.STATUS -> formatNavStatusForTimeline(navAction)
                            ActionType.CONTROL -> formatNavControlForTimeline(navAction)
                            else -> null
                        }
                    }
                }
            }
        }

        return formattedActions
    }

    fun exportOdt(missionId: Int): MissionExportEntity? {

        val mission: MissionEntity? = getMissionById.execute(missionId = missionId)

        if (mission == null) {
            return null
        } else {
            val generalInfo: MissionGeneralInfoEntity? = getMissionGeneralInfoByMissionId.execute(missionId)
            val agentsCrew: List<MissionCrewEntity> = agentsCrewByMissionId.execute(missionId = missionId)
            val statuses = navActionStatus.findAllByMissionId(missionId = missionId).sortedBy { it.startDateTimeUtc }
                .map { it.toActionStatusEntity() }


            val durations = computeDurations(mission, statuses)

            // TODO quelle formule utiliser? celle ci ou plutot missionEnd - missionStart ?
            val missionDuration = (durations["atSeaDurations"]?.get("total") ?: 0) +
                (durations["dockingDurations"]?.get("total") ?: 0) +
                (durations["unavailabilityDurations"]?.get("total") ?: 0)

            val timeline = formatActionsForTimeline(mission.actions)

            return exportRepository.exportOdt(
                service = mission.openBy,
                id = "pam" + mission.id,
                startDateTime = mission.startDateTimeUtc,
                endDateTime = mission.endDateTimeUtc,
                presenceMer = durations["atSeaDurations"].orEmpty(),
                presenceQuai = durations["dockingDurations"].orEmpty(),
                indisponibilite = durations["unavailabilityDurations"].orEmpty(),
                nbJoursMer = 0,
                dureeMission = missionDuration,
                patrouilleEnv = 0,
                patrouilleMigrant = 0,
                distanceMilles = generalInfo?.distanceInNauticalMiles,
                goMarine = generalInfo?.consumedGOInLiters,
                essence = generalInfo?.consumedFuelInLiters,
                crew = agentsCrew,
                timeline = timeline
            )
        }

    }
}
