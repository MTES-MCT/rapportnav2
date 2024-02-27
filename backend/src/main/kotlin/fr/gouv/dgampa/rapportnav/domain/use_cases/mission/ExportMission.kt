package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
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
    private val getEnvMissionById: GetEnvMissionById,
    private val navActionStatus: INavActionStatusRepository,
    private val getStatusDurations: GetStatusDurations,
) {

    fun exportOdt(missionId: Int) {

        val mission: ExtendedEnvMissionEntity? = getEnvMissionById.execute(missionId = missionId)

        if (mission?.mission == null) {
            return
        }
        else {
            val generalInfo: MissionGeneralInfoEntity? = getMissionGeneralInfoByMissionId.execute(missionId)
            val agentsCrew: List<MissionCrewEntity> = agentsCrewByMissionId.execute(missionId = missionId)
            val statuses = navActionStatus.findAllByMissionId(missionId = missionId).sortedBy { it.startDateTimeUtc }
                .map { it.toActionStatusEntity() }

            val durations = getStatusDurations.computeActionDurations(
                missionStartDateTime = mission.mission.startDateTimeUtc,
                missionEndDateTime = mission.mission.endDateTimeUtc,
                actions = statuses,
            )

            val presenceMer = mapOf(
                "navigationEffective" to (durations.find {it.status == ActionStatusType.NAVIGATING}?.value ?: 0),
                "mouillage" to (durations.find { it.status === ActionStatusType.ANCHORED }?.value ?: 0),
                "total" to 226
            )

            val presenceQuai = mapOf(
                "maintenance" to (durations.find { it.reason === ActionStatusReason.MAINTENANCE }?.value ?: 0),
                "meteo" to (durations.find { it.reason === ActionStatusReason.WEATHER }?.value ?: 0),
                "representation" to (durations.find { it.reason === ActionStatusReason.REPRESENTATION }?.value ?: 0),
                "adminFormation" to (durations.find { it.reason === ActionStatusReason.ADMINISTRATION }?.value ?: 0),
                "autre" to (durations.find { it.reason === ActionStatusReason.OTHER }?.value ?: 0),
                "contrPol" to (durations.find { it.reason === ActionStatusReason.HARBOUR_CONTROL }?.value ?: 0),
                "total" to 25
            )

            val indisponibilite = mapOf(
                "technique" to (durations.find { it.reason === ActionStatusReason.TECHNICAL }?.value ?: 0),
                "personnel" to (durations.find { it.reason === ActionStatusReason.PERSONNEL }?.value ?: 0),
                "total" to 78
            )



            if (generalInfo != null) {
                exportRepository.exportOdt(
                    service = mission.mission.openBy,
                    id = "pam" + mission.mission.id,
                    startDateTime = mission.mission.startDateTimeUtc,
                    endDateTime = mission.mission.endDateTimeUtc,
                    presenceMer = presenceMer,
                    presenceQuai = presenceQuai,
                    indisponibilite = indisponibilite,
                    nbJoursMer = 4,
                    dureeMission = 3,
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
