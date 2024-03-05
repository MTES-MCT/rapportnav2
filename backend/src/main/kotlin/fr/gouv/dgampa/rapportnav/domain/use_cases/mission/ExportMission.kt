package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations

@UseCase
class ExportMission(
    private val exportRepository: IRpnExportRepository,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val agentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMissionById: GetMissionById,
    private val navActionStatus: INavActionStatusRepository,
    private val getStatusDurations: GetStatusDurations,
) {

    private inline fun List<GetStatusDurations.ActionStatusWithDuration>.findDuration(predicate: (GetStatusDurations.ActionStatusWithDuration) -> Boolean): Int {
        return find(predicate)?.value?.toInt() ?: 0
    }

    fun exportOdt(missionId: Int) {

        val mission: MissionEntity? = getMissionById.execute(missionId = missionId)

        if (mission == null) {
            return
        } else {
            val generalInfo: MissionGeneralInfoEntity? = getMissionGeneralInfoByMissionId.execute(missionId)
            val agentsCrew: List<MissionCrewEntity> = agentsCrewByMissionId.execute(missionId = missionId)
            val statuses = navActionStatus.findAllByMissionId(missionId = missionId).sortedBy { it.startDateTimeUtc }
                .map { it.toActionStatusEntity() }


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

            val missionDuration =
                (atSeaDurations["total"] ?: 0) + (dockingDurations["total"] ?: 0) + (unavailabilityDurations["total"]
                    ?: 0)


            if (generalInfo != null) {
                exportRepository.exportOdt(
                    service = mission.openBy,
                    id = "pam" + mission.id,
                    startDateTime = mission.startDateTimeUtc,
                    endDateTime = mission.endDateTimeUtc,
                    presenceMer = atSeaDurations,
                    presenceQuai = dockingDurations,
                    indisponibilite = unavailabilityDurations,
                    nbJoursMer = 4,
                    dureeMission = missionDuration,
                    patrouilleEnv = 2,
                    patrouilleMigrant = 4,
                    distanceMilles = generalInfo.distanceInNauticalMiles,
                    goMarine = generalInfo.consumedGOInLiters,
                    essence = generalInfo.consumedFuelInLiters,
                    crew = agentsCrew,
                )
            }
        }

    }
}
