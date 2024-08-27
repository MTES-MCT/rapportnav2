import { vi } from 'vitest'
import { render, screen, waitFor } from '../../../../../../test-utils.tsx'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { ActionFreeNote, ActionStatusType } from '@common/types/action-types.ts'
import { GraphQLError } from 'graphql/error'
import { fireEvent } from '@testing-library/dom'
import ActionNoteForm from './action-note-form.tsx'
import * as useActionByIdModule from '@features/pam/mission/hooks/use-action-by-id'
import * as useAddModule from '@features/pam/mission/hooks/use-add-update-note'
import * as useDeleteModule from '@features/pam/mission/hooks/use-delete-note'

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
    startDateTimeUtc: '2022-01-01T00:00:00Z',
    observations: 'test'
  } as any as ActionFreeNote
}

describe('ActionNoteForm', () => {
  beforeEach(() => {
    vi.spyOn(useAddModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
    vi.spyOn(useDeleteModule, 'default').mockReturnValue([deleteMock, { error: undefined }])
  })
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: true, error: null })
      render(<ActionNoteForm action={actionMock} />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('renders error state', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({
        data: actionMock,
        loading: false,
        error: new GraphQLError('Error!')
      })
      render(<ActionNoteForm action={actionMock} />)
      expect(screen.getByText('error')).toBeInTheDocument()
    })

    test('renders data', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: null })
      render(<ActionNoteForm action={actionMock} />)
      expect(screen.getByText('Dupliquer')).toBeInTheDocument()
    })
    test('renders null when none above', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: undefined, loading: false, error: null })
      render(<ActionNoteForm action={actionMock} />)
      expect(screen.queryByTestId('action-note-form')).not.toBeInTheDocument()
    })
  })

  describe('The update mutation', () => {
    it('should be called when changing observations', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: null })

      render(<ActionNoteForm action={actionMock} />)

      const field = screen.getByTestId('observations')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)

      waitFor(() => {
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
  })

  describe('The delete mutation', () => {
    it('should be called when changing clicking on the delete button', async () => {
      vi.spyOn(useActionByIdModule, 'default').mockReturnValue({ data: actionMock, loading: false, error: null })
      render(<ActionNoteForm action={actionMock} />)
      const button = screen.getByTestId('deleteButton')
      fireEvent.click(button)
      expect(deleteMock).toHaveBeenCalled()
    })
  })
})
