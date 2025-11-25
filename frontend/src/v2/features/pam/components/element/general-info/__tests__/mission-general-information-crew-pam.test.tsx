import { vi } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../../test-utils.tsx'
import MissionGeneralInformationCrewPam from '../mission-general-information-crew-pam.tsx'

const agent1 = {
  id: 1,
  firstName: 'John',
  lastName: 'Doe'
}
const agent2 = {
  id: 2,
  firstName: 'Jane',
  lastName: 'Smith'
}
const role1 = {
  id: 1,
  title: 'Commandant'
}
const role2 = {
  id: 2,
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
        agents: [
          {
            agent: agent1,
            role: role1
          },
          {
            agent: agent2,
            role: role2
          }
        ]
      }
    ]
  })
}))

describe('MissionGeneralInformationCrewPam', () => {
  // Helper function to create mock field array
  const createMockFieldArray = (initialValues = []) => ({
    form: {
      values: {
        crew: initialValues
      },
      setFieldValue: vi.fn()
    },
    remove: vi.fn()
  })
  // Sample crew members for testing
  const sampleCrewMembers = [
    {
      id: 1,
      agent: {
        id: 'agent1',
        firstName: 'John',
        lastName: 'Doe'
      },
      role: {
        id: 'role1',
        title: 'Captain'
      },
      comment: 'Test comment 1'
    },
    {
      id: 2,
      agent: {
        id: 'agent2',
        firstName: 'Jane',
        lastName: 'Smith'
      },
      role: {
        id: 'role2',
        title: 'Navigator'
      },
      comment: 'Test comment 2'
    }
  ]

  const renderComponent = (props = {}) => {
    const defaultProps = {
      name: 'crew',
      missionId: 1,
      currentCrewList: sampleCrewMembers,
      fieldArray: createMockFieldArray(sampleCrewMembers)
    }

    return render(<MissionGeneralInformationCrewPam {...defaultProps} {...props} />)
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

  it('opens crew form when adding a new member', async () => {
    renderComponent()

    // Click add member button
    const addMemberButton = screen.getByText("Ajouter un membre d'équipage")
    fireEvent.click(addMemberButton)

    // Check if crew form is opened
    expect(screen.getByTestId('crew-form')).toBeInTheDocument()
  })

  it('opens crew form when editing an existing member', async () => {
    renderComponent()

    // Find and click edit button for first crew member
    const editButtons = screen.getAllByTestId('edit-crew-member-icon')
    fireEvent.click(editButtons[0])

    // Check if crew form is opened with correct data
    expect(screen.getByTestId('crew-form')).toBeInTheDocument()
  })

  it('handles deleting a crew member', async () => {
    const mockFieldArray = createMockFieldArray(sampleCrewMembers)
    renderComponent({ fieldArray: mockFieldArray })

    // Find and click delete button for first crew member
    const deleteButtons = screen.getAllByTestId('delete-crew-member-icon')
    fireEvent.click(deleteButtons[0])

    // Verify remove method was called
    expect(mockFieldArray.remove).toHaveBeenCalledWith(0)
  })

  it('closes crew form when cancel is clicked', async () => {
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
})
