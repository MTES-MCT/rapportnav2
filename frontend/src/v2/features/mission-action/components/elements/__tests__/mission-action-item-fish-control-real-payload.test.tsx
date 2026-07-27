import { beforeEach, describe, expect, it, vi } from 'vitest'
import { act, render } from '../../../../../../test-utils.tsx'
import { MissionAction } from '../../../../common/types/mission-action'
import MissionActionItemFishControl from '../mission-action-item-fish-control.tsx'

vi.mock('../../../../fish-sati/hooks/use-sati.tsx', () => ({
  useSati: () => ({ isSatiEnabled: true })
}))

// Real payload reported as triggering a backend call with no user-visible change.
const realAction = {
  source: 'MONITORFISH',
  id: '10522',
  missionId: 21916,
  actionType: 'CONTROL',
  isCompleteForStats: true,
  status: 'UNKNOWN',
  summaryTags: ['1 PV', '3 NATINF'],
  controlsToComplete: [],
  completenessForStats: { status: 'VALID', sources: null, errors: [], isComplete: true },
  sourcesOfMissingDataForStats: [],
  data: {
    startDateTimeUtc: '2026-05-13T16:00:00Z',
    endDateTimeUtc: '2026-05-13T18:00:00Z',
    vesselId: 1245284,
    vesselName: 'LA PAIX DU CIEL II',
    internalReferenceNumber: 'FRA000925174',
    externalReferenceNumber: 'AC925174',
    flagState: 'FR',
    districtCode: 'AC',
    faoAreas: ['27.8.b'],
    fishActionType: 'LAND_CONTROL',
    emitsVms: 'NOT_APPLICABLE',
    emitsAis: 'NOT_APPLICABLE',
    vmsEmissionControlBeforeArrival: 'YES',
    portEntranceAndLandingAuthorized: 'YES',
    logbookFilledPriorToControl: 'NO',
    logbookMatchesActivity: 'YES',
    licencesMatchActivity: 'YES',
    speciesWeightControlled: 'YES',
    speciesSizeControlled: 'NO',
    separateStowageOfPreservedSpecies: 'YES',
    propulsionEnginePowerControl: 'NOT_APPLICABLE',
    fishingLicencesMatchActivity: 'YES',
    stowagePlanPresent: 'NO',
    onboardWeighingPermit: 'NOT_APPLICABLE',
    weighingCertificateAndSystemsValid: 'NOT_APPLICABLE',
    underSizedSeparateStowage: 'YES',
    underSizedSeparateRecording: 'YES',
    minimumConservationReferenceSizeControlled: 'NO',
    cratesWeighingSamplingControl: 'NOT_APPLICABLE',
    approvedWeighingOperatorInformation: 'NOT_APPLICABLE',
    holdControlledAfterUnloading: 'YES',
    catchesWeighedAtLanding: 'NO',
    licencesAndLogbookObservations: null,
    speciesObservations: null,
    seizureAndDiversion: false,
    numberOfVesselsFlownOver: null,
    unitWithoutOmegaGauge: false,
    controlQualityComments: null,
    feedbackSheetRequired: null,
    userTrigram: 'RNT',
    segments: [{ segment: 'SWW09', segmentName: 'Fileyeurs Golfe de Gascogne 80-100mm' }],
    facade: 'SA',
    longitude: null,
    latitude: null,
    portLocode: 'FRARC',
    portName: 'La Rochelle',
    vesselTargeted: 'NO',
    seizureAndDiversionComments: null,
    otherComments: null,
    gearOnboard: [],
    speciesOnboard: [],
    discardedSpecies: [],
    isFromPoseidon: false,
    isDeleted: null,
    hasSomeGearsSeized: false,
    hasSomeSpeciesSeized: false,
    completedBy: 'RNT',
    completion: 'COMPLETED',
    isLastHaul: true,
    isAdministrativeControl: false,
    isComplianceWithWaterRegulationsControl: false,
    isSafetyEquipmentAndStandardsComplianceControl: false,
    isSeafarersControl: false,
    isINNControl: false,
    isGangwayDeployed: true,
    observationsByUnit: 'this is an observation',
    speciesQuantitySeized: null,
    targets: [],
    fishInfractions: [],
    sati: {
      id: null,
      actionId: '',
      module: 'M1',
      resource: null,
      vessel: null,
      startDatetimeUtc: null,
      endDatetimeUtc: null,
      principalInspector: {
        id: null,
        cardId: null,
        party: null,
        authorityType: null,
        agentId: null,
        isOutOfUnit: false
      },
      otherInspectors: []
    },
    hasDivingDuringOperation: null,
    incidentDuringOperation: null,
    observations: null
  },
  ownerId: null
} as unknown as MissionAction

describe('MissionActionItemFishControl - real payload, no mocked hook', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('does not call onChange after mounting/initializing with real data', async () => {
    const onChange = vi.fn().mockResolvedValue(undefined)

    render(<MissionActionItemFishControl action={realAction} onChange={onChange} />)

    // Let the initValue effect, Formik mount, and FormikEffect's first evaluation settle.
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 200))
    })

    if (onChange.mock.calls.length > 0) {
      const [submittedAction] = onChange.mock.calls[0]
      console.log('onChange called', onChange.mock.calls.length, 'time(s). Diff vs realAction.data:')
      const before = realAction.data as Record<string, unknown>
      const after = submittedAction.data as Record<string, unknown>
      const keys = new Set([...Object.keys(before), ...Object.keys(after)])
      for (const key of keys) {
        if (JSON.stringify(before[key]) !== JSON.stringify(after[key])) {
          console.log(key, '\n  before:', JSON.stringify(before[key]), '\n  after: ', JSON.stringify(after[key]))
        }
      }
    }

    expect(onChange).not.toHaveBeenCalled()
  })
})
