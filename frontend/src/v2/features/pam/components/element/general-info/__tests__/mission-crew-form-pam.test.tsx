import { vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../../../../../test-utils.tsx'
import MissionCrewFormPam from '../mission-crew-form-pam.tsx'
import { Formik } from 'formik'
import { MissionCrew } from '@common/types/crew-types.ts'

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

// Mock dependencies
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

vi.mock('../../../../../common/services/use-agent-roles.tsx', () => ({
  default: () => ({
    data: [role1, role2]
  })
}))

describe('MissionCrewFormPam', () => {
  const mockHandleClose = vi.fn()
  const mockHandleSubmitForm = vi.fn()
  const initialCrewList: MissionCrew[] = [
    {
      id: '1',
      agent: agent1,
      role: role1,
      comment: 'Test comment'
    }
  ]

  const renderComponent = (props = {}) => {
    const defaultProps = {
      crewId: undefined,
      crewList: initialCrewList,
      handleClose: mockHandleClose,
      handleSubmitForm: mockHandleSubmitForm
    }

    return render(
      <Formik initialValues={{}} onSubmit={vi.fn()}>
        <MissionCrewFormPam {...defaultProps} {...props} />
      </Formik>
    )
  }

  it('renders the component for adding a new crew member', () => {
    renderComponent({})

    // Check title
    expect(screen.getByText('Ajout d’un membre d’équipage du DCS')).toBeInTheDocument()

    // Check form elements
    expect(screen.getByText('Identité')).toBeInTheDocument()
    expect(screen.getByText('Fonction')).toBeInTheDocument()
    expect(screen.getByText('Commentaires (23 caractères max.)')).toBeInTheDocument()

    // Check buttons
    expect(screen.getByTestId('submit-crew-form-button')).toHaveTextContent('Ajouter un membre')
    expect(screen.getByText('Annuler')).toBeInTheDocument()
  })

  it('renders the component for updating an existing crew member', () => {
    renderComponent({ crewId: '1' })

    // Check title
    expect(screen.getByText('Mise à jour d’un membre d’équipage')).toBeInTheDocument()

    // Check form initial values
    const identityInput = screen.getByLabelText('Identité')
    expect(identityInput.textContent).toEqual(`${agent1.firstName} ${agent1.lastName}`)

    const functionInput = screen.getByLabelText('Fonction')
    expect(functionInput.textContent).toEqual(role1.title)

    const commentInput = screen.getByLabelText('Commentaires (23 caractères max.)')
    expect(commentInput).toHaveValue('Test comment')

    // Check buttons
    expect(screen.getByTestId('submit-crew-form-button')).toHaveTextContent('Mettre à jour un membre')
  })

  it('validates form submission with required fields', async () => {
    renderComponent()

    // Try to submit without filling required fields
    const submitButton = screen.getByTestId('submit-crew-form-button')
    fireEvent.click(submitButton)

    // Check validation errors
    expect(await screen.findByText('Identité requise.')).toBeInTheDocument()
    expect(await screen.findByText('Fonction requise.')).toBeInTheDocument()
  })

  it('submits the form successfully', async () => {
    renderComponent({ crewId: '1' })

    // Submit form
    const submitButton = screen.getByTestId('submit-crew-form-button')
    fireEvent.click(submitButton)

    // Wait for form submission
    await waitFor(() => {
      expect(mockHandleSubmitForm).toHaveBeenCalledOnce()
      expect(mockHandleSubmitForm).toHaveBeenCalledWith(
        expect.objectContaining({
          agent: expect.objectContaining({ id: agent1.id }),
          role: expect.objectContaining({ id: role1.id }),
          comment: 'Test comment'
        })
      )
    })
  })

  it('validates comment length', async () => {
    renderComponent({ crewId: '1' })

    // Try to submit with a comment longer than 23 characters
    const commentInput = screen.getByLabelText('Commentaires (23 caractères max.)')
    fireEvent.change(commentInput, {
      target: { value: 'This is a very long comment that exceeds the maximum length allowed' }
    })

    // Submit form
    const submitButton = screen.getByTestId('submit-crew-form-button')
    fireEvent.click(submitButton)

    // Check validation error
    await waitFor(() => {
      expect(screen.getByText('Maximum 23 caractères.')).toBeInTheDocument()
    })
  })

  it('closes the modal when close button is clicked', async () => {
    renderComponent()

    // Find and click close button
    const closeButton = screen.getByTestId('close-crew-form-icon')
    fireEvent.click(closeButton)

    // Check that handleClose was called
    expect(mockHandleClose).toHaveBeenCalledWith(false)
  })

  it('closes the modal when cancel button is clicked', async () => {
    renderComponent()

    // Find and click cancel button
    const cancelButton = screen.getByText('Annuler')
    fireEvent.click(cancelButton)

    // Check that handleClose was called
    expect(mockHandleClose).toHaveBeenCalledWith(false)
  })
})
