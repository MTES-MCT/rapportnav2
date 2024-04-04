import { render, screen } from '../../../test-utils'
import { ActionTypeEnum, MissionSourceEnum } from '../../../types/env-mission-types.ts'
import { Action, ActionFreeNote, ActionStatusType } from '../../../types/action-types.ts'
import { vi } from 'vitest'
import useActionById from './use-action-by-id.tsx'
import { GraphQLError } from 'graphql/error'
import { fireEvent } from '../../../test-utils.tsx'
import ActionNoteForm from './action-note-form.tsx'

const mutateMock = vi.fn()
const deleteMock = vi.fn()
vi.mock('./use-action-by-id.tsx', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: vi.fn()
  }
})

vi.mock('../notes/use-add-update-note.tsx', () => ({
  default: () => [mutateMock]
}))

vi.mock('../notes/use-delete-note.tsx', () => ({
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
    startDateTimeUtc: '2022-01-01T00:00:00Z',
    observations: 'test'
  } as any as ActionFreeNote
}

// Set up the mock implementation for useActionById
const mockedQueryResult = (action: Action = actionMock as any, loading: boolean = false, error: any = undefined) => ({
  data: action,
  loading,
  error
})

describe('ActionNoteForm', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, true))
      render(<ActionNoteForm action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false, new GraphQLError('Error!')))
      render(<ActionNoteForm action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))
      render(<ActionNoteForm action={actionMock} />)
      expect(screen.getByText('Dupliquer')).toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      ;(useActionById as any).mockReturnValue({ ...mockedQueryResult(undefined, false), data: null })
      render(<ActionNoteForm action={actionMock} />)
      expect(screen.queryByTestId('action-note-form')).not.toBeInTheDocument()
    })
  })

  describe('The update mutation', () => {
    beforeEach(() => {
      mutateMock.mockClear()
    })
    it('should be called when changing observations', async () => {
      ;(useActionById as any).mockReturnValue(mockedQueryResult(actionMock as any, false))

      render(<ActionNoteForm action={actionMock} />)

      const field = screen.getByTestId('observations')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      expect(mutateMock).toHaveBeenCalledWith({
        variables: {
          freeNoteAction: {
            // actionId and missionId are undefined because useParam is not setup in the test run
            missionId: undefined,
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
      render(<ActionNoteForm action={actionMock} />)
      const button = screen.getByTestId('deleteButton')
      fireEvent.click(button)
      expect(deleteMock).toHaveBeenCalled()
    })
  })
})
