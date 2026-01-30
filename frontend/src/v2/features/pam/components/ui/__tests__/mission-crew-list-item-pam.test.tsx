import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import { describe, it, expect, vi } from 'vitest'
import MissionCrewListItemPam from '../mission-crew-list-item-pam'
import { MissionCrew, MissionCrewAbsenceType, MissionCrewAbsenceReason } from '../../../../common/types/crew-type.ts'

const mockHandleEditAbsence = vi.fn()
const mockHandleEditCrew = vi.fn()
const mockHandleDelete = vi.fn()
const mockOnToggleCheckbox = vi.fn()

const defaultProps = {
  index: 0,
  name: 'test-crew',
  handleEditAbsence: mockHandleEditAbsence,
  handleEditCrew: mockHandleEditCrew,
  handleDelete: mockHandleDelete,
  onToggleCheckbox: mockOnToggleCheckbox
}

const standardCrewMember: MissionCrew = {
  id: '1',
  agent: {
    id: 1,
    firstName: 'Jean',
    lastName: 'Dupont',
    services: []
  },
  role: {
    id: 'captain',
    title: 'Commandant'
  },
  absences: []
}

const invitedCrewMember: MissionCrew = {
  id: '2',
  fullName: 'Marie Martin',
  comment: 'Invité externe',
  role: {
    id: 'observer',
    title: 'Observateur'
  },
  absences: []
}

describe('MissionCrewListItemPam', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('Standard crew member (with agent)', () => {
    it('renders the crew member name and role', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={standardCrewMember} />)
      expect(screen.getByText('Jean Dupont')).toBeInTheDocument()
      expect(screen.getByText('Commandant')).toBeInTheDocument()
    })

    it('renders checkbox as checked when not absent full mission', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={standardCrewMember} isAbsentFullMission={false} />)
      const checkbox = screen.getByRole('checkbox')
      expect(checkbox).toBeChecked()
    })

    it('renders checkbox as unchecked when absent full mission', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={standardCrewMember} isAbsentFullMission={true} />)
      const checkbox = screen.getByRole('checkbox')
      expect(checkbox).not.toBeChecked()
    })

    it('calls onToggleCheckbox when checkbox is clicked', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={standardCrewMember} isAbsentFullMission={false} />)
      const checkbox = screen.getByRole('checkbox')
      fireEvent.click(checkbox)
      expect(mockOnToggleCheckbox).toHaveBeenCalledWith(0, false)
    })

    it('calls handleEditAbsence with TEMPORARY type when not absent full mission', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={standardCrewMember} isAbsentFullMission={false} />)
      const editButton = screen.getByTestId('edit-crew-member-icon')
      fireEvent.click(editButton)
      expect(mockHandleEditAbsence).toHaveBeenCalledWith(0, MissionCrewAbsenceType.TEMPORARY)
    })

    it('calls handleEditAbsence with FULL_MISSION type when absent full mission', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={standardCrewMember} isAbsentFullMission={true} />)
      const editButton = screen.getByTestId('edit-crew-member-icon')
      fireEvent.click(editButton)
      expect(mockHandleEditAbsence).toHaveBeenCalledWith(0, MissionCrewAbsenceType.FULL_MISSION)
    })

    it('renders absence tag inline when crew member has exactly 1 absence', () => {
      const crewWithOneAbsence: MissionCrew = {
        ...standardCrewMember,
        absences: [
          {
            id: 1,
            isAbsentFullMission: true,
            reason: MissionCrewAbsenceReason.SICK_LEAVE
          }
        ]
      }
      render(<MissionCrewListItemPam {...defaultProps} crewMember={crewWithOneAbsence} />)
      expect(screen.getByText('Arrêt maladie')).toBeInTheDocument()
      // Should only render once (inline), not in a second row
      expect(screen.getAllByText('Arrêt maladie')).toHaveLength(1)
    })

    it('renders absence tags in a second row when crew member has 2 or more absences', () => {
      const crewWithMultipleAbsences: MissionCrew = {
        ...standardCrewMember,
        absences: [
          {
            id: 1,
            isAbsentFullMission: false,
            reason: MissionCrewAbsenceReason.SICK_LEAVE,
            startDate: '2024-01-01',
            endDate: '2024-01-02'
          },
          {
            id: 2,
            isAbsentFullMission: false,
            reason: MissionCrewAbsenceReason.TRAINING,
            startDate: '2024-01-10',
            endDate: '2024-01-12'
          }
        ]
      }
      render(<MissionCrewListItemPam {...defaultProps} crewMember={crewWithMultipleAbsences} />)
      // Both absence tags should be rendered in the second row
      expect(screen.getByText('Arrêt maladie - abs 2j')).toBeInTheDocument()
      expect(screen.getByText('Formation - abs 3j')).toBeInTheDocument()
    })

    it('does not render second row when crew member has only 1 absence', () => {
      const crewWithOneAbsence: MissionCrew = {
        ...standardCrewMember,
        absences: [
          {
            id: 1,
            isAbsentFullMission: false,
            reason: MissionCrewAbsenceReason.SICK_LEAVE
          }
        ]
      }
      const { container } = render(<MissionCrewListItemPam {...defaultProps} crewMember={crewWithOneAbsence} />)
      // The second row has a Stack with direction="row" and spacing="1rem"
      // When there's only 1 absence, there should be only one FlexboxGrid (not the second row)
      const flexboxGrids = container.querySelectorAll('.rs-flex-box-grid')
      // Should have 2 FlexboxGrids: outer one and the inner one for inline absence, but NOT the second row
      expect(flexboxGrids.length).toBeLessThan(4)
    })

    it('renders all absence tags in second row when crew member has 3 absences', () => {
      const crewWithThreeAbsences: MissionCrew = {
        ...standardCrewMember,
        absences: [
          {
            id: 1,
            isAbsentFullMission: false,
            reason: MissionCrewAbsenceReason.SICK_LEAVE
          },
          {
            id: 2,
            isAbsentFullMission: false,
            reason: MissionCrewAbsenceReason.TRAINING
          },
          {
            id: 3,
            isAbsentFullMission: false,
            reason: MissionCrewAbsenceReason.HOLIDAYS
          }
        ]
      }
      render(<MissionCrewListItemPam {...defaultProps} crewMember={crewWithThreeAbsences} />)
      expect(screen.getByText('Arrêt maladie', { exact: false })).toBeInTheDocument()
      expect(screen.getByText('Formation', { exact: false })).toBeInTheDocument()
      expect(screen.getByText('Congés', { exact: false })).toBeInTheDocument()
    })

    it('matches snapshot', () => {
      const { container } = render(
        <MissionCrewListItemPam {...defaultProps} crewMember={standardCrewMember} isAbsentFullMission={false} />
      )
      expect(container).toMatchSnapshot()
    })
  })

  describe('Invited crew member (without agent)', () => {
    it('renders the full name and role', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={invitedCrewMember} />)
      expect(screen.getByText('Marie Martin')).toBeInTheDocument()
      expect(screen.getByText('Observateur')).toBeInTheDocument()
    })

    it('renders the comment', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={invitedCrewMember} />)
      expect(screen.getByText('Invité externe')).toBeInTheDocument()
    })

    it('calls handleEditCrew when edit button is clicked', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={invitedCrewMember} />)
      const editButton = screen.getByTestId('edit-crew-member-icon')
      fireEvent.click(editButton)
      expect(mockHandleEditCrew).toHaveBeenCalledWith(0)
    })

    it('calls handleDelete when delete button is clicked', () => {
      render(<MissionCrewListItemPam {...defaultProps} crewMember={invitedCrewMember} />)
      const deleteButton = screen.getByTestId('delete-crew-member-icon')
      fireEvent.click(deleteButton)
      expect(mockHandleDelete).toHaveBeenCalledWith(0)
    })

    it('matches snapshot', () => {
      const { container } = render(<MissionCrewListItemPam {...defaultProps} crewMember={invitedCrewMember} />)
      expect(container).toMatchSnapshot()
    })
  })
})
