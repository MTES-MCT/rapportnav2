import { vi } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../../../test-utils.tsx'
import { Formik } from 'formik'
import MissionGeneralInformationCrewPamAbsenceForm from '../mission-general-information-crew-pam-absence-form'
import { MissionCrewAbsenceType } from '../../../../../../common/types/crew-type.ts'

// Only mock the hook that has external dependencies
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
    handleSubmit: vi.fn()
  })
}))

const mockHandleClose = vi.fn()

const defaultInitialValues = {
  crew: [
    {
      id: '1',
      agent: { id: 1, firstName: 'Jean', lastName: 'Dupont', services: [] },
      role: { id: '1', title: 'Commandant' },
      absences: []
    },
    {
      id: '2',
      agent: { id: 2, firstName: 'Marie', lastName: 'Martin', services: [] },
      role: { id: '2', title: 'Second' },
      absences: []
    }
  ]
}

const renderComponent = (props = {}, initialValues = defaultInitialValues) => {
  const defaultProps = {
    crewIndex: 0,
    absenceType: MissionCrewAbsenceType.TEMPORARY,
    handleClose: mockHandleClose
  }

  return render(
    <Formik initialValues={initialValues} onSubmit={vi.fn()}>
      <MissionGeneralInformationCrewPamAbsenceForm {...defaultProps} {...props} />
    </Formik>
  )
}

describe('MissionGeneralInformationCrewPamAbsenceForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('rendering', () => {
    it('renders the dialog', () => {
      renderComponent()
      expect(screen.getByTestId('crew-absence-form')).toBeInTheDocument()
    })

    it('renders the close button', () => {
      renderComponent()
      expect(screen.getByTestId('close-crew-form-icon')).toBeInTheDocument()
    })

    it('returns null when crew at index does not exist', () => {
      const { container } = renderComponent({ crewIndex: 99 })
      expect(container.querySelector('[data-testid="crew-absence-form"]')).not.toBeInTheDocument()
    })

    it('returns null when crew array is empty', () => {
      const { container } = renderComponent({ crewIndex: 0 }, { crew: [] })
      expect(container.querySelector('[data-testid="crew-absence-form"]')).not.toBeInTheDocument()
    })

    it('returns null when crew is undefined', () => {
      const { container } = renderComponent({ crewIndex: 0 }, { crew: undefined } as any)
      expect(container.querySelector('[data-testid="crew-absence-form"]')).not.toBeInTheDocument()
    })
  })

  describe('title based on absenceType', () => {
    it('displays "Ajouter une absence temporaire" for TEMPORARY absence type', async () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY })
      expect(screen.queryAllByText('Ajouter une absence temporaire')).not.toBeNull()
    })

    it('displays "Déclaration de non participation" for FULL_MISSION absence type', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.FULL_MISSION })
      expect(screen.getByText('Déclaration de non participation')).toBeInTheDocument()
    })
  })

  describe('CrewAbsenceForm integration', () => {
    it('renders the reason select field from CrewAbsenceForm', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY })
      expect(screen.getByText('Motif')).toBeInTheDocument()
    })

    it('renders add button for temporary absence', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.TEMPORARY })
      expect(screen.queryAllByText('Ajouter une absence temporaire')).not.toBeNull()
    })

    it('renders validation button for full mission absence', () => {
      renderComponent({ absenceType: MissionCrewAbsenceType.FULL_MISSION })
      expect(screen.getByText('Valider l’absence sur toute la mission')).toBeInTheDocument()
    })
  })

  describe('close button interaction', () => {
    it('calls handleClose when close button is clicked', () => {
      renderComponent()
      const closeButton = screen.getByTestId('close-crew-form-icon')
      fireEvent.click(closeButton)
      expect(mockHandleClose).toHaveBeenCalled()
    })

    it('calls handleClose only once per click', () => {
      renderComponent()
      const closeButton = screen.getByTestId('close-crew-form-icon')
      fireEvent.click(closeButton)
      expect(mockHandleClose).toHaveBeenCalledTimes(1)
    })
  })

  describe('with different crew indices', () => {
    it('renders when crewIndex is 0', () => {
      renderComponent({ crewIndex: 0 })
      expect(screen.getByTestId('crew-absence-form')).toBeInTheDocument()
    })

    it('renders when crewIndex is 1', () => {
      renderComponent({ crewIndex: 1 })
      expect(screen.getByTestId('crew-absence-form')).toBeInTheDocument()
    })
  })
})
