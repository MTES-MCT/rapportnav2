import { vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../../../../../../test-utils.tsx'
import { FullMissionAbsenceForm } from '../crew-full-mission-absence-form'
import { MissionCrewAbsence, MissionCrewAbsenceReason } from '../../../../../../common/types/crew-type.ts'
import { FieldProps } from 'formik'

const mockHandleSubmit = vi.hoisted(() => vi.fn())

const mockCrew = {
  id: '1',
  absences: [],
  agent: {
    id: 1,
    firstName: 'Jean',
    lastName: 'Dupont',
    services: []
  },
  role: {
    id: 'captain',
    title: 'Commandant'
  }
}

// Mock the useMissionCrewAbsenceForm hook
vi.mock('../../../../../hooks/use-crew-absence.tsx', () => ({
  useMissionCrewAbsenceForm: (_name: string, fieldFormik: any) => ({
    initValue: {
      id: fieldFormik?.field?.value?.id ?? undefined,
      startDate: fieldFormik?.field?.value?.startDate ?? undefined,
      endDate: fieldFormik?.field?.value?.endDate ?? undefined,
      reason: fieldFormik?.field?.value?.reason ?? undefined,
      isAbsentFullMission: fieldFormik?.field?.value?.isAbsentFullMission ?? false,
      dates: [undefined, undefined]
    },
    validationSchema: {},
    handleSubmit: mockHandleSubmit
  })
}))

const mockHandleClose = vi.fn()

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
    crew: mockCrew,
    name: 'crew.0.absences.0',
    fieldFormik: createFieldFormik(),
    handleClose: mockHandleClose
  }

  return render(<FullMissionAbsenceForm {...defaultProps} {...props} />)
}

describe('FullMissionAbsenceForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders the form with reason select', () => {
    renderComponent()
    expect(screen.getByText('Motif')).toBeInTheDocument()
  })

  it('renders the submit button', () => {
    renderComponent()
    expect(screen.getByText('Valider l’absence sur toute la mission')).toBeInTheDocument()
  })

  it('renders submit button as disabled when no reason is selected', () => {
    renderComponent()
    const submitButton = screen.getByText('Valider l’absence sur toute la mission').parentNode
    expect(submitButton).toBeDisabled()
  })

  it('calls handleSubmit with isAbsentFullMission true when form is submitted', async () => {
    const fieldFormik = createFieldFormik({ reason: MissionCrewAbsenceReason.SICK_LEAVE })
    renderComponent({ fieldFormik })

    const submitButton = screen.getByText('Valider l’absence sur toute la mission')
    fireEvent.click(submitButton)

    await waitFor(() => {
      expect(mockHandleSubmit).toHaveBeenCalledWith(
        expect.objectContaining({
          isAbsentFullMission: true
        })
      )
    })
  })

  it('calls handleClose after successful submission', async () => {
    const fieldFormik = createFieldFormik({ reason: MissionCrewAbsenceReason.TRAINING })
    renderComponent({ fieldFormik })

    const submitButton = screen.getByText('Valider l’absence sur toute la mission')
    fireEvent.click(submitButton)

    await waitFor(() => {
      expect(mockHandleClose).toHaveBeenCalled()
    })
  })
})
