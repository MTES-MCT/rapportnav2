package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.input.AdminGeneralInfosUpdateInput

@UseCase
class UpdateGeneralInfoAdmin(
    private val repository: IMissionGeneralInfoRepository,
    private val getServiceById: GetServiceById,

) {
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
        return MissionGeneralInfoEntity.fromMissionGeneralInfoModel(saved)
    }
}
