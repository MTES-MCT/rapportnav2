import { act, render, waitFor } from '../../../../../../test-utils.tsx'
import { vi, describe, it, expect, beforeEach } from 'vitest'
import { MissionAction } from '../../../../common/types/mission-action'
import MissionActionItemIllegalImmigration from '../mission-action-item-illegal-immigration.tsx'

// Real payload reported as possibly triggering an empty backend call on load.
const realAction = {
  source: 'RAPPORT_NAV',
  id: 'a481359f-ba6c-42ca-a440-c8f896a827ba',
  missionId: 21916,
  ownerId: null,
  actionType: 'ILLEGAL_IMMIGRATION',
  summaryTags: ['Sans PV', 'Sans infraction'],
  status: 'UNKNOWN',
  isCompleteForStats: false,
  controlsToComplete: null,
  completenessForStats: {
    status: 'INCOMPLETE',
    sources: ['RAPPORT_NAV'],
    errors: [],
    isComplete: false
  },
  sourcesOfMissingDataForStats: null,
  data: {
    latitude: null,
    longitude: null,
    detectedPollution: null,
    pollutionObservedByAuthorizedAgent: null,
    diversionCarriedOut: null,
    isSimpleBrewingOperationDone: null,
    isAntiPolDeviceDeployed: null,
    controlMethod: null,
    locationType: null,
    vesselIdentifier: null,
    vesselType: null,
    vesselSize: null,
    identityControlledPerson: null,
    nbOfInterceptedVessels: null,
    nbOfInterceptedMigrants: null,
    nbOfSuspectedSmugglers: null,
    isVesselRescue: true,
    isPersonRescue: false,
    isVesselNoticed: false,
    isVesselTowed: false,
    isInSRRorFollowedByCROSSMRCC: false,
    numberPersonsRescued: null,
    numberOfDeaths: null,
    operationFollowsDEFREP: false,
    locationDescription: null,
    isMigrationRescue: false,
    nbOfVesselsTrackedWithoutIntervention: null,
    nbAssistedVesselsReturningToShore: null,
    reason: null,
    startDateTimeUtc: '2025-07-28T07:43:46.297Z',
    endDateTimeUtc: null,
    observations: null,
    status: null,
    targets: null,
    nbrOfHours: null,
    trainingType: null,
    unitManagementTrainingType: null,
    isWithinDepartment: null,
    hasDivingDuringOperation: null,
    incidentDuringOperation: null,
    resourceType: null,
    resourceIds: [],
    nbrOfControl: null,
    controlType: null,
    sectorType: null,
    nbrOfControlAmp: null,
    nbrOfControl300m: null,
    leisureType: null,
    fishingGearType: null,
    isControlDuringSecurityDay: null,
    isSeizureSleepingFishingGear: null,
    sectorEstablishmentType: null,
    nbrSecurityVisit: null,
    securityVisitType: null,
    establishment: null,
    portLocode: null,
    zipCode: null,
    city: null,
    fishAuction: null
  }
} as unknown as MissionAction

describe('MissionActionItemIllegalImmigration - real payload', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('does not call onChange after mounting/initializing with real data', async () => {
    const onChange = vi.fn().mockResolvedValue(undefined)

    render(<MissionActionItemIllegalImmigration action={realAction} onChange={onChange} />)

    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 200))
    })

    await waitFor(() => {
      expect(onChange).not.toHaveBeenCalled()
    })
  })
})
