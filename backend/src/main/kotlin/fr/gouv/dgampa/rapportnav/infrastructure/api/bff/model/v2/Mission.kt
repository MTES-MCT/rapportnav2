package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo
import java.util.UUID

data class Mission(
    val id: Int? = null,
    val idUUID: String? = null,
    val status: MissionStatusEnum,
    val data: MissionData? = null,
    var actions: List<MissionAction?> = listOf(),
    val generalInfos: MissionGeneralInfo? = null,
    val isCompleteForStats: Boolean? = null,
    val completenessForStats: CompletenessForStatsEntity? = null,
) {

    fun toMissionEntity(): MissionEntity {
        return MissionEntity(
            id = id,
            idUUID = idUUID?.let { UUID.fromString(it) },
            data = MissionEnvEntity(
                missionTypes = data?.missionTypes,
                openBy = data?.openBy,
                completedBy = data?.completedBy,
                observationsCacem = null,
                observationsByUnit = data?.observationsByUnit,
                observationsCnsp = null,
                facade = null,
                geom = null,
                startDateTimeUtc = data?.startDateTimeUtc!!,
                endDateTimeUtc = data.endDateTimeUtc,
                envActions = listOf(),
                isDeleted = data.isDeleted,
                isGeometryComputedFromControls = false,
                missionSource = MissionSourceEnum.RAPPORT_NAV,
                hasMissionOrder = false,
                isUnderJdp = false
            ),
            actions = actions.map {
                val a = it as MissionNavAction
                NavActionEntity(
                    id = UUID.fromString(a.id),
                    missionId = a.missionId,
                    ownerId = it.ownerId?.let { UUID.fromString(it) },
                    actionType = it.actionType,
                    status = it.status,
                    sourcesOfMissingDataForStats = it.sourcesOfMissingDataForStats,
                    startDateTimeUtc = it.data.startDateTimeUtc,
                    endDateTimeUtc = it.data.endDateTimeUtc,
                    observations = it.data.observations,
                    latitude = it.data.latitude,
                    longitude = it.data.longitude,
                    detectedPollution = it.data.detectedPollution,
                    pollutionObservedByAuthorizedAgent = it.data.pollutionObservedByAuthorizedAgent,
                    diversionCarriedOut = it.data.diversionCarriedOut,
                    isSimpleBrewingOperationDone = it.data.isSimpleBrewingOperationDone,
                    isAntiPolDeviceDeployed = it.data.isAntiPolDeviceDeployed,
                    controlMethod = it.data.controlMethod,
                    vesselIdentifier = it.data.vesselIdentifier,
                    vesselType = it.data.vesselType,
                    vesselSize = it.data.vesselSize,
                    identityControlledPerson = it.data.identityControlledPerson,
                    nbOfInterceptedVessels = it.data.nbOfInterceptedVessels,
                    nbOfInterceptedMigrants = it.data.nbOfInterceptedMigrants,
                    nbOfSuspectedSmugglers = it.data.nbOfSuspectedSmugglers,
                    isVesselRescue = it.data.isVesselRescue,
                    isPersonRescue = it.data.isPersonRescue,
                    isVesselNoticed = it.data.isVesselNoticed,
                    isVesselTowed = it.data.isVesselTowed,
                    isInSRRorFollowedByCROSSMRCC = it.data.isInSRRorFollowedByCROSSMRCC,
                    numberPersonsRescued = it.data.numberPersonsRescued,
                    numberOfDeaths = it.data.numberOfDeaths,
                    operationFollowsDEFREP = it.data.operationFollowsDEFREP,
                    locationDescription = it.data.locationDescription,
                    isMigrationRescue = it.data.isMigrationRescue,
                    nbOfVesselsTrackedWithoutIntervention = it.data.nbOfVesselsTrackedWithoutIntervention,
                    nbAssistedVesselsReturningToShore = it.data.nbAssistedVesselsReturningToShore,
                    //status = if (it.data?.actionType == ActionType.STATUS) it.data?.status else null, // status only set for ActionType Status
                    reason = it.data.reason,
                    nbrOfHours = it.data.nbrOfHours,
                    trainingType = it.data.trainingType,
                    unitManagementTrainingType = it.data.unitManagementTrainingType,
                    isWithinDepartment = it.data.isWithinDepartment,
                    hasDivingDuringOperation = it.data.hasDivingDuringOperation,
                    incidentDuringOperation = it.data.incidentDuringOperation,
                    resourceId = it.data.resourceId,
                    resourceType = it.data.resourceType,
                    nbrOfControl = it.data.nbrOfControl,
                    sectorType = it.data.sectorType,
                    nbrOfControlAmp = it.data.nbrOfControlAmp,
                    nbrOfControl300m = it.data.nbrOfControl300m,
                    isControlDuringSecurityDay = it.data.isControlDuringSecurityDay,
                    isSeizureSleepingFishingGear = it.data.isSeizureSleepingFishingGear,
                    sectorEstablishmentType = it.data.sectorEstablishmentType,
                    leisureType = it.data.leisureType,
                    fishingGearType = it.data.fishingGearType,
                    controlType = it.data.controlType,
                    securityVisitType = it.data.securityVisitType,
                    nbrSecurityVisit = it.data.nbrSecurityVisit,
                    establishment = it.data.establishment?.toEstablishmentEntity(),
                    targets = it.data.targets?.map { it.toTargetEntity() }
                )
            },
            generalInfos = MissionGeneralInfoEntity(
                services = generalInfos?.services,
                crew = generalInfos?.crew?.map {
                    it.toMissionCrewEntity(
                        missionId = id,
                        missionIdUUID = idUUID?.let { UUID.fromString(it) }
                    )
                },
                data = generalInfos?.toGeneralInfoEntity(
                    missionId = id,
                    missionIdUUID = idUUID?.let { UUID.fromString(it) })
            )
        )
    }

    companion object {
        fun fromMissionEntity(mission: MissionEntity): Mission {
            val completenessForStats = mission.isCompleteForStats()
            val status = mission.calculateMissionStatus(
                endDateTimeUtc = mission.data?.endDateTimeUtc,
                startDateTimeUtc = mission.data?.startDateTimeUtc!!
            )
            return Mission(
                id = mission.id,
                status = status,
                idUUID = mission.idUUID?.toString(),
                completenessForStats = completenessForStats,
                data = MissionData.fromMissionEntity(mission.data),
                isCompleteForStats = completenessForStats.sources?.isEmpty(),
                generalInfos = MissionGeneralInfo.fromMissionGeneralInfoEntity(
                    generalInfo2 = mission.generalInfos,
                    isUnderJdp = mission.data.isUnderJdp
                ),
                actions = mission.actions?.map { action -> MissionAction.fromMissionActionEntity(action) } ?: listOf()
            )
        }
    }
}
