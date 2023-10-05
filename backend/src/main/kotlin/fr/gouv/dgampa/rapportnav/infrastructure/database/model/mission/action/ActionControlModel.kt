package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.mapStringToVesselSize
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.mapStringToVesselType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.mapStringToControlMethod
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
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

//    @Column(name = "geom", nullable = true)
//    var geom: MultiPolygon? = null,

    @Column(name = "vessel_identifier", nullable = false)
    var vesselIdentifier: String,

    @Column(name = "vessel_type", nullable = false)
    var vesselType: String,

    @Column(name = "vessel_size", nullable = false)
    var vesselSize: String,

    @Column(name = "identity_controlled_person", nullable = false)
    var identityControlledPerson: String,

    @Column(name = "observations", nullable = true)
    var observations: String?,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlAdministrative: ControlAdministrativeModel? = null,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlGensDeMer: ControlGensDeMerModel? = null,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlNavigation: ControlNavigationModel? = null,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlSecurity: ControlSecurityModel? = null,
) {
    fun toActionControl(): ActionControl {
        return ActionControl(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            controlMethod = mapStringToControlMethod(controlMethod),
            vesselIdentifier = vesselIdentifier,
            vesselType = mapStringToVesselType(vesselType),
            vesselSize = mapStringToVesselSize(vesselSize),
            observations = observations,
            identityControlledPerson = identityControlledPerson,
            controlAdministrative = controlAdministrative?.toControlAdministrative(),
            controlGensDeMer = controlGensDeMer?.toControlGensDeMer(),
            controlNavigation = controlNavigation?.toControlNavigation(),
            controlSecurity = controlSecurity?.toControlSecurity()
        )
    }

    companion object {
        fun fromActionControl(controlAction: ActionControl, mapper: ObjectMapper) = ActionControlModel(
            id = controlAction.id,
            missionId = controlAction.missionId,
            startDateTimeUtc = controlAction.startDateTimeUtc,
            endDateTimeUtc = controlAction.endDateTimeUtc,
            controlMethod = controlAction.controlMethod.toString(),
            vesselIdentifier = controlAction.vesselIdentifier.toString(),
            vesselType = controlAction.vesselType.toString(),
            vesselSize = controlAction.vesselSize.toString(),
            identityControlledPerson = controlAction.identityControlledPerson.toString(),
            observations = controlAction.observations,
        )
    }


}
