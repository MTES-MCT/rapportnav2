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
import java.time.Instant
import java.util.*

data class ActionControlEntity(
    @MandatoryForStats
    override val id: UUID,

    @MandatoryForStats
    override val missionId: Int,

    override var isCompleteForStats: Boolean? = null,
    override var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,

    @MandatoryForStats
    override val startDateTimeUtc: Instant,

    @MandatoryForStats
    override val endDateTimeUtc: Instant? = null,

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

    override val observations: String? = null,
) : BaseAction {

    constructor(
        id: UUID,
        missionId: Int,
        startDateTimeUtc: Instant,
        endDateTimeUtc: Instant? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        controlMethod: ControlMethod? = null,
        vesselIdentifier: String? = null,
        vesselType: VesselTypeEnum? = null,
        vesselSize: VesselSizeEnum? = null,
        identityControlledPerson: String? = null,
        observations: String? = null,
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
