package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import org.locationtech.jts.geom.Geometry
import java.time.ZonedDateTime
import java.util.*

data class EnvActionData(
    val id: UUID?,
    val actionStartDateTimeUtc: ZonedDateTime,
    val actionEndDateTimeUtc: ZonedDateTime?,
    val actionType: ActionTypeEnum,
    val department: String? = null,
    val facade: String? = null,
    val geom: Geometry? = null,
    val themes: List<ThemeEntity>? = listOf(),
    val observations: String? = null,
    val actionNumberOfControls: Int? = null,
    val actionTargetType: ActionTargetTypeEnum? = null,
    val vehicleType: VehicleTypeEnum? = null,
    val infractions: List<InfractionEntity>? = listOf(),
    val isAdministrativeControl: Boolean? = null,
    val isComplianceWithWaterRegulationsControl: Boolean? = null,
    val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    val isSeafarersControl: Boolean? = null,
    val controlAdministrative: ControlAdministrative? = null,
    val controlGensDeMer: ControlGensDeMer? = null,
    val controlNavigation: ControlNavigation? = null,
    val controlSecurity: ControlSecurity? = null
) : ActionData()
