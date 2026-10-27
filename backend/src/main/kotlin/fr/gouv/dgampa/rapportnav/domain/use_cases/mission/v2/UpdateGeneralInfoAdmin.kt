package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamResourceMetrics
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IResourceUsageRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetPamShipResource
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.input.AdminGeneralInfosUpdateInput
import org.slf4j.LoggerFactory

@UseCase
class UpdateGeneralInfoAdmin(
    private val repository: IMissionGeneralInfoRepository,
    private val getServiceById: GetServiceById,
    private val resourceUsageRepository: IResourceUsageRepository,
    private val getPamShipResource: GetPamShipResource,
    private val getMissionByExternalId: GetMissionByExternalId,
) {
    private val logger = LoggerFactory.getLogger(UpdateGeneralInfoAdmin::class.java)

    fun execute(generalInfo: AdminGeneralInfosUpdateInput): MissionGeneralInfoEntity? {
        val current: MissionGeneralInfoEntity? = if (generalInfo.missionId != null) {
            MissionGeneralInfoEntity.fromMissionGeneralInfoModel(repository.findByMissionId(generalInfo.missionId).get())
        }
        else if (generalInfo.missionIdUUID != null) {
            MissionGeneralInfoEntity.fromMissionGeneralInfoModel(repository.findByMissionIdUUID(generalInfo.missionIdUUID).get())
        }
        else null

        if (current == null) return null

        val service = getServiceById.execute(id = generalInfo.serviceId)

        val merged = current.copy(
            missionId = generalInfo.missionId ?: current.missionId,
            missionIdUUID = generalInfo.missionIdUUID ?: current.missionIdUUID,
            service = service ?: current.service,
            distanceInNauticalMiles = generalInfo.distanceInNauticalMiles ?: current.distanceInNauticalMiles,
            consumedGOInLiters = generalInfo.consumedGOInLiters ?: current.consumedGOInLiters,
            consumedFuelInLiters = generalInfo.consumedFuelInLiters ?: current.consumedFuelInLiters,
            operatingCostsInEuro = generalInfo.operatingCostsInEuro ?: current.operatingCostsInEuro,
            fuelCostsInEuro = generalInfo.fuelCostsInEuro ?: current.fuelCostsInEuro,
            nbrOfRecognizedVessel = generalInfo.nbrOfRecognizedVessel ?: current.nbrOfRecognizedVessel,
            isWithInterMinisterialService = generalInfo.isWithInterMinisterialService ?: current.isWithInterMinisterialService,
            isMissionArmed = generalInfo.isMissionArmed ?: current.isMissionArmed,
            missionReportType = generalInfo.missionReportType ?: current.missionReportType,
            reinforcementType = generalInfo.reinforcementType ?: current.reinforcementType,
            nbHourAtSea = generalInfo.nbHourAtSea ?: current.nbHourAtSea,
            jdpType = generalInfo.jdpType ?: current.jdpType,
            isResourcesNotUsed = generalInfo.isResourcesNotUsed ?: current.isResourcesNotUsed
        )

        val saved = repository.save(merged)
        mergePamMetricsOnShip(merged)
        return MissionGeneralInfoEntity.fromMissionGeneralInfoModel(saved)
    }

    /**
     * Mirrors the three PAM usage values (distance, GO, fuel) onto the ship's row of mission_resource_usage, touching only
     * those columns. The costs belong to the mission and stay in mission_general_info only.
     * Phase 1 (dual-write): mission_general_info stays the source of truth, so a failure here is logged and never
     * fails the admin save.
     */
    private fun mergePamMetricsOnShip(generalInfo: MissionGeneralInfoEntity) {
        try {
            // The service comes from our DB (getServiceById or the stored row), so its control units are trusted.
            if (generalInfo.service?.serviceType != ServiceTypeEnum.PAM) return
            val ship = getPamShipResource.execute(generalInfo.service)
            val missionIdUUID = generalInfo.missionIdUUID
                ?: generalInfo.missionId?.let { getMissionByExternalId.execute(it.toString())?.id }
            if (ship == null || missionIdUUID == null) {
                logger.warn(
                    "UpdateGeneralInfoAdmin: PAM metrics not written to mission_resource_usage for general info " +
                        "id=${generalInfo.id}: ship=${ship?.id}, missionIdUUID=$missionIdUUID"
                )
                return
            }
            resourceUsageRepository.mergePamMetrics(
                missionIdUUID = missionIdUUID,
                resourceId = ship.id,
                metrics = PamResourceMetrics.fromMissionGeneralInfoEntity(generalInfo)
            )
        } catch (e: Exception) {
            logger.warn("UpdateGeneralInfoAdmin: could not merge PAM metrics for general info id=${generalInfo.id}", e)
        }
    }
}
