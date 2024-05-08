package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.NavActionControl
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlSecurity
import java.time.ZonedDateTime
import java.util.*

data class ActionControlEntity(
    @MandatoryForStats
    val id: UUID,

    @MandatoryForStats
    val missionId: Int,

    var isCompleteForStats: Boolean? = null,
    var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,

    @MandatoryForStats
    val startDateTimeUtc: ZonedDateTime,

    @MandatoryForStats
    val endDateTimeUtc: ZonedDateTime,

    @MandatoryForStats
    val latitude: Double? = null,

    @MandatoryForStats
    val longitude: Double? = null,

    @MandatoryForStats
    val controlMethod: ControlMethod?,

    @MandatoryForStats
    val vesselIdentifier: String? = null,

    @MandatoryForStats
    val vesselType: VesselTypeEnum? = null,

    @MandatoryForStats
    val vesselSize: VesselSizeEnum? = null,

    @MandatoryForStats
    val identityControlledPerson: String? = null,

    var controlAdministrative: ControlAdministrativeEntity? = null,
    var controlGensDeMer: ControlGensDeMerEntity? = null,
    var controlNavigation: ControlNavigationEntity? = null,
    var controlSecurity: ControlSecurityEntity? = null,

    val observations: String? = null,
) {

    constructor(
        id: UUID,
        missionId: Int,
        startDateTimeUtc: ZonedDateTime,
        endDateTimeUtc: ZonedDateTime,
        latitude: Double?,
        longitude: Double?,
        controlMethod: ControlMethod?,
        vesselIdentifier: String?,
        vesselType: VesselTypeEnum?,
        vesselSize: VesselSizeEnum?,
        identityControlledPerson: String?,
        observations: String?,
        controlAdministrative: ControlAdministrativeEntity? = null,
        controlGensDeMer: ControlGensDeMerEntity? = null,
        controlNavigation: ControlNavigationEntity? = null,
        controlSecurity: ControlSecurityEntity? = null,
    ) : this(
        id = id,
        missionId = missionId,
        isCompleteForStats = null,
        startDateTimeUtc = startDateTimeUtc,
        endDateTimeUtc = endDateTimeUtc,
        latitude = latitude,
        longitude = longitude,
        controlMethod = controlMethod,
        vesselIdentifier = vesselIdentifier,
        vesselType = vesselType,
        vesselSize = vesselSize,
        identityControlledPerson = identityControlledPerson,
        controlAdministrative = controlAdministrative,
        controlGensDeMer = controlGensDeMer,
        controlNavigation = controlNavigation,
        controlSecurity = controlSecurity,
        observations = observations
    ) {
        // completeness for stats being computed at class instantiation in constructor
        this.isCompleteForStats = EntityCompletenessValidator.isCompleteForStats(this)
        this.sourcesOfMissingDataForStats = listOf(MissionSourceEnum.RAPPORTNAV)
    }

    fun toNavActionEntity(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            actionType = ActionType.CONTROL,
            controlAction = this,
            isCompleteForStats = isCompleteForStats,
            sourcesOfMissingDataForStats = sourcesOfMissingDataForStats,
        )
    }

    fun toNavActionControl(): NavActionControl {
        return NavActionControl(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            latitude = latitude,
            longitude = longitude,
            controlMethod = controlMethod,
            observations = observations,
            vesselIdentifier = vesselIdentifier,
            vesselType = vesselType,
            vesselSize = vesselSize,
            identityControlledPerson = identityControlledPerson,
            controlAdministrative = ControlAdministrative.fromControlAdministrativeEntity(controlAdministrative),
            controlGensDeMer = ControlGensDeMer.fromControlGensDeMerEntity(controlGensDeMer),
            controlNavigation = ControlNavigation.fromControlNavigationEntity(controlNavigation),
            controlSecurity = ControlSecurity.fromControlSecurityEntity(controlSecurity),
        )
    }
}
