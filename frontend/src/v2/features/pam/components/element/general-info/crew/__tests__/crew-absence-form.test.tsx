import { vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../../../../../../test-utils.tsx'
import { Formik } from 'formik'
import { CrewAbsenceForm } from '../crew-absence-form'
import { MissionCrewAbsenceType, MissionCrewAbsenceReason } from '../../../../../../common/types/crew-type.ts'

const mockCrew = {
  id: '1',
  absences: []
}
// Mock the useMissionCrewAbsenceForm hook
vi.mock('../../../../../hooks/use-crew-absence.tsx', () => ({
  useMissionCrewAbsenceForm: () => ({
    initValue: {
      id: undefined,
      startDate: undefined,
      endDate: undefined,
      reason: undefined,
      isAbsentFullMission: false
    },
    handleSubmit: vi.fn()
  })
}))

const mockHandleClose = vi.fn()

const renderComponent = (props = {}, initialValues = {}) => {
  const defaultProps = {
    crew: mockCrew,
    crewIndex: 0,
    absenceType: MissionCrewAbsenceType.TEMPORARY,
    handleClose: mockHandleClose
  }

  const defaultInitialValues = {
    crew: [mockCrew]
  }

  return render(
    <Formik initialValues={{ ...defaultInitialValues, ...initialValues }} onSubmit={vi.fn()}>
      <CrewAbsenceForm {...defaultProps} {...props} />
    </Formik>
  )
}

describe('CrewAbsenceForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('Temporary absence mode', () => {
    it('renders the form for temporary absence', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY })
      expect(screen.getByText('Ajouter une absence temporaire')).toBeInTheDocument()
    })

    it('renders an empty form row when no absences exist', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY })
      expect(screen.getByText('Motif')).toBeInTheDocument()
    })

    it('shows add button when less than 3 absences', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY })
      expect(screen.getByText('Ajouter une absence temporaire')).toBeInTheDocument()
    })

    it('adds a new local absence row when add button is clicked', async () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY })

      const addButton = screen.getByText('Ajouter une absence temporaire')
      fireEvent.click(addButton)
      fireEvent.click(addButton)

      await waitFor(() => {
        // Should now have 2 "Motif" labels (one for initial empty form, one for new row)
        const motifLabels = screen.getAllByText('Motif')
        expect(motifLabels.length).toBe(2)
      })
    })

    it('hides add button when 3 absences exist', () => {
      const initialValues = {
        crew: [
          {
            id: '1',
            absences: [
              { id: 1, reason: MissionCrewAbsenceReason.SICK_LEAVE },
              { id: 2, reason: MissionCrewAbsenceReason.TRAINING },
              { id: 3, reason: MissionCrewAbsenceReason.HOLIDAYS }
            ]
          }
        ]
      }
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY }, initialValues)
      expect(screen.queryByText('Ajouter une absence temporaire')).not.toBeInTheDocument()
    })

    it('renders existing absences from formik context', () => {
      const initialValues = {
        crew: [
          {
            id: '1',
            absences: [{ id: 1, reason: MissionCrewAbsenceReason.SICK_LEAVE }]
          }
        ]
      }
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY }, initialValues)
      expect(screen.getByText('Motif')).toBeInTheDocument()
    })

    it('shows delete button when absences exist', () => {
      const initialValues = {
        crew: [
          {
            id: '1',
            absences: [{ id: 1, reason: MissionCrewAbsenceReason.SICK_LEAVE }]
          }
        ]
      }
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY }, initialValues)
      expect(screen.getByTestId('delete-absence')).toBeInTheDocument()
    })

    it('calls handleClose when last absence is removed', () => {
      const initialValues = {
        crew: [
          {
            id: '1',
            absences: [{ id: 1, reason: MissionCrewAbsenceReason.SICK_LEAVE }]
          }
        ]
      }
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY }, initialValues)

      const deleteButton = screen.getByTestId('delete-absence')
      fireEvent.click(deleteButton)

      expect(mockHandleClose).toHaveBeenCalled()
    })
  })

  describe('Full mission absence mode', () => {
    it('renders the form for full mission absence', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.FULL_MISSION })
      expect(screen.getByText('Valider l’absence sur toute la mission')).toBeInTheDocument()
    })

    it('does not show add button for full mission absence', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.FULL_MISSION })
      expect(screen.queryByText('Ajouter une absence temporaire')).not.toBeInTheDocument()
    })

    it('renders the reason select field', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.FULL_MISSION })
      expect(screen.getByText('Motif')).toBeInTheDocument()
    })
  })
})
