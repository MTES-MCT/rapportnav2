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
      hasNetwork: true,
      toggleOnline: vi.fn()
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
      hasNetwork: true,
      toggleOnline: vi.fn()
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

  it('calls toggleOnline when toggle is clicked', () => {
    // Create a mock toggleOnline function
    const mockSetOnline = vi.fn()

    // Mock the hook to return a controllable state
    vi.mocked(useOnlineManager).mockReturnValue({
      isOnline: true,
      isOffline: false,
      hasNetwork: true,
      toggleOnline: mockSetOnline
    })

    const { getByRole } = render(<OnlineToggle />)

    // Get the toggle and simulate a click
    const toggle = getByRole('switch')
    fireEvent.click(toggle)

    // Verify toggleOnline was called with the opposite of current state
    expect(mockSetOnline).toHaveBeenCalledWith(false)
  })

  it('shows correct title when toggle is disabled due to chosen offline mode', () => {
    // Mock the hook to simulate disabled toggle
    const mockToggleOnline = vi.fn()
    vi.mocked(useOnlineManager).mockReturnValue({
      isOnline: false,
      isOffline: true,
      hasNetwork: true,
      toggleOnline: mockToggleOnline
    })

    const { getByRole } = render(<OnlineToggle />)

    // Get the input element (the toggle), not disabled
    const toggleInput = getByRole('switch') as HTMLInputElement
    expect(toggleInput).not.toBeDisabled()

    // Access the parent label and check the title attribute
    const label = toggleInput.closest('label')
    expect(label).toHaveAttribute(
      'title',
      'Vous êtes hors-ligne mais une connection est détectée, vous pouvez tenter de revenir en ligne.'
    )

    // Try clicking the disabled toggle - it should trigger a change
    fireEvent.click(toggleInput)
    expect(mockToggleOnline).toHaveBeenCalled()
  })

  it('shows correct title when toggle is disabled due to missing network', () => {
    // Mock the hook to simulate disabled toggle
    const mockToggleOnline = vi.fn()
    vi.mocked(useOnlineManager).mockReturnValue({
      isOnline: false,
      isOffline: true,
      hasNetwork: false,
      toggleOnline: mockToggleOnline
    })

    const { getByRole } = render(<OnlineToggle />)

    // Get the input element (the toggle), disabled
    const toggleInput = getByRole('switch') as HTMLInputElement
    expect(toggleInput).toBeDisabled()

    // Access the parent label and check the title attribute
    const label = toggleInput.closest('label')
    expect(label).toHaveAttribute('title', 'Nous ne pouvez pas passer en ligne, connection non détectée.')

    // Try clicking the disabled toggle, it should not do anything
    fireEvent.click(toggleInput)
    expect(mockToggleOnline).not.toHaveBeenCalled()
  })
})
