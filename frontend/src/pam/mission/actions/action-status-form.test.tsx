import { render, screen } from '../../../test-utils'
import { ActionTypeEnum, MissionSourceEnum } from '../../../types/env-mission-types.ts'
import ActionStatusForm from './action-status-form.tsx'
import { Action, ActionControl, ActionStatusType } from '../../../types/action-types.ts'
import { ControlMethod } from '../../../types/control-types.ts'
import { vi } from 'vitest'
import useActionById from './use-action-by-id.tsx'
import { GraphQLError } from 'graphql/error'
import { fireEvent } from '../../../test-utils.tsx'

const mutateMock = vi.fn()
const deleteMock = vi.fn()
vi.mock('./use-action-by-id.tsx', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: vi.fn()
  }
})

vi.mock('../status/use-add-update-status.tsx', () => ({
  default: () => [mutateMock]
}))

vi.mock('../status/use-delete-status.tsx', () => ({
  default: () => [deleteMock]
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

describe('ActionStatusForm', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, true))
      render(<ActionStatusForm action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false, new GraphQLError('Error!')))
      render(<ActionStatusForm action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      render(<ActionStatusForm action={actionMock} />)
      expect(screen.getByText('Dupliquer')).toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      ;(useActionById as any).mockReturnValue({ ...mockedQueryResult(undefined, false), data: null })
      render(<ActionStatusForm action={actionMock} />)
      expect(screen.queryByTestId('action-status-form')).not.toBeInTheDocument()
    })
  })

  describe('The update mutation', () => {
    beforeEach(() => {
      mutateMock.mockClear()
    })
    it('should be called when changing observations', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))

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
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      render(<ActionStatusForm action={actionMock} />)
      const button = screen.getByTestId('deleteButton')
      fireEvent.click(button)
      expect(deleteMock).toHaveBeenCalled()
    })
  })
})
