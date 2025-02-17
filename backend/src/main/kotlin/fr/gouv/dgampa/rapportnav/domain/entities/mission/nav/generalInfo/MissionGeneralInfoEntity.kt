package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.generalInfo.MissionGeneralInfo
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

data class MissionGeneralInfoEntity(
    var id: Int,
    var missionId: Int,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
    var serviceId: Int? = null,
    @MandatoryForStats
    var nbrOfRecognizedVessel: Int? = null,
    var isWithInterMinisterialService: Boolean? = false,
    var isAllAgentsParticipating: Boolean? = false,
    var isMissionArmed: Boolean? = false,
    var resources: List<LegacyControlUnitResourceEntity>? = listOf(),
    var nbHourAtSea: Int? = null,
    var missionReportType: MissionReportTypeEnum? = null,
    var reinforcementType: MissionReinforcementTypeEnum? = null,
    var interMinisterialServices: List<InterMinisterialServiceEntity>? = listOf()
) {

    companion object{
        fun fromMissionGeneralInfo2(generalInfo2: MissionGeneralInfo2, missionId: Int): MissionGeneralInfoEntity {
            return MissionGeneralInfoEntity(
                id = generalInfo2.id!!,
                missionId = missionId,
                missionReportType = generalInfo2.missionReportType,
                reinforcementType = generalInfo2.reinforcementType,
                isMissionArmed = generalInfo2.isMissionArmed,
                interMinisterialServices = generalInfo2.interMinisterialServices?.map { it.toInterMinisterialServiceEntity() },
                nbHourAtSea = generalInfo2.nbHourAtSea,
                isWithInterMinisterialService = generalInfo2.isWithInterMinisterialService,
                resources = generalInfo2.resources
            )
        }
    }
    fun toMissionGeneralInfo(): MissionGeneralInfo {
        return MissionGeneralInfo(
            id = id,
            missionId = missionId,
            distanceInNauticalMiles = distanceInNauticalMiles,
            consumedGOInLiters = consumedGOInLiters,
            consumedFuelInLiters = consumedFuelInLiters,
            serviceId = serviceId,
            nbrOfRecognizedVessel = nbrOfRecognizedVessel,
            resources = resources
        )
    }
}
