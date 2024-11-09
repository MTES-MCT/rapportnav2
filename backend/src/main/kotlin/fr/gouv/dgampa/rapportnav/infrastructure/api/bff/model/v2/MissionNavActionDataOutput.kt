package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import java.time.Instant

data class MissionNavActionDataOutput (
    override val startDateTimeUtc: Instant?,
    override val endDateTimeUtc: Instant? = null,
    override val observations: String? = null,
    override val latitude: Double? = null,
    override val longitude: Double? = null,
    override val detectedPollution: Boolean? = null,
    override val pollutionObservedByAuthorizedAgent: Boolean? = null,
    override val diversionCarriedOut: Boolean? = null,
    override val isSimpleBrewingOperationDone: Boolean? = null,
    override val isAntiPolDeviceDeployed: Boolean? = null,
    override val controlMethod: ControlMethod? = null,
    override val vesselIdentifier: String? = null,
    override val vesselType: VesselTypeEnum? = null,
    override val vesselSize: VesselSizeEnum? = null,
    override val identityControlledPerson: String? = null,
    override val nbOfInterceptedVessels: Int? = null,
    override val nbOfInterceptedMigrants: Int? = null,
    override val nbOfSuspectedSmugglers: Int? = null,
    override val isVesselRescue: Boolean? = false,
    override val isPersonRescue: Boolean? = false,
    override val isVesselNoticed: Boolean? = false,
    override val isVesselTowed: Boolean? = false,
    override val isInSRRorFollowedByCROSSMRCC: Boolean? = false,
    override val numberPersonsRescued: Int? = null,
    override val numberOfDeaths: Int? = null,
    override val operationFollowsDEFREP: Boolean? = false,
    override val locationDescription: String? = null,
    override val isMigrationRescue: Boolean? = false,
    override val nbOfVesselsTrackedWithoutIntervention: Int? = null,
    override val nbAssistedVesselsReturningToShore: Int? = null,
    override val reason: ActionStatusReason? = null,
    override val controlSecurity: ControlSecurityEntity? = null,
    override val controlGensDeMer: ControlGensDeMerEntity? = null,
    override val controlNavigation: ControlNavigationEntity? = null,
    override val controlAdministrative: ControlAdministrativeEntity? = null,
) : MissionActionDataOutput(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    controlSecurity = controlSecurity,
    controlGensDeMer = controlGensDeMer,
    controlNavigation = controlNavigation,
    controlAdministrative = controlAdministrative
), BaseMissionNavActionDataOutput {}
