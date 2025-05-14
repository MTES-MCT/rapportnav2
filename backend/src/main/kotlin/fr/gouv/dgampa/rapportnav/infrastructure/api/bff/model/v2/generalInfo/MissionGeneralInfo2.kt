package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.generalInfo.InterMinisterialService
import java.time.Instant

data class MissionGeneralInfo2(
    val id: Int? = null,
    val missionId: Int? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val missionReportType: MissionReportTypeEnum? = null,
    val missionTypes: List<MissionTypeEnum> = listOf(),
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
    val resources: List<LegacyControlUnitResource>? = listOf(),
    val interMinisterialServices : List<InterMinisterialService>? = listOf(),
    val jdpType: JdpTypeEnum? = null,
    val missionIdString: String? = null
) {
    companion object {
        fun fromMissionGeneralInfoEntity(
            generalInfo2: MissionGeneralInfoEntity2?
        ): MissionGeneralInfo2 {
            return MissionGeneralInfo2(
                missionId = generalInfo2?.data?.missionId,
                id = generalInfo2?.data?.id,
                missionReportType = generalInfo2?.data?.missionReportType?: MissionReportTypeEnum.FIELD_REPORT,
                nbHourAtSea = generalInfo2?.data?.nbHourAtSea,
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
                interMinisterialServices = generalInfo2?.data?.interMinisterialServices?.map {
                    InterMinisterialService.fromInterMinisterialServiceEntity(
                        it
                    )
                } ?: listOf(),
                reinforcementType = generalInfo2?.data?.reinforcementType,
                jdpType = generalInfo2?.data?.jdpType,
                missionIdString = generalInfo2?.data?.missionIdString
            )
        }
    }

    fun toMissionGeneralInfoEntity(missionId: String): MissionGeneralInfoEntity {

        val idAsInt = if (!isValidUUID(missionId)) missionId.toInt() else null
        return MissionGeneralInfoEntity(
            id = id,
            missionId = idAsInt,
            distanceInNauticalMiles = distanceInNauticalMiles,
            consumedGOInLiters = consumedGOInLiters,
            consumedFuelInLiters = consumedFuelInLiters,
            serviceId = serviceId,
            nbrOfRecognizedVessel = nbrOfRecognizedVessel,
            isWithInterMinisterialService = isWithInterMinisterialService,
            isAllAgentsParticipating = isAllAgentsParticipating,
            isMissionArmed = isMissionArmed,
            nbHourAtSea = nbHourAtSea,
            interMinisterialServices = interMinisterialServices?.map { it.toInterMinisterialServiceEntity() },
            missionReportType = missionReportType,
            reinforcementType = reinforcementType,
            jdpType = jdpType,
            missionIdString = missionIdString
        )
    }

    fun isMissionNav(): Boolean {
       return missionReportType in listOf(MissionReportTypeEnum.OFFICE_REPORT, MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT)
    }
}
