import { render, screen } from '../../../../../../test-utils.tsx'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import ActionStatusForm from './action-status-form.tsx'
import { ActionControl, ActionStatusType } from '@common/types/action-types.ts'
import { ControlMethod } from '@common/types/control-types.ts'
import { vi } from 'vitest'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import * as useAddUpdateStatusModule from '@features/pam/mission/hooks/use-add-update-status'
import * as useDeleteStatusModule from '@features/pam/mission/hooks/use-delete-status'
import { GraphQLError } from 'graphql/error'
import { fireEvent } from '../../../../../../test-utils.tsx'

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

describe('ActionSta tusForm', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: true, error: null })
      render(<ActionStatusForm action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({
        data: actionMock,
        loading: false,
        error: new GraphQLError('Error!')
      })
      render(<ActionStatusForm action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })
      render(<ActionStatusForm action={actionMock} />)
      expect(screen.getByText('Dupliquer')).toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: undefined, loading: false, error: undefined })
      render(<ActionStatusForm action={actionMock} />)
      expect(screen.queryByTestId('action-status-form')).not.toBeInTheDocument()
    })
  })

  describe('The update mutation', () => {
    it('should be called when changing observations', async () => {
      const mutateMock = vi.fn()
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })
      vi.spyOn(useAddUpdateStatusModule, 'default').mockReturnValue([mutateMock])

      render(<ActionStatusForm action={actionMock} />)

      const field = screen.getByTestId('observations')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      expect(mutateMock).toHaveBeenCalledWith({
        variables: {
          statusAction: {
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
  })

  describe('The delete mutation', () => {
    it('should be called when changing clicking on the delete button', async () => {
      const deleteMock = vi.fn()
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: undefined })
      vi.spyOn(useDeleteStatusModule, 'default').mockReturnValue([deleteMock])
      render(<ActionStatusForm action={actionMock} />)
      const button = screen.getByTestId('deleteButton')
      fireEvent.click(button)
      expect(deleteMock).toHaveBeenCalled()
    })
  })
})
