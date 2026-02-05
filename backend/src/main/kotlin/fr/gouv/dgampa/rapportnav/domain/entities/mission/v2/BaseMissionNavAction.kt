package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import java.time.Instant
import java.util.*

interface BaseMissionNavAction {
    var id: UUID
    var missionId: Int
    var ownerId: UUID?
    var actionType: ActionType
    var isCompleteForStats: Boolean?
    val sourcesOfMissingDataForStats: List<MissionSourceEnum>?
    var startDateTimeUtc: Instant?
    var endDateTimeUtc: Instant?
    var observations: String?
    var latitude: Double?
    var longitude: Double?
    var detectedPollution: Boolean?
    var pollutionObservedByAuthorizedAgent: Boolean?
    var diversionCarriedOut: Boolean?
    var isSimpleBrewingOperationDone: Boolean?
    var isAntiPolDeviceDeployed: Boolean?
    var controlMethod: ControlMethod?
    var vesselIdentifier: String?
    var vesselType: VesselTypeEnum?
    var vesselSize: VesselSizeEnum?
    var identityControlledPerson: String?
    var nbOfInterceptedVessels: Int?
    var nbOfInterceptedMigrants: Int?
    var nbOfSuspectedSmugglers: Int?
    var isVesselRescue: Boolean?
    var isPersonRescue: Boolean?
    var isVesselNoticed: Boolean?
    var isVesselTowed: Boolean?
    var isInSRRorFollowedByCROSSMRCC: Boolean?
    var numberPersonsRescued: Int?
    var numberOfDeaths: Int?
    var operationFollowsDEFREP: Boolean?
    var locationDescription: String?
    var isMigrationRescue: Boolean?
    var nbOfVesselsTrackedWithoutIntervention: Int?
    var nbAssistedVesselsReturningToShore: Int?
    var reason: ActionStatusReason?
    var nbrOfHours: Int?
    var trainingType: String?
    var unitManagementTrainingType: String?
    var isWithinDepartment: Boolean?
    var hasDivingDuringOperation: Boolean?
    var incidentDuringOperation: Boolean?
    var resourceType: String?
    var resourceId: Int?
    var establishment: EstablishmentEntity?
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
    var nbrSecurityVisit: Int?
    var securityVisitType: SecurityVisitType?
}
