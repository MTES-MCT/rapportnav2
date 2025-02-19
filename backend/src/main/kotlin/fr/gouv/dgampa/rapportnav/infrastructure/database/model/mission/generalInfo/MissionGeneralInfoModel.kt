package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import jakarta.persistence.*

@Entity
@Table(name = "mission_general_info")
class MissionGeneralInfoModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: Int?,

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

    @Column(name = "is_mission_armed", nullable = true)
    var isMissionArmed: Boolean? = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_report_type", nullable = true)
    var missionReportType: MissionReportTypeEnum? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "reinforcement_type", nullable = true)
    var reinforcementType: MissionReinforcementTypeEnum? = null,

    @Column(name = "nb_hour_at_sea")
    var nbHourAtSea: Int? = null,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name="mission_general_info_id")
    @JsonIgnore
    var interMinisterialServices: List<InterMinisterialServiceModel>? = mutableListOf()
) {
}
