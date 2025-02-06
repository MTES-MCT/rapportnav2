package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "mission_general_info")
class MissionGeneralInfoModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: Int,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int = 0,

    @Column(name = "distance_in_nautical_miles", nullable = true)
    var distanceInNauticalMiles: Float? = null,

    @Column(name = "consumed_go_in_liters", nullable = true)
    var consumedGOInLiters: Float? = null,

    @Column(name = "consumed_fuel_in_liters", nullable = true)
    var consumedFuelInLiters: Float? = null,

    @Column(name = "service_id", nullable = true)
    var serviceId: Int? = null,

    @Column(name = "nbr_of_recognized_vessel", nullable = true)
    var  nbrOfRecognizedVessel: Int? = null,

    @Column(name = "is_with_interministerial_service", nullable = true)
    var isWithInterMinisterialService: Boolean? = false,

    @Column(name = "is_all_agents_participating", nullable = true)
    var isAllAgentsParticipating: Boolean? = false,

    @Column(name = "is_mission_armed", nullable = true)
    var isMissionArmed: Boolean? = false
) {
    fun toMissionGeneralInfoEntity(): MissionGeneralInfoEntity {
        return MissionGeneralInfoEntity(
            id,
            missionId,
            distanceInNauticalMiles,
            consumedGOInLiters,
            consumedFuelInLiters,
            serviceId,
            nbrOfRecognizedVessel,
            isWithInterMinisterialService,
            isAllAgentsParticipating,
            isMissionArmed
        )
    }

    companion object {
        fun fromMissionGeneralInfoEntity(info: MissionGeneralInfoEntity) = MissionGeneralInfoModel(
            id = info.id,
            missionId = info.missionId,
            distanceInNauticalMiles = info.distanceInNauticalMiles,
            consumedGOInLiters = info.consumedGOInLiters,
            consumedFuelInLiters = info.consumedFuelInLiters,
            serviceId = info.serviceId,
            nbrOfRecognizedVessel = info.nbrOfRecognizedVessel,
            isWithInterMinisterialService = info.isWithInterMinisterialService,
            isAllAgentsParticipating = info.isAllAgentsParticipating,
            isMissionArmed = info.isMissionArmed
        )
    }
}
