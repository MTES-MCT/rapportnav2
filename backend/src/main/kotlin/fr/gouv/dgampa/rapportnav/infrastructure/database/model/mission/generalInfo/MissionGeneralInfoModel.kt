package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.JdpTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReinforcementTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "mission_general_info")
class MissionGeneralInfoModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null,

    @Column(name = "mission_id", nullable = true)
    var missionId: Int? = 0,

    @Column(name = "distance_in_nautical_miles", nullable = true)
    var distanceInNauticalMiles: Float? = null,

    @Column(name = "consumed_go_in_liters", nullable = true)
    var consumedGOInLiters: Float? = null,

    @Column(name = "consumed_fuel_in_liters", nullable = true)
    var consumedFuelInLiters: Float? = null,

    @Column(name = "operating_costs_in_euro", nullable = true)
    var operatingCostsInEuro: Float? = null,

    @Column(name = "fuel_costs_in_euro", nullable = true)
    var fuelCostsInEuro: Float? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    var service: ServiceModel? = null,

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
    var interMinisterialServices: List<InterMinisterialServiceModel>? = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(name = "jdp_type")
    var jdpType: JdpTypeEnum? = null,

    @Column(name = "mission_id_uuid", nullable = true)
    var missionIdUUID: UUID? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: Int? = null,

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: Int? = null
) {
}
