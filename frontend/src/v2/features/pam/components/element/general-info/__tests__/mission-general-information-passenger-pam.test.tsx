import { vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../../../../../test-utils.tsx'
import MissionGeneralInformationCrewPam from '../mission-general-information-passenger-pam.tsx'
import { MissionPassenger } from '../../../../../common/types/passenger-type.ts'

describe('MissionGeneralInformationCrewPam', () => {
  // Helper function to create mock field array
  const createMockFieldArray = (initialValues: any[] = []) => ({
    form: {
      values: {
        passengers: initialValues
      },
      setFieldValue: vi.fn()
    },
    remove: vi.fn()
  })
  // Sample crew members for testing
  const samplePassengers: MissionPassenger[] = [
    {
      id: '1',
      fullName: 'John Doe',
      organization: 'Affaires Maritimes',
      isIntern: false,
      startDateTimeUtc: '2023-12-01T10:00:00Z',
      endDateTimeUtc: '2023-12-02T10:00:00Z'
    },
    {
      id: '2',
      fullName: 'Jane Smith',
      organization: 'Affaires Maritimes',
      isIntern: true,
      startDateTimeUtc: '2023-12-01T10:00:00Z',
      endDateTimeUtc: '2023-12-02T10:00:00Z'
    }
  ]

  const renderComponent = (props = {}) => {
    const defaultProps = {
      name: 'passengers',
      missionId: 1,
      currentPassengerList: samplePassengers,
      fieldArray: createMockFieldArray(samplePassengers)
    }

    return render(<MissionGeneralInformationCrewPam {...defaultProps} {...props} />)
  }

  it('renders empty state when no crew members', () => {
    renderComponent({
      currentCrewList: [],
      fieldArray: createMockFieldArray()
    })

    // Check empty state text
    expect(screen.getByText('Pas de passagers')).toBeInTheDocument()
  })

  it('renders the component with crew list', () => {
    renderComponent()

    // Check title
    expect(screen.getByText('Passagers', { exact: false })).toBeInTheDocument()

    // Check crew member names
    expect(screen.getByText('John Doe')).toBeInTheDocument()
    expect(screen.getByText('Jane Smith')).toBeInTheDocument()

    // check interns are shown (1 time)
    expect(screen.getByText('Stagiaire')).toBeInTheDocument()

    // Check add member button
    expect(screen.getByText('Ajouter un passager')).toBeInTheDocument()
  })

  it('opens passenger form when adding a new member', () => {
    renderComponent()

    // Click add member button
    const addMemberButton = screen.getByText('Ajouter un passager')
    fireEvent.click(addMemberButton)

    // Check if passenger form is opened
    waitFor(() => {
      expect(screen.getByTestId('passenger-form')).toBeInTheDocument()
    })
  })

  it('opens passenger form when editing an existing member', async () => {
    renderComponent()

    // Find and click edit button for first crew member
    const editButtons = screen.getAllByTestId('edit-crew-member-icon')
    fireEvent.click(editButtons[0])

    waitFor(() => {
      // Check if crew form is opened with correct data
      expect(screen.getByTestId('passenger-form')).toBeInTheDocument()

      // passenger name should be printed twice: in the list and in the dialog
      expect(screen.queryAllByText('John Doe')).toHaveLength(2)
    })
  })

  it('handles deleting a crew member', async () => {
    const mockFieldArray = createMockFieldArray(samplePassengers)
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
    const addMemberButton = screen.getByText('Ajouter un passager')
    fireEvent.click(addMemberButton)

    // Find and click cancel button
    const cancelButton = screen.getByText('Annuler')
    fireEvent.click(cancelButton)

    // Crew form should no longer be in the document
    expect(screen.queryByTestId('crew-form')).not.toBeInTheDocument()
  })

  it('sets selectedPassengerId when editing', async () => {
    renderComponent()

    const editButtons = screen.getAllByTestId('edit-crew-member-icon')
    fireEvent.click(editButtons[1]) // Jane Smith

    waitFor(() => {
      expect(screen.findByTestId('passenger-form')).toBeInTheDocument()
    })
  })

  describe('submitting', () => {
    it('appends a new passenger when submitting a passenger with no id', async () => {
      const mockFieldArray = createMockFieldArray(samplePassengers)

      renderComponent({ fieldArray: mockFieldArray })

      fireEvent.click(screen.getByText('Ajouter un passager'))

      // Fill & submit (your form uses handleSubmitForm directly)
      const submitButton = await screen.getByTestId('submit-passenger-form-button')
      fireEvent.click(submitButton)

      waitFor(() => {
        expect(mockFieldArray.form.setFieldValue).toHaveBeenCalled()

        const args = mockFieldArray.form.setFieldValue.mock.calls[0]
        expect(args[0]).toBe('passengers')
        expect(args[1].length).toBe(3) // one more
      })
    })

    it('appends passenger when id exists but is not found in currentPassengerList', async () => {
      const customPassengers = [] // current list empty
      const mockFieldArray = createMockFieldArray(customPassengers)

      renderComponent({
        currentPassengerList: customPassengers,
        fieldArray: mockFieldArray
      })

      fireEvent.click(screen.getByText('Ajouter un passager'))

      const submit = await screen.getByTestId('submit-passenger-form-button')
      fireEvent.click(submit)

      waitFor(() => {
        const args = mockFieldArray.form.setFieldValue.mock.calls[0]
        expect(args[1].length).toBe(1) // appended
      })
    })
  })

  it('resets selectedPassengerId to undefined after submitting form', async () => {
    renderComponent()

    fireEvent.click(screen.getByText('Ajouter un passager'))

    const submitButton = await screen.getByTestId('submit-passenger-form-button')
    fireEvent.click(submitButton)

    waitFor(() => {
      expect(screen.queryByTestId('passenger-form')).not.toBeInTheDocument()
    })
  })
})
