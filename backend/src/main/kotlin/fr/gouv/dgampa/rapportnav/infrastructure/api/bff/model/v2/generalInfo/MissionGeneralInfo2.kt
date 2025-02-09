package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import java.time.Instant

data class MissionGeneralInfo2(
    val id: Int? = null,
    val missionId: Int,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val missionReportType: MissionReportTypeEnum,
    val missionTypes: List<MissionTypeEnum>,
    val reinforcementType: MissionReinforcementTypeEnum? = null,
    val nbHourAtSea: Int? = null,
    var distanceInNauticalMiles: Float? = null,
    var consumedGOInLiters: Float? = null,
    var consumedFuelInLiters: Float? = null,
    var serviceId: Int? = null,
    var nbrOfRecognizedVessel: Int? = null,
    val crew: List<MissionCrew>? = null,
    val services: List<ServiceEntity>? = null,
    val isWithInterMinisterialService: Boolean? = false,
    val isAllAgentsParticipating: Boolean? = false,
    val isMissionArmed: Boolean? = false,
    val observations: String? = null,
    val resources: List<String>? = listOf(),
) {
    companion object {
        fun fromMissionGeneralInfoEntity(
            envData: MissionEntity,
            generalInfo2: MissionGeneralInfoEntity2?
        ): MissionGeneralInfo2 {
            return MissionGeneralInfo2(
                missionId = generalInfo2?.data?.missionId!!,
                id = generalInfo2?.data?.id,
                startDateTimeUtc = envData.startDateTimeUtc,
                endDateTimeUtc = envData.endDateTimeUtc,
                missionTypes = envData.missionTypes,
                missionReportType = MissionReportTypeEnum.FIELD_REPORT,
                nbHourAtSea = 0,
                distanceInNauticalMiles = generalInfo2?.data?.distanceInNauticalMiles,
                consumedGOInLiters = generalInfo2?.data?.consumedGOInLiters,
                consumedFuelInLiters = generalInfo2?.data?.consumedFuelInLiters,
                serviceId = generalInfo2?.data?.serviceId,
                nbrOfRecognizedVessel = generalInfo2?.data?.nbrOfRecognizedVessel,
                services = generalInfo2?.services,
                crew = generalInfo2?.crew?.map { MissionCrew.fromMissionCrewEntity(it) },
                isAllAgentsParticipating = generalInfo2?.data?.isAllAgentsParticipating,
                isWithInterMinisterialService = generalInfo2?.data?.isWithInterMinisterialService,
                isMissionArmed = generalInfo2?.data?.isMissionArmed,
                observations = envData.observationsByUnit
                //TODO: resource
            )
        }
    }

    fun toMissionGeneralInfoEntity(): MissionGeneralInfoEntity {
        return MissionGeneralInfoEntity(
            id = id!!,
            missionId = missionId,
            distanceInNauticalMiles = distanceInNauticalMiles,
            consumedGOInLiters = consumedGOInLiters,
            consumedFuelInLiters = consumedFuelInLiters,
            serviceId = serviceId,
            nbrOfRecognizedVessel = nbrOfRecognizedVessel,
            isWithInterMinisterialService = isWithInterMinisterialService,
            isAllAgentsParticipating = isAllAgentsParticipating,
            isMissionArmed = isMissionArmed
        )
    }
}
