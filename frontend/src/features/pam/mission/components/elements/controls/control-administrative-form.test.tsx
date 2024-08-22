import { MockedProvider, MockedResponse } from '@apollo/client/testing'
import { render, screen } from '@testing-library/react'
import { BrowserRouter, Params } from 'react-router-dom'
import { vi } from 'vitest'
import { fireEvent, waitFor } from '../../../test-utils'
import { ActionAntiPollution } from '../../../types/action-types.ts'
import { ControlAdministrative, ControlResult } from '../../../types/control-types.ts'
import UIThemeWrapper from '../../../ui/ui-theme-wrapper.tsx'
import { GET_ACTION_BY_ID } from '../actions/use-action-by-id.tsx'
import { GET_MISSION_TIMELINE } from '../timeline/use-mission-timeline.tsx'
import ControlAdministrativeForm from './control-administrative-form'
import { MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE } from './use-add-update-control.tsx'
import { DELETE_CONTROL_ADMINISTRATIVE } from './use-delete-control.tsx'

vi.mock('react-router-dom', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    useParams: (): Readonly<Params<string>> => ({ missionId: '761', actionId: '3434' })
  }
})

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

type ControlAdministrativeMockProps = {
  unitShouldConfirm?: boolean
  data?: ControlAdministrative
  shouldCompleteControl?: boolean
  mocks?: ReadonlyArray<MockedResponse<any, any>>
}

const renderControlAdministrativeForm = ({ mocks, ...props }: ControlAdministrativeMockProps) =>
  render(
    <UIThemeWrapper>
      <MockedProvider addTypename={false} mocks={mocks}>
        <BrowserRouter>{<ControlAdministrativeForm {...props} />}</BrowserRouter>
      </MockedProvider>
    </UIThemeWrapper>
  )

describe('ControlAdministrativeForm', () => {
  it('should render control administrative', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlAdministrative
    renderControlAdministrativeForm({ data })
    expect(screen.getByText('Permis de mise en exploitation (autorisation à pêcher) conforme')).toBeInTheDocument()
  })

  it('it should have control title check when should complete control is true', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlAdministrative
    const wrapper = renderControlAdministrativeForm({ data, shouldCompleteControl: false })
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    expect(checkbox).toBeChecked()
  })

  it('it should display required red ', () => {
    const wrapper = renderControlAdministrativeForm({ shouldCompleteControl: true })
    expect(wrapper.getAllByTestId('control-title-required-control')).not.toBeNull()
  })

  it('it should show unit should confirm toogle', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlAdministrative
    renderControlAdministrativeForm({ data, unitShouldConfirm: true })
    expect(screen.getByText('Contrôle confirmé par l’unité')).toBeInTheDocument()
  })

  it('it should trigger mutate control', async () => {
    const addOrUpdateControlMatcher = vi.fn().mockReturnValue(true)
    const mocks = [
      {
        request: {
          query: MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE
        },
        variableMatcher: addOrUpdateControlMatcher,
        result: {
          data: MOCK_DATA_MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE
        }
      },
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
      }
    ]
    const wrapper = renderControlAdministrativeForm({ mocks, shouldCompleteControl: false, unitShouldConfirm: false })
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    fireEvent.click(checkbox)
    await waitFor(() => {
      expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(1)
    })
  })

  it('it should trigger delete control', async () => {
    const deleteControlMock = vi.fn().mockReturnValue(true)
    const mocks = [
      {
        request: {
          query: DELETE_CONTROL_ADMINISTRATIVE
        },
        variableMatcher: deleteControlMock,
        result: {
          data: MOCK_DATA_DELETE_CONTROL_ADMINISTRATIVE
        }
      },
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
      }
    ]
    const data = {
      id: 'scscss',
      observations: 'myObservations',
      amountOfControls: 0,
      compliantSecurityDocuments: ControlResult.NOT_CONTROLLED,
      upToDateNavigationPermit: ControlResult.NO,
      compliantOperatingPermit: ControlResult.NOT_CONCERNED
    } as ControlAdministrative

    const wrapper = renderControlAdministrativeForm({
      mocks,
      data,
      shouldCompleteControl: true,
      unitShouldConfirm: false
    })
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    fireEvent.click(checkbox)
    await waitFor(() => {
      expect(deleteControlMock).toHaveBeenCalledTimes(1)
    })
  })

  it('it should update form and trigger mutate control 5 secondes after', async () => {
    vi.useFakeTimers({ shouldAdvanceTime: true })
    const addOrUpdateControlMatcher = vi.fn().mockReturnValue(true)
    const mocks = [
      {
        request: {
          query: MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE
        },
        variableMatcher: addOrUpdateControlMatcher,
        result: {
          data: MOCK_DATA_MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE
        }
      },
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
      }
    ]
    const wrapper = renderControlAdministrativeForm({ mocks, shouldCompleteControl: false, unitShouldConfirm: false })
    const radio = wrapper.container.querySelectorAll("input[type='radio']")[0] as HTMLInputElement
    fireEvent.click(radio)
    expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(0)
    vi.advanceTimersByTime(5000)
    await waitFor(() => {
      expect(addOrUpdateControlMatcher).toHaveBeenCalledTimes(1)
    })
  })
})
