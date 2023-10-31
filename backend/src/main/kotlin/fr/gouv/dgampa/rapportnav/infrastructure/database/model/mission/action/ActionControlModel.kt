package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.mapStringToVesselSize
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.mapStringToVesselType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.toStringOrNull
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.mapStringToControlMethod
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlSecurityModel
import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "mission_action_control")
data class ActionControlModel(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: ZonedDateTime,

    @Column(name = "end_datetime_utc", nullable = true)
    var endDateTimeUtc: ZonedDateTime,

    @Column(name = "control_method", nullable = false)
    var controlMethod: String,

    @Column(name = "latitude", nullable = true)
    var latitude: Double? = null,

    @Column(name = "longitude", nullable = true)
    var longitude: Double? = null,

    @Column(name = "vessel_identifier", nullable = true)
    var vesselIdentifier: String? = null,

    @Column(name = "vessel_type", nullable = true)
    var vesselType: String? = null,

    @Column(name = "vessel_size", nullable = true)
    var vesselSize: String? = null,

    @Column(name = "identity_controlled_person", nullable = true)
    var identityControlledPerson: String? = null,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @Column(name = "deleted_at")
    var deletedAt: ZonedDateTime? = null,

//    @OneToOne(mappedBy = "actionControl", cascade = [CascadeType.ALL])
//    @JoinColumn(name = "action_control_id", nullable = true)
//    var controlAdministrative: ControlAdministrativeModel? = null,
//
//    @OneToOne(mappedBy = "actionControl", cascade = [CascadeType.ALL])
//    @JoinColumn(name = "action_control_id", insertable = false, updatable = false, nullable = true)
//    var controlGensDeMer: ControlGensDeMerModel? = null,
//
//    @OneToOne(mappedBy = "actionControl", cascade = [CascadeType.ALL])
//    @JoinColumn(name = "action_control_id", insertable = false, updatable = false, nullable = true)
//    var controlNavigation: ControlNavigationModel? = null,
//
//    @OneToOne(mappedBy = "actionControl", cascade = [CascadeType.ALL])
//    @JoinColumn(name = "action_control_id", insertable = false, updatable = false, nullable = true)
//    var controlSecurity: ControlSecurityModel? = null,
) {
    fun toActionControl(): ActionControlEntity {
        return ActionControlEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            latitude = latitude,
            longitude = longitude,
            controlMethod = mapStringToControlMethod(controlMethod),
            vesselIdentifier = vesselIdentifier,
            vesselType = mapStringToVesselType(vesselType),
            vesselSize = mapStringToVesselSize(vesselSize),
            observations = observations,
            identityControlledPerson = identityControlledPerson,
            deletedAt = deletedAt,
//            controlAdministrative = controlAdministrative?.toControlAdministrativeEntity(),
//            controlGensDeMer = controlGensDeMer?.toControlGensDeMerEntity(),
//            controlNavigation = controlNavigation?.toControlNavigationEntity(),
//            controlSecurity = controlSecurity?.toControlSecurityEntity()
        )
    }

    companion object {
        fun fromActionControl(controlAction: ActionControlEntity, mapper: ObjectMapper) = ActionControlModel(
            id = controlAction.id,
            missionId = controlAction.missionId,
            startDateTimeUtc = controlAction.startDateTimeUtc,
            endDateTimeUtc = controlAction.endDateTimeUtc,
            latitude = controlAction.latitude,
            longitude = controlAction.longitude,
            controlMethod = controlAction.controlMethod.toString(),
            vesselIdentifier = controlAction.vesselIdentifier,
            vesselType = controlAction.vesselType.toStringOrNull(),
            vesselSize = controlAction.vesselSize.toStringOrNull(),
            identityControlledPerson = controlAction.identityControlledPerson,
            observations = controlAction.observations,
            deletedAt = controlAction.deletedAt,
        )
    }


}
