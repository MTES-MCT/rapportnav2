import { vi } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../../../test-utils.tsx'
import { Formik } from 'formik'
import MissionGeneralInformationCrewPam from '../mission-general-information-crew-pam.tsx'
import { MissionCrew } from '../../../../../../common/types/crew-type.ts'

// Mock the hook used by CrewAbsenceForm
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

const agent1 = {
  id: 1,
  firstName: 'John',
  lastName: 'Doe',
  services: []
}
const agent2 = {
  id: 2,
  firstName: 'Jane',
  lastName: 'Smith',
  services: []
}
const role1 = {
  id: '1',
  title: 'Commandant'
}
const role2 = {
  id: '2',
  title: 'Second'
}

vi.mock('../../../../../common/services/use-agent-roles.tsx', () => ({
  default: () => ({
    data: [role1, role2]
  })
}))

vi.mock('../../../../../common/services/use-agents.tsx', () => ({
  default: () => ({
    data: [agent1, agent2]
  })
}))

vi.mock('../../../../../common/services/use-agent-services.tsx', () => ({
  default: () => ({
    data: [
      {
        service: {
          id: 1,
          name: 'service1'
        },
        agents: [agent1, agent2]
      }
    ]
  })
}))

describe('MissionGeneralInformationCrewPam', () => {
  // Sample crew members for testing
  const sampleCrewMembers: MissionCrew[] = [
    {
      id: '1',
      agent: agent1,
      role: {
        id: 'role1',
        title: 'Captain'
      },
      comment: 'Test comment 1',
      absences: []
    },
    {
      id: '2',
      agent: agent2,
      role: {
        id: 'role2',
        title: 'Navigator'
      },
      comment: 'Test comment 2',
      absences: []
    }
  ]

  // Helper function to create mock field array
  const createMockFieldArray = (initialValues: MissionCrew[] = []) => ({
    form: {
      values: {
        crew: initialValues
      },
      setFieldValue: vi.fn()
    },
    remove: vi.fn()
  })

  const renderComponent = (props = {}) => {
    const defaultProps = {
      name: 'crew',
      missionId: 1,
      currentCrewList: sampleCrewMembers,
      fieldArray: createMockFieldArray(sampleCrewMembers)
    }

    return render(
      <Formik initialValues={{ crew: sampleCrewMembers }} onSubmit={vi.fn()}>
        <MissionGeneralInformationCrewPam {...defaultProps} {...props} />
      </Formik>
    )
  }

  it('renders empty state when no crew members', () => {
    renderComponent({
      currentCrewList: [],
      fieldArray: createMockFieldArray()
    })

    // Check empty state text
    expect(screen.getByText("Sélectionner votre bordée, pour importer votre liste d'équipage")).toBeInTheDocument()
  })

  it('renders the component with crew list', () => {
    renderComponent()

    // Check title
    expect(screen.getByText('❯ Équipage')).toBeInTheDocument()

    // Check crew member names
    expect(screen.getByText('John Doe')).toBeInTheDocument()
    expect(screen.getByText('Jane Smith')).toBeInTheDocument()

    // Check add member button
    expect(screen.getByText("Ajouter un membre d'équipage")).toBeInTheDocument()
  })

  it('opens additional crew form when adding a new member', async () => {
    renderComponent()

    // Click add member button
    const addMemberButton = screen.getByText("Ajouter un membre d'équipage")
    fireEvent.click(addMemberButton)

    // Check if crew form is opened
    expect(screen.getByTestId('crew-form')).toBeInTheDocument()
  })

  it('closes additional crew form when cancel is clicked', async () => {
    renderComponent()

    // Open add member form
    const addMemberButton = screen.getByText("Ajouter un membre d'équipage")
    fireEvent.click(addMemberButton)

    // Find and click cancel button
    const cancelButton = screen.getByText('Annuler')
    fireEvent.click(cancelButton)

    // Crew form should no longer be in the document
    expect(screen.queryByTestId('crew-form')).not.toBeInTheDocument()
  })

  it('opens crew absence form when editing an existing member', () => {
    renderComponent()

    // Find and click edit button for first crew member
    const editButtons = screen.getAllByTestId('edit-crew-member-icon')
    fireEvent.click(editButtons[0])

    // Check if crew form is opened with correct data
    expect(screen.getByTestId('crew-absence-form')).toBeInTheDocument()
  })

  it('calls remove when deleting an invited crew member', () => {
    const invitedCrewMember: MissionCrew = {
      id: '3',
      fullName: 'Invited Person',
      role: { id: 'role3', title: 'Observer' },
      absences: []
    }
    const mockFieldArray = createMockFieldArray([invitedCrewMember])

    render(
      <Formik initialValues={{ crew: [invitedCrewMember] }} onSubmit={vi.fn()}>
        <MissionGeneralInformationCrewPam
          name="crew"
          missionId={1}
          currentCrewList={[invitedCrewMember]}
          fieldArray={mockFieldArray as any}
        />
      </Formik>
    )

    const deleteButton = screen.getByTestId('delete-crew-member-icon')
    fireEvent.click(deleteButton)

    expect(mockFieldArray.remove).toHaveBeenCalledWith(0)
  })

  it('opens crew edit form for invited crew member', () => {
    const invitedCrewMember: MissionCrew = {
      id: '3',
      fullName: 'Invited Person',
      role: { id: 'role3', title: 'Observer' },
      absences: []
    }

    render(
      <Formik initialValues={{ crew: [invitedCrewMember] }} onSubmit={vi.fn()}>
        <MissionGeneralInformationCrewPam
          name="crew"
          missionId={1}
          currentCrewList={[invitedCrewMember]}
          fieldArray={createMockFieldArray([invitedCrewMember]) as any}
        />
      </Formik>
    )

    const editButton = screen.getByTestId('edit-crew-member-icon')
    fireEvent.click(editButton)

    expect(screen.getByTestId('crew-form')).toBeInTheDocument()
  })

  it('opens full mission absence form when unchecking checkbox', () => {
    renderComponent()

    const checkboxes = screen.getAllByRole('checkbox')
    fireEvent.click(checkboxes[0])

    expect(screen.getByTestId('crew-absence-form')).toBeInTheDocument()
    expect(screen.getByText('Déclaration de non participation')).toBeInTheDocument()
  })

  it('clears temporary absences when unchecking checkbox for crew member with existing absences', () => {
    const crewWithTemporaryAbsence: MissionCrew[] = [
      {
        id: '1',
        agent: agent1,
        role: { id: 'role1', title: 'Captain' },
        absences: [{ id: 1, isAbsentFullMission: false, startDate: '2024-01-01', endDate: '2024-01-02' }]
      }
    ]
    const mockFieldArray = createMockFieldArray(crewWithTemporaryAbsence)

    render(
      <Formik initialValues={{ crew: crewWithTemporaryAbsence }} onSubmit={vi.fn()}>
        <MissionGeneralInformationCrewPam
          name="crew"
          missionId={1}
          currentCrewList={crewWithTemporaryAbsence}
          fieldArray={mockFieldArray as any}
        />
      </Formik>
    )

    // Checkbox is checked (crew member has temporary absence but is present for part of mission)
    const checkbox = screen.getByRole('checkbox')
    // Uncheck the checkbox to declare full mission absence
    fireEvent.click(checkbox)

    // Existing temporary absences should be cleared before opening the full mission absence form
    expect(mockFieldArray.form.setFieldValue).toHaveBeenCalledWith('crew.0.absences', [])
    expect(screen.getByTestId('crew-absence-form')).toBeInTheDocument()
  })

  it('clears absences when checking checkbox for absent crew member', () => {
    const crewWithAbsence: MissionCrew[] = [
      {
        id: '1',
        agent: agent1,
        role: { id: 'role1', title: 'Captain' },
        absences: [{ id: 1, isAbsentFullMission: true }]
      }
    ]
    const mockFieldArray = createMockFieldArray(crewWithAbsence)

    render(
      <Formik initialValues={{ crew: crewWithAbsence }} onSubmit={vi.fn()}>
        <MissionGeneralInformationCrewPam
          name="crew"
          missionId={1}
          currentCrewList={crewWithAbsence}
          fieldArray={mockFieldArray as any}
        />
      </Formik>
    )

    const checkbox = screen.getByRole('checkbox')
    fireEvent.click(checkbox)

    expect(mockFieldArray.form.setFieldValue).toHaveBeenCalledWith('crew.0.absences', [])
  })

  it('displays crew member roles', () => {
    renderComponent()

    expect(screen.getByText('Captain')).toBeInTheDocument()
    expect(screen.getByText('Navigator')).toBeInTheDocument()
  })

  it('closes absence form when close button is clicked', () => {
    renderComponent()

    // Open absence form
    const editButtons = screen.getAllByTestId('edit-crew-member-icon')
    fireEvent.click(editButtons[0])

    expect(screen.getByTestId('crew-absence-form')).toBeInTheDocument()

    // Close absence form
    const closeButton = screen.getByTestId('close-crew-form-icon')
    fireEvent.click(closeButton)

    expect(screen.queryByTestId('crew-absence-form')).not.toBeInTheDocument()
  })
})
