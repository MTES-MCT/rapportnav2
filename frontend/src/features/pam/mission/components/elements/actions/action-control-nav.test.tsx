import { render, screen } from '../../../../../../test-utils.tsx'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import ActionControlNav from './action-control-nav.tsx'
import { Action, ActionBAAEMPermanence, ActionControl, ActionStatusType } from '@common/types/action-types.ts'
import { ControlMethod } from '@common/types/control-types.ts'
import { vi } from 'vitest'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import * as useAddModule from '@features/pam/mission/hooks/use-add-update-action-control'
import * as useDeleteModule from '@features/pam/mission/hooks/use-delete-action-control'
import { GraphQLError } from 'graphql/error'
import { fireEvent } from '../../../../../../test-utils.tsx'
import { VesselTypeEnum } from '@common/types/mission-types.ts'

const mutateMock = vi.fn()
const deleteMock = vi.fn()

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

describe('ActionControlNav', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: true, error: undefined })
      render(<ActionControlNav action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({
        data: actionMock,
        loading: false,
        error: new GraphQLError('Error!')
      })
      render(<ActionControlNav action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })
      render(<ActionControlNav action={actionMock} />)
      expect(screen.getByText('Dupliquer')).toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: undefined, loading: false, error: undefined })
      render(<ActionControlNav action={actionMock} />)
      expect(screen.queryByTestId('action-control-nav')).not.toBeInTheDocument()
    })
  })

  describe('The update mutation', () => {
    beforeEach(() => {
      vi.spyOn(useAddModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
      vi.spyOn(useDeleteModule, 'default').mockReturnValue([deleteMock, { error: undefined }])
    })
    it('should be called when changing observations', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })

      render(<ActionControlNav action={actionMock} />)

      const field = screen.getByTestId('observations')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      expect(mutateMock).toHaveBeenCalledWith({
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
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })

      render(<ActionControlNav action={actionMock} />)

      const field = screen.getByTestId('identityControlledPerson')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      expect(mutateMock).toHaveBeenCalledWith({
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
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })

      render(<ActionControlNav action={actionMock} />)

      const field = screen.getByTestId('vesselIdentifier')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      expect(mutateMock).toHaveBeenCalledWith({
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
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })
      render(<ActionControlNav action={actionMock} />)
      const button = screen.getByTestId('deleteButton')
      fireEvent.click(button)
      expect(deleteMock).toHaveBeenCalled()
    })
  })

  describe('The controls', () => {
    it('should not show controls Gens de Mer for SAILING_LEISURE', async () => {
      const mock = { ...actionMock, data: { ...actionMock.data, vesselType: VesselTypeEnum.SAILING_LEISURE } }
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: mock, loading: false, error: undefined })
      render(<ActionControlNav action={mock} />)
      const text = screen.queryByText('Contrôle administratif gens de mer')
      expect(text).toBeFalsy()
    })
    it('should show controls Gens de Mer for for other vessel types', async () => {
      const mock = { ...actionMock, data: { ...actionMock.data, vesselType: VesselTypeEnum.COMMERCIAL } }
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: mock, loading: false, error: undefined })
      render(<ActionControlNav action={mock} />)
      const text = screen.getByText('Contrôle administratif gens de mer')
      expect(text).toBeInTheDocument()
    })
  })
})
