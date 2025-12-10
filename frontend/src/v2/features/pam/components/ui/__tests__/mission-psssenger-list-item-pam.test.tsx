import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import { describe, it, expect } from 'vitest'
import MissionCrewListItemPam from '../mission-passenger-list-item-pam.tsx'
import { MissionPassenger, PASSENGER_OPTIONS } from '../../../../common/types/passenger-type.ts'

describe('MissionCrewListItemPam', () => {
  const passenger: MissionPassenger = {
    id: '123',
    fullName: 'John Doe',
    organization: PASSENGER_OPTIONS[0].value,
    isIntern: false,
    startDate: '2023-12-01T10:00:00Z',
    endDate: '2023-12-02T10:00:00Z'
  }

  const renderComponent = (override = {}) => {
    const props = {
      index: 0,
      name: 'passengers',
      passenger,
      handleEdit: vi.fn(),
      handleDelete: vi.fn(),
      ...override
    }

    return {
      ...render(<MissionCrewListItemPam {...props} />),
      props
    }
  }
  it('renders the passenger fullName', () => {
    renderComponent()
    expect(screen.getByTestId('passenger')).toHaveTextContent('John Doe')
  })

  it('renders the passenger organization label from PASSENGER_OPTIONS', () => {
    renderComponent()
    const expectedLabel = PASSENGER_OPTIONS.find(o => o.value === passenger.organization)?.label

    expect(screen.getAllByTestId('passenger-role')[0]).toHaveTextContent(expectedLabel!)
  })

  it('renders "Stagiaire" only when passenger.isIntern = true', () => {
    renderComponent({
      passenger: { ...passenger, isIntern: true }
    })
    expect(screen.getByTestId('passenger-intern')).toHaveTextContent('Stagiaire')
  })

  it('renders empty text if organization is not found in PASSENGER_OPTIONS', () => {
    renderComponent({
      passenger: { ...passenger, organization: 'NO_MATCH' }
    })

    // First role label
    expect(screen.getAllByTestId('passenger-role')[0]).toHaveTextContent('')
  })

  it('calls handleEdit with index when clicking edit button', () => {
    const { props } = renderComponent()

    fireEvent.click(screen.getByTestId('edit-crew-member-icon'))
    expect(props.handleEdit).toHaveBeenCalledWith(0)
  })

  it('calls handleDelete with index when clicking delete button', () => {
    const { props } = renderComponent()

    fireEvent.click(screen.getByTestId('delete-crew-member-icon'))
    expect(props.handleDelete).toHaveBeenCalledWith(0)
  })
})
