import { render, fireEvent } from '../../../../../../test-utils.tsx'
import OnlineToggle from '../online-toggle.tsx'
import { useOnlineManager } from '../../../hooks/use-online-manager.tsx'
import { THEME } from '@mtes-mct/monitor-ui'
import { vi } from 'vitest'

// Mock the useOnlineManager hook
vi.mock('../../../hooks/use-online-manager', () => ({
  useOnlineManager: vi.fn()
}))

describe('OnlineToggle', () => {
  it('renders correctly when online', () => {
    // Mock the hook to return online state
    vi.mocked(useOnlineManager).mockReturnValue({
      isOnline: true,
      isOffline: false,
      setOnline: vi.fn()
    })

    const { getByText, getByRole } = render(<OnlineToggle />)

    // Check online text is bold and colored green
    const onlineText = getByText('En ligne')
    expect(onlineText).toHaveStyle(`color: ${THEME.color.mediumSeaGreen}`)
    expect(onlineText).toHaveStyle('font-weight: 700')

    // Check offline text is normal and colored light gray
    const offlineText = getByText('Hors ligne')
    expect(offlineText).toHaveStyle(`color: ${THEME.color.lightGray}`)
    expect(offlineText).toHaveStyle('font-weight: 400')

    // Check toggle is checked
    const toggle = getByRole('switch')
    expect(toggle).toBeChecked()
  })

  it('renders correctly when offline', () => {
    // Mock the hook to return offline state
    vi.mocked(useOnlineManager).mockReturnValue({
      isOnline: false,
      isOffline: true,
      setOnline: vi.fn()
    })

    const { getByText, getByRole } = render(<OnlineToggle />)

    // Check online text is normal and colored light gray
    const onlineText = getByText('En ligne')
    expect(onlineText).toHaveStyle(`color: ${THEME.color.lightGray}`)
    expect(onlineText).toHaveStyle('font-weight: 400')

    // Check offline text is bold and colored red
    const offlineText = getByText('Hors ligne')
    expect(offlineText).toHaveStyle(`color: ${THEME.color.maximumRed}`)
    expect(offlineText).toHaveStyle('font-weight: 700')

    // Check toggle is unchecked
    const toggle = getByRole('switch')
    expect(toggle).not.toBeChecked()
  })

  it('calls setOnline when toggle is clicked', () => {
    // Create a mock setOnline function
    const mockSetOnline = vi.fn()

    // Mock the hook to return a controllable state
    vi.mocked(useOnlineManager).mockReturnValue({
      isOnline: true,
      isOffline: false,
      setOnline: mockSetOnline
    })

    const { getByRole } = render(<OnlineToggle />)

    // Get the toggle and simulate a click
    const toggle = getByRole('switch')
    fireEvent.click(toggle)

    // Verify setOnline was called with the opposite of current state
    expect(mockSetOnline).toHaveBeenCalledWith(false)
  })
})
