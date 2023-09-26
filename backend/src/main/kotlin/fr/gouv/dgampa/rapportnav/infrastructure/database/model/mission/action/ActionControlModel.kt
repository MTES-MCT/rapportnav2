package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.mapStringToVesselSize
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.mapStringToVesselType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlEquipmentAndSecurityModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationRulesModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlVesselAdministrativeModel
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

//    @Column(name = "geom", nullable = true)
//    var geom: MultiPolygon? = null,

    @Column(name = "observations", nullable = true)
    var observations: String,

    @Column(name = "vessel_identifier", nullable = true)
    var vesselIdentifier: String,

    @Column(name = "vessel_type", nullable = true)
    var vesselType: String,

    @Column(name = "vessel_size", nullable = true)
    var vesselSize: String,

    @Column(name = "identity_controlled_person", nullable = true)
    var identityControlledPerson: String,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlsVesselAdministrative: ControlVesselAdministrativeModel? = null,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlsGensDeMer: ControlGensDeMerModel? = null,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlsNavigationRules: ControlNavigationRulesModel? = null,

    @OneToOne(mappedBy = "actionControl")
    @JoinColumn(name = "action_control_id")
    var controlsEquipmentAndSecurity: ControlEquipmentAndSecurityModel? = null,
) {
    fun toActionControl(): ActionControl {
        return ActionControl(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
//            geom = geom,
            observations = observations,
            vesselIdentifier = vesselIdentifier,
            vesselType = mapStringToVesselType(vesselType),
            vesselSize = mapStringToVesselSize(vesselSize),
            identityControlledPerson = identityControlledPerson,
            controlsVesselAdministrative = controlsVesselAdministrative?.toControlVesselAdministrative(),
            controlsGensDeMer = controlsGensDeMer?.toControlGensDeMer(),
            controlsNavigationRules = controlsNavigationRules?.toControlNavigationRules(),
            controlsEquipmentAndSecurity = controlsEquipmentAndSecurity?.toControlEquipmentAndSecurity()
        )
    }


}
