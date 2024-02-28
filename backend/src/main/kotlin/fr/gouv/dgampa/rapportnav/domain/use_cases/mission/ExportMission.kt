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

            val presenceMer = mutableMapOf(
                "navigationEffective" to (durations.find { it.status == ActionStatusType.NAVIGATING }?.value ?: 0),
                "mouillage" to (durations.find { it.status === ActionStatusType.ANCHORED }?.value ?: 0),
                "total" to 0
            )

            val totalPresenceMer = presenceMer.values.sum()
            presenceMer["total"] = totalPresenceMer


            val presenceQuai = mutableMapOf(
                "maintenance" to (durations.find { it.reason === ActionStatusReason.MAINTENANCE }?.value ?: 0),
                "meteo" to (durations.find { it.reason === ActionStatusReason.WEATHER }?.value ?: 0),
                "representation" to (durations.find { it.reason === ActionStatusReason.REPRESENTATION }?.value ?: 0),
                "adminFormation" to (durations.find { it.reason === ActionStatusReason.ADMINISTRATION }?.value ?: 0),
                "autre" to (durations.find { it.reason === ActionStatusReason.OTHER }?.value ?: 0),
                "contrPol" to (durations.find { it.reason === ActionStatusReason.HARBOUR_CONTROL }?.value ?: 0),
                "total" to 0
            )

            val totalQuai = presenceQuai.values.sum()
            presenceQuai["total"] = totalQuai

            val indisponibilite = mutableMapOf(
                "technique" to (durations.find { it.reason === ActionStatusReason.TECHNICAL }?.value ?: 0),
                "personnel" to (durations.find { it.reason === ActionStatusReason.PERSONNEL }?.value ?: 0),
                "total" to 78
            )

            val totalIndisponibilite = indisponibilite.values.sum()
            indisponibilite["total"] = totalIndisponibilite

            val dureeMission = totalPresenceMer + totalQuai + totalIndisponibilite



            if (generalInfo != null) {
                exportRepository.exportOdt(
                    service = mission.openBy,
                    id = "pam" + mission.id,
                    startDateTime = mission.startDateTimeUtc,
                    endDateTime = mission.endDateTimeUtc,
                    presenceMer = presenceMer,
                    presenceQuai = presenceQuai,
                    indisponibilite = indisponibilite,
                    nbJoursMer = 4,
                    dureeMission = dureeMission,
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
