import { vi } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../../../test-utils.tsx'
import TemporaryAbsenceItemForm from '../crew-temporary-absence-form-item'
import { MissionCrewAbsence } from '../../../../../../common/types/crew-type.ts'
import { FieldProps } from 'formik'

const mockHandleSubmit = vi.fn()

// Mock the useMissionCrewAbsenceForm hook
vi.mock('../../../../../hooks/use-crew-absence.tsx', () => ({
  useMissionCrewAbsenceForm: () => ({
    initValue: {
      id: undefined,
      startDate: undefined,
      endDate: undefined,
      reason: undefined,
      isAbsentFullMission: false,
      dates: [undefined, undefined]
    },
    handleSubmit: mockHandleSubmit
  })
}))

const mockOnRemove = vi.fn()
const mockOnCommit = vi.fn()

const createFieldFormik = (value: MissionCrewAbsence = {}): FieldProps<MissionCrewAbsence> =>
  ({
    field: {
      name: 'crew.0.absences.0',
      value: {
        id: undefined,
        startDate: undefined,
        endDate: undefined,
        reason: undefined,
        isAbsentFullMission: false,
        ...value
      }
    },
    form: {
      setFieldValue: vi.fn(),
      values: {},
      errors: {},
      touched: {}
    },
    meta: {
      value: {},
      touched: false
    }
  }) as unknown as FieldProps<MissionCrewAbsence>

const renderComponent = (props = {}) => {
  const defaultProps = {
    name: 'crew.0.absences.0',
    fieldFormik: createFieldFormik(),
    onRemove: mockOnRemove,
    onCommit: mockOnCommit,
    showCloseButton: false
  }

  return render(<TemporaryAbsenceItemForm {...defaultProps} {...props} />)
}

describe('TemporaryAbsenceItemForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders the form with reason select', () => {
    renderComponent()
    expect(screen.getByText('Motif')).toBeInTheDocument()
  })

  it('renders the date range picker', () => {
    renderComponent()
    expect(screen.getByText('Dates de début et de fin (utc)')).toBeInTheDocument()
  })

  it('renders delete button when showCloseButton is true', () => {
    renderComponent({ showCloseButton: true })
    expect(screen.getByTestId('delete-absence')).toBeInTheDocument()
  })

  it('calls onRemove when delete button is clicked', () => {
    renderComponent({ showCloseButton: true })
    const deleteButton = screen.getByTestId('delete-absence')
    fireEvent.click(deleteButton)
    expect(mockOnRemove).toHaveBeenCalled()
  })

  it('renders delete button with correct title', () => {
    renderComponent({ showCloseButton: true })
    const deleteButton = screen.getByTestId('delete-absence')
    expect(deleteButton).toHaveAttribute('title', 'Supprimer une absence')
  })
})
