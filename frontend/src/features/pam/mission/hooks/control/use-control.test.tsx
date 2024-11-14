import { MockedProvider } from '@apollo/client/testing'
import { ActionAntiPollution } from '@common/types/action-types'
import { ControlAdministrative, ControlResult, ControlType } from '@common/types/control-types'
import { act, renderHook } from '@testing-library/react'
import { vi } from 'vitest'
import { GET_ACTION_BY_ID } from '../use-action-by-id'
import { MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE } from '../use-add-update-control'
import { DELETE_CONTROL_ADMINISTRATIVE } from '../use-delete-control'
import { GET_MISSION_TIMELINE } from '../use-mission-timeline'
import { useControl } from './use-control'

const deleteControlMatcher = vi.fn()
const addOrUpdateControlMatcher = vi.fn()

const MOCK_DATA_GET_ACTION_BY_ID = {
  data: {
    id: '',
    startDateTimeUtc: '2024-01-01T00:00:00Z',
    endDateTimeUtc: '2024-01-12T01:00:00Z',
    longitude: 3,
    latitude: 5,
    observations: '',
    detectedPollution: false,
    pollutionObservedByAuthorizedAgent: false,
    diversionCarriedOut: false,
    isSimpleBrewingOperationDone: false,
    isAntiPolDeviceDeployed: false
  } as ActionAntiPollution
}

const MOCK_DATA_GET_MISSION_TIMELINE = {
  mission: {
    id: '762',
    startDateTimeUtc: '2024-01-09T09:00Z',
    endDateTimeUtc: '2024-01-09T15:00Z',
    status: null,
    completenessForStats: null,
    actions: [
      {
        id: '763',
        type: 'CONTROL',
        source: 'MONITORFISH',
        status: 'UNKNOWN',
        summaryTags: ['Sans PV', '4 NATINF'],
        startDateTimeUtc: '2024-01-09T14:00Z',
        endDateTimeUtc: null,
        completenessForStats: {
          status: 'INCOMPLETE',
          sources: ['RAPPORTNAV'],
          __typename: 'CompletenessForStats'
        },
        data: {
          id: '763',
          actionDatetimeUtc: '2024-01-09T14:00Z',
          actionType: 'LAND_CONTROL',
          vesselId: 5232556,
          vesselName: 'Le Stella',
          controlsToComplete: ['ADMINISTRATIVE', 'SECURITY'],
          __typename: 'FishActionData'
        },
        __typename: 'Action'
      },
      {
        id: '226d84bc-e6c5-4d29-8a5f-7db642f99762',
        type: 'CONTROL',
        source: 'MONITORENV',
        status: 'UNKNOWN',
        summaryTags: ['Sans PV', '2 NATINF'],
        startDateTimeUtc: '2024-01-09T10:00Z',
        endDateTimeUtc: null,
        completenessForStats: {
          status: 'INCOMPLETE',
          sources: ['RAPPORTNAV', 'MONITORENV'],
          __typename: 'CompletenessForStats'
        },
        data: {
          id: '226d84bc-e6c5-4d29-8a5f-7db642f99762',
          actionNumberOfControls: 2,
          actionTargetType: 'VEHICLE',
          vehicleType: 'VESSEL',
          controlsToComplete: ['NAVIGATION', 'SECURITY'],
          formattedControlPlans: null,
          __typename: 'EnvActionData'
        },
        __typename: 'Action'
      },
      {
        id: '226d84bc-e6c5-4d29-8a5f-799642f99762',
        type: 'SURVEILLANCE',
        source: 'MONITORENV',
        status: 'UNKNOWN',
        summaryTags: ['Sans PV', 'Sans infraction'],
        startDateTimeUtc: '2024-01-09T12:00Z',
        endDateTimeUtc: '2024-01-09T13:00Z',
        completenessForStats: {
          status: 'COMPLETE',
          sources: [],
          __typename: 'CompletenessForStats'
        },
        data: {
          id: '226d84bc-e6c5-4d29-8a5f-799642f99762',
          actionNumberOfControls: null,
          actionTargetType: null,
          vehicleType: null,
          controlsToComplete: [],
          formattedControlPlans: null,
          __typename: 'EnvActionData'
        },
        __typename: 'Action'
      },
      {
        id: '8cea3f9e-fc6c-433a-8de4-e8d664ea25ed',
        type: 'CONTROL',
        source: 'RAPPORTNAV',
        status: 'UNKNOWN',
        summaryTags: ['Sans PV', 'Sans infraction'],
        startDateTimeUtc: '2024-08-19T14:05Z',
        endDateTimeUtc: '2024-08-19T14:05Z',
        completenessForStats: {
          status: 'INCOMPLETE',
          sources: null,
          __typename: 'CompletenessForStats'
        },
        data: {
          id: '8cea3f9e-fc6c-433a-8de4-e8d664ea25ed',
          controlMethod: 'SEA',
          vesselIdentifier: null,
          vesselType: 'FISHING',
          vesselSize: null,
          __typename: 'NavActionControl'
        },
        __typename: 'Action'
      }
    ],
    __typename: 'Mission'
  }
}

const MOCK_DATA_MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE = {
  addOrUpdateControlAdministrative: {
    id: 'bf2ace26-1897-4c69-84f4-1b0d1e275eba',
    amountOfControls: 1,
    unitShouldConfirm: null,
    unitHasConfirmed: true,
    compliantOperatingPermit: null,
    upToDateNavigationPermit: null,
    compliantSecurityDocuments: 'NO',
    observations: null,
    __typename: 'ControlAdministrative'
  }
}
const MOCK_DATA_DELETE_CONTROL_ADMINISTRATIVE = { deleteControlAdministrative: true }

const mocks = [
  {
    request: {
      query: GET_ACTION_BY_ID
    },
    variableMatcher: () => true,
    result: {
      data: MOCK_DATA_GET_ACTION_BY_ID
    }
  },
  {
    request: {
      query: GET_MISSION_TIMELINE
    },
    variableMatcher: () => true,
    result: {
      data: MOCK_DATA_GET_MISSION_TIMELINE
    }
  },
  {
    request: {
      query: DELETE_CONTROL_ADMINISTRATIVE
    },
    variableMatcher: deleteControlMatcher,
    result: {
      data: MOCK_DATA_DELETE_CONTROL_ADMINISTRATIVE
    }
  },
  {
    request: {
      query: MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE
    },
    variableMatcher: addOrUpdateControlMatcher,
    result: {
      data: MOCK_DATA_MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE
    }
  }
]

const actionId = 'myActionId'

const data = {
  id: 'myId',
  observations: 'myObservations',
  amountOfControls: 0,
  compliantSecurityDocuments: ControlResult.NOT_CONTROLLED,
  upToDateNavigationPermit: ControlResult.NO,
  compliantOperatingPermit: ControlResult.NOT_CONCERNED
} as ControlAdministrative

const control = {
  ...data,
  missionId: 'missionId',
  unitShouldConfirm: false,
  amountOfControls: 1,
  unitHasConfirmed: undefined
}

export const wrapper = ({ children }: { children: JSX.Element }): JSX.Element => (
  <MockedProvider addTypename={false} mocks={mocks}>
    {children}
  </MockedProvider>
)

describe('useControl hook', () => {
  beforeEach(() => {
    deleteControlMatcher.mockReturnValue(true)
    addOrUpdateControlMatcher.mockReturnValue(true)
  })

  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('it should trigger mutate control', async () => {
    const { result } = renderHook(() => useControl(undefined, ControlType.ADMINISTRATIVE, false), { wrapper })
    act(() => {
      result.current.toggleControl(true, actionId, control)
    })
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(1)
  })

  it('it should trigger delete control', async () => {
    const { result } = renderHook(() => useControl(data, ControlType.ADMINISTRATIVE, true), { wrapper })
    act(() => {
      result.current.toggleControl(false, actionId, control)
    })
    expect(deleteControlMatcher).toHaveBeenCalledTimes(1)
  })

  it('it should update form and trigger mutate control 5 secondes after', async () => {
    vi.useFakeTimers({ shouldAdvanceTime: true })
    const { result } = renderHook(() => useControl(data, ControlType.ADMINISTRATIVE, true), { wrapper })
    act(() => {
      result.current.controlChanged(actionId, control)
    })
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(0)
    vi.advanceTimersByTime(5000)
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(1)
  })

  it('it should update control', async () => {
    const { result } = renderHook(() => useControl(data, ControlType.ADMINISTRATIVE, true), { wrapper })
    act(() => {
      result.current.updateControl(actionId, control)
    })
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(1)
  })

  it('it should update control with unitHasConfirmed true if data and unitHasConfirmed input form is undefined', async () => {
    control.unitHasConfirmed = undefined
    const { result } = renderHook(() => useControl(data, ControlType.ADMINISTRATIVE, true), { wrapper })
    act(() => {
      result.current.updateControl(actionId, control)
    })
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(1)
    expect(addOrUpdateControlMatcher).toHaveBeenCalledWith({
      control: expect.objectContaining({ unitHasConfirmed: true })
    })
  })

  it('it should override debounce time', async () => {
    const debounceTime = 10000
    vi.useFakeTimers({ shouldAdvanceTime: true })
    const { result } = renderHook(() => useControl(data, ControlType.ADMINISTRATIVE, true, debounceTime), { wrapper })
    act(() => {
      result.current.controlChanged(actionId, control)
    })
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(0)
    vi.advanceTimersByTime(5000)
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(0)
    vi.advanceTimersByTime(debounceTime)
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(1)
  })

  it('it should not send a request if the toogle is checked but amountControls value is not defined or 0', async () => {
    vi.useFakeTimers({ shouldAdvanceTime: true })
    control.amountOfControls = 0
    const { result } = renderHook(() => useControl(data, ControlType.ADMINISTRATIVE, true), { wrapper })
    act(() => {
      result.current.toggleControl(true, actionId, control)
    })
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(0)
    vi.advanceTimersByTime(10000)
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(0)
  })
})
