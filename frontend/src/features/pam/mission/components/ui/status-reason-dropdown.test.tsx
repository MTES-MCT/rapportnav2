import { render, screen } from '../../../../../test-utils.tsx'
import StatusReasonDropdown from './status-reason-dropdown.tsx'
import { ActionStatusType } from '../../../../common/types/action-types.ts'

describe('StatusReasonDropdown', () => {
  test('renders not render the dropdown for NAVIGATING', () => {
    const { container } = render(
      <StatusReasonDropdown actionType={ActionStatusType.NAVIGATING} value={undefined} onSelect={vi.fn()} />
    )
    expect(container.firstChild).toBeNull()
  })
  test('renders not render the dropdown for ANCHORED', () => {
    const { container } = render(
      <StatusReasonDropdown actionType={ActionStatusType.ANCHORED} value={undefined} onSelect={vi.fn()} />
    )
    expect(container.firstChild).toBeNull()
  })
  test('renders not render the dropdown for UNKNOWN', () => {
    const { container } = render(
      <StatusReasonDropdown actionType={ActionStatusType.UNKNOWN} value={undefined} onSelect={vi.fn()} />
    )
    expect(container.firstChild).toBeNull()
  })

  test('renders render the dropdown for DOCKED', () => {
    render(<StatusReasonDropdown actionType={ActionStatusType.DOCKED} value={undefined} onSelect={vi.fn()} />)
    expect(screen.getByText('Motif')).toBeInTheDocument()
  })
  test('renders render the dropdown for UNAVAILABLE', () => {
    render(<StatusReasonDropdown actionType={ActionStatusType.UNAVAILABLE} value={undefined} onSelect={vi.fn()} />)
    expect(screen.getByText('Motif')).toBeInTheDocument()
  })
})
