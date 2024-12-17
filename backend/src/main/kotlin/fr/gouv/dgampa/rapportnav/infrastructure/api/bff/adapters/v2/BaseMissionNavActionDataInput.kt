package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType

interface BaseMissionNavActionDataInput {
    val latitude: Double?
    val longitude: Double?
    val detectedPollution: Boolean?
    val pollutionObservedByAuthorizedAgent: Boolean?
    val diversionCarriedOut: Boolean?
    val isSimpleBrewingOperationDone: Boolean?
    val isAntiPolDeviceDeployed: Boolean?
    val controlMethod: ControlMethod?
    val vesselIdentifier: String?
    val vesselType: VesselTypeEnum?
    val vesselSize: VesselSizeEnum?
    val identityControlledPerson: String?
    val nbOfInterceptedVessels: Int?
    val nbOfInterceptedMigrants: Int?
    val nbOfSuspectedSmugglers: Int?
    val isVesselRescue: Boolean?
    val isPersonRescue: Boolean?
    val isVesselNoticed: Boolean?
    val isVesselTowed: Boolean?
    val isInSRRorFollowedByCROSSMRCC: Boolean?
    val numberPersonsRescued: Int?
    val numberOfDeaths: Int?
    val operationFollowsDEFREP: Boolean?
    val locationDescription: String?
    val isMigrationRescue: Boolean?
    val nbOfVesselsTrackedWithoutIntervention: Int?
    val nbAssistedVesselsReturningToShore: Int?
    val reason: ActionStatusReason?
    val status: ActionStatusType?
}
