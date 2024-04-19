import { render, screen } from '../../../test-utils'
import { ActionTypeEnum, MissionSourceEnum } from '../../../types/env-mission-types.ts'
import ActionControlNav from './action-control-nav.tsx'
import { Action, ActionControl, ActionStatusType } from '../../../types/action-types.ts'
import { ControlMethod } from '../../../types/control-types.ts'
import { vi } from 'vitest'
import useActionById from './use-action-by-id.tsx'
import { GraphQLError } from 'graphql/error'
import { fireEvent } from '../../../test-utils.tsx'
import { VesselTypeEnum } from '../../../types/mission-types.ts'

const mutateControlMock = vi.fn()
const deleteControlMock = vi.fn()
vi.mock('./use-action-by-id.tsx', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: vi.fn()
  }
})

vi.mock('./use-add-update-action-control.tsx', () => ({
  default: () => [mutateControlMock]
}))

vi.mock('./use-delete-action-control.tsx', () => ({
  default: () => [deleteControlMock]
}))

const actionMock = {
  id: '1',
  missionId: 1,
  type: ActionTypeEnum.CONTROL,
  source: MissionSourceEnum.RAPPORTNAV,
  status: ActionStatusType.DOCKED,
  startDateTimeUtc: '2022-01-01T00:00:00Z',
  endDateTimeUtc: '2022-01-01T01:00:00Z',
  summaryTags: undefined,
  controlsToComplete: undefined,
  data: {
    controlMethod: ControlMethod.AIR,
    latitude: 123,
    longitude: 123,
    startDateTimeUtc: '2022-01-01T00:00:00Z',
    endDateTimeUtc: '2022-01-01T01:00:00Z'
  } as any as ActionControl
}

// Set up the mock implementation for useActionById
const mockedQueryResult = (action: Action = actionMock as any, loading: boolean = false, error: any = undefined) => ({
  data: action,
  loading,
  error
})

describe('ActionControlNav', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, true))
      render(<ActionControlNav action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false, new GraphQLError('Error!')))
      render(<ActionControlNav action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      render(<ActionControlNav action={actionMock} />)
      expect(screen.getByText('Dupliquer')).toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      ;(useActionById as any).mockReturnValue({ ...mockedQueryResult(undefined, false), data: null })
      render(<ActionControlNav action={actionMock} />)
      expect(screen.queryByTestId('action-control-nav')).not.toBeInTheDocument()
    })
  })

  describe('The update mutation', () => {
    beforeEach(() => {
      mutateControlMock.mockClear()
    })
    it('should be called when changing observations', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))

      render(<ActionControlNav action={actionMock} />)

      const field = screen.getByTestId('observations')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      expect(mutateControlMock).toHaveBeenCalledWith({
        variables: {
          controlAction: {
            // actionId and missionId are undefined because useParam is not setup in the test run
            missionId: undefined,
            controlMethod: 'AIR',
            endDateTimeUtc: '2022-01-01T01:00:00Z',
            latitude: 123,
            longitude: 123,
            startDateTimeUtc: '2022-01-01T00:00:00Z',
            observations: value
          }
        }
      })
    })
    it('should be called when changing identity of the controlled person', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))

      render(<ActionControlNav action={actionMock} />)

      const field = screen.getByTestId('identityControlledPerson')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      expect(mutateControlMock).toHaveBeenCalledWith({
        variables: {
          controlAction: {
            // actionId and missionId are undefined because useParam is not setup in the test run
            missionId: undefined,
            controlMethod: 'AIR',
            endDateTimeUtc: '2022-01-01T01:00:00Z',
            latitude: 123,
            longitude: 123,
            startDateTimeUtc: '2022-01-01T00:00:00Z',
            identityControlledPerson: value
          }
        }
      })
    })
    it('should be called when changing vessel identifier', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))

      render(<ActionControlNav action={actionMock} />)

      const field = screen.getByTestId('vesselIdentifier')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      expect(mutateControlMock).toHaveBeenCalledWith({
        variables: {
          controlAction: {
            // actionId and missionId are undefined because useParam is not setup in the test run
            missionId: undefined,
            controlMethod: 'AIR',
            endDateTimeUtc: '2022-01-01T01:00:00Z',
            latitude: 123,
            longitude: 123,
            startDateTimeUtc: '2022-01-01T00:00:00Z',
            vesselIdentifier: value
          }
        }
      })
    })
    // TODO figure out how to make it work when Select component
    //     it('should be called when changing vessel size', async () => {
    //         ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
    //
    //         render(<ActionControlNav action={actionMock}/>);
    //
    //         const field = screen.getByRole('vesselSize')
    //         const value = VESSEL_SIZE_OPTIONS[0].value
    //         fireEvent.change(field, {value});
    //
    //             expect(mutateControlMock).toHaveBeenCalledWith({
    //                 "variables": {
    //                     "controlAction": {
    //                         // actionId and missionId are undefined because useParam is not setup in the test run
    //                         "missionId": undefined,
    //                         "controlMethod": "AIR",
    //                         "endDateTimeUtc": "2022-01-01T01:00:00Z",
    //                         "latitude": 123,
    //                         "longitude": 123,
    //                         "startDateTimeUtc": "2022-01-01T00:00:00Z",
    //                         "vesselSize": value,
    //                     },
    //                 },
    //             })
    //     });
    // });
    //
    // TODO figure out how to make it work when CoordinatesInput component
    // it('should be called when changing the coordinates', async () => {
    //     ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
    //
    //     render(<ActionControlNav action={actionMock}/>);
    //
    //     const field = screen.getByRole('coordinates')
    //     const value = ['-21.186389, 122.186389']
    //     fireEvent.change(field, {target: {value: value.join(', ')}});
    //
    //
    //         expect(mutateControlMock).toHaveBeenCalledWith({
    //             "variables": {
    //                 "controlAction": {
    //                     // actionId and missionId are undefined because useParam is not setup in the test run
    //                     "missionId": undefined,
    //                     "controlMethod": "AIR",
    //                     "endDateTimeUtc": "2022-01-01T01:00:00Z",
    //                     "startDateTimeUtc": "2022-01-01T00:00:00Z",
    //                     "latitude": value[0],
    //                     "longitude": value[1],
    //                 },
    //             },
    //         })
    // });
    // TODO figure out how to make it work when DateRangePicker component
    // it('should be called when changing the date', async () => {
    //     ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
    //
    //     render(<ActionControlNav action={actionMock}/>);
    //
    //     const field = screen.getByRole('ok')
    //     const value = ["2022-01-01T00:30:00Z", "2022-01-01T01:00:00Z"]
    //     fireEvent.change(field, {value});
    //
    //         expect(mutateControlMock).toHaveBeenCalledWith({
    //             "variables": {
    //                 "controlAction": {
    //                     // actionId and missionId are undefined because useParam is not setup in the test run
    //                     "missionId": undefined,
    //                     "controlMethod": "AIR",
    //                     "startDateTimeUtc": value[0],
    //                     "endDateTimeUtc": value[1],
    //                     "latitude": 123,
    //                     "longitude": 123,
    //
    //                 },
    //             },
    //         })
    // });
  })

  describe('The delete mutation', () => {
    it('should be called when changing clicking on the delete button', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      render(<ActionControlNav action={actionMock} />)
      const button = screen.getByTestId('deleteButton')
      fireEvent.click(button)
      expect(deleteControlMock).toHaveBeenCalled()
    })
  })

  describe('The controls', () => {
    it('should not show controls Gens de Mer for SAILING_LEISURE', async () => {
      const mock = { ...actionMock, data: { ...actionMock.data, vesselType: VesselTypeEnum.SAILING_LEISURE } }
      ;(useActionById as any).mockReturnValue(mockedQueryResult(mock as any, false))
      render(<ActionControlNav action={mock} />)
      const text = screen.queryByText('Contrôle administratif gens de mer')
      expect(text).toBeFalsy()
    })
    it('should show controls Gens de Mer for for other vessel types', async () => {
      const mock = { ...actionMock, data: { ...actionMock.data, vesselType: VesselTypeEnum.COMMERCIAL } }
      ;(useActionById as any).mockReturnValue(mockedQueryResult(mock as any, false))
      render(<ActionControlNav action={mock} />)
      const text = screen.getByText('Contrôle administratif gens de mer')
      expect(text).toBeInTheDocument()
    })
  })
})
