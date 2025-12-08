import { vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../../../../../../test-utils.tsx'
import { Formik } from 'formik'
import { MissionPassenger } from '../../../../../../common/types/passenger-type.ts'
import MissionGeneralInformationPassengerPamForm from '../mission-general-information-passenger-pam-form.tsx'

const samplePassenger: MissionPassenger = {
  id: '1',
  fullName: 'John Doe',
  organization: 'Affaires Maritimes',
  isIntern: false,
  startDate: '2023-12-01',
  endDate: '2023-12-02'
}

describe('MissionGeneralInformationPassengerPamForm', () => {
  const mockHandleClose = vi.fn()
  const mockHandleSubmitForm = vi.fn()

  const renderComponent = (props = {}) => {
    const defaultProps = {
      passenger: samplePassenger,
      handleClose: mockHandleClose,
      handleSubmitForm: mockHandleSubmitForm
    }

    return render(
      <Formik initialValues={{}} onSubmit={vi.fn()}>
        <MissionGeneralInformationPassengerPamForm {...defaultProps} {...props} />
      </Formik>
    )
  }

  it('renders the component for adding a new passenger member', () => {
    renderComponent({ passenger: undefined })

    // Check title
    expect(screen.getByText('Ajout d’un passager')).toBeInTheDocument()

    // Check form elements
    expect(screen.getByText('Prénom, Nom')).toBeInTheDocument()
    expect(screen.getByText('Organisation')).toBeInTheDocument()
    expect(screen.getByText('Dates de début et de fin', { exact: false })).toBeInTheDocument()

    // Check buttons
    expect(screen.getByTestId('submit-passenger-form-button')).toHaveTextContent('Ajouter passager')
    expect(screen.getByText('Annuler')).toBeInTheDocument()
  })

  it('renders the component for updating an existing passenger', () => {
    renderComponent()

    // Check title
    expect(screen.getByText('Mise à jour d’un passager')).toBeInTheDocument()

    // Check form initial values
    const identityInput = screen.getByLabelText('Prénom, Nom')
    expect(identityInput.value).toEqual(samplePassenger.fullName)

    // Check buttons
    expect(screen.getByTestId('submit-passenger-form-button')).toHaveTextContent('Mettre à jour passager')
  })

  it('validates form submission with required fields', async () => {
    renderComponent({ passenger: undefined })

    // Try to submit without filling required fields
    const submitButton = screen.getByTestId('submit-passenger-form-button')
    fireEvent.click(submitButton)

    // Check validation errors
    expect(await screen.findByText('Nom requis.')).toBeInTheDocument()
    expect(await screen.findByText('Organisation requise.')).toBeInTheDocument()
    expect(screen.queryAllByText('Date requise')).toHaveLength(2)
  })

  it('submits the form successfully', async () => {
    renderComponent({ crewId: '1' })

    // Submit form
    const submitButton = screen.getByTestId('submit-passenger-form-button')
    fireEvent.click(submitButton)

    // Wait for form submission
    await waitFor(() => {
      expect(mockHandleSubmitForm).toHaveBeenCalledOnce()
      expect(mockHandleSubmitForm).toHaveBeenCalledWith(expect.objectContaining(samplePassenger))
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

  it('shows validation error messages when required fields are empty', async () => {
    renderComponent({ passenger: undefined })

    const submitButton = screen.getByTestId('submit-passenger-form-button')
    fireEvent.click(submitButton) // trigger submit

    expect(await screen.findByText('Nom requis.')).toBeInTheDocument()
    expect(await screen.findByText('Organisation requise.')).toBeInTheDocument()
    expect(screen.queryAllByText('Date requise')).toHaveLength(2)
  })

  it('shows validation error messages when end date before start date', async () => {
    renderComponent({
      passenger: {
        ...samplePassenger,
        ...{
          startDate: '2023-12-03',
          endDate: '2023-12-02'
        }
      }
    })

    const submitButton = screen.getByTestId('submit-passenger-form-button')
    fireEvent.click(submitButton) // trigger submit

    expect(
      await screen.findByText('La date de fin doit être antérieure ou égale à la date de début')
    ).toBeInTheDocument()
  })
})
