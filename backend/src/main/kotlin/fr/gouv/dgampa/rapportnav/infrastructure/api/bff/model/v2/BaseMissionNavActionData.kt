package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import java.time.Instant

interface BaseMissionNavActionData {
    val startDateTimeUtc: Instant?
    val endDateTimeUtc: Instant?
    val observations: String?
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
    val status: ActionStatusType?
    val reason: ActionStatusReason?
    val nbrOfHours: Int?
    val trainingType: String?
    val unitManagementTrainingType: String?
    val isWithinDepartment: Boolean?
    val hasDivingDuringOperation: Boolean?
    val incidentDuringOperation: Boolean?
    val resourceType: String?
    val resourceId: Int?
    var siren: String?
    var nbrOfControl: Int?
    var controlType: String?
    val sectorType: SectorType?
    var nbrOfControlAmp: Int?
    var nbrOfControl300m: Int?
    var leisureType: LeisureType?
    var fishingGearType: FishingGearType?
    var isControlDuringSecurityDay: Boolean?
    var isSeizureSleepingFishingGear: Boolean?
    var sectorEstablishmentType: SectorEstablishmentType?
    var nbrSecurityVisit:Int?
    var securityVisitType: SecurityVisitType?
}
