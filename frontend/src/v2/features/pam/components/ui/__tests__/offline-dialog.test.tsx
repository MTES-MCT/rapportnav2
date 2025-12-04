import { render, screen, fireEvent, act } from '../../../../../../test-utils.tsx'
import { vi, describe, it, expect, beforeEach, afterEach } from 'vitest'
import OfflineDialog, { OFFLINE_CHECK_INTERVAL } from '../offline-dialog.tsx'

// Mock the custom hooks
const mockUseOnlineManager = vi.fn()
const mockUseOfflineSince = vi.fn()

vi.mock('../../../../common/hooks/use-online-manager.tsx', () => ({
  useOnlineManager: () => mockUseOnlineManager()
}))

vi.mock('../../../../common/hooks/use-offline-since.tsx', () => ({
  useOfflineSince: () => mockUseOfflineSince()
}))

describe('OfflineDialog', () => {
  beforeEach(() => {
    vi.useFakeTimers()

    // Default mock implementations
    mockUseOnlineManager.mockReturnValue({
      isOffline: false,
      hasNetwork: true,
      toggleOnline: vi.fn()
    })

    mockUseOfflineSince.mockReturnValue({
      offlineSince: null,
      isOfflineSinceTooLong: vi.fn(() => false)
    })
  })

  afterEach(() => {
    vi.useRealTimers()
    vi.clearAllMocks()
  })

  it('should not show dialog initially when not offline for too long', () => {
    render(<OfflineDialog />)

    expect(screen.queryByTestId('offline-dialog')).not.toBeInTheDocument()
  })

  it('should show dialog when offline for too long', async () => {
    const mockIsOfflineSinceTooLong = vi.fn(() => true)

    mockUseOfflineSince.mockReturnValue({
      offlineSince: new Date(),
      isOfflineSinceTooLong: mockIsOfflineSinceTooLong
    })

    render(<OfflineDialog />)

    // Trigger the interval immediately
    act(() => {
      vi.advanceTimersByTime(OFFLINE_CHECK_INTERVAL)
    })

    expect(screen.getByTestId('offline-dialog')).toBeInTheDocument()
  })

  it('should hide dialog when "Rester hors ligne" button is clicked', async () => {
    const mockIsOfflineSinceTooLong = vi.fn(() => true)

    mockUseOfflineSince.mockReturnValue({
      offlineSince: new Date(),
      isOfflineSinceTooLong: mockIsOfflineSinceTooLong
    })

    render(<OfflineDialog />)

    // Show dialog first
    act(() => {
      vi.advanceTimersByTime(OFFLINE_CHECK_INTERVAL)
    })

    expect(screen.getByTestId('offline-dialog')).toBeInTheDocument()

    const stayOfflineButton = screen.getByText('Rester hors ligne')
    fireEvent.click(stayOfflineButton)

    expect(screen.queryByTestId('offline-dialog')).not.toBeInTheDocument()
  })

  it('should hide dialog and call toggleOnline when "Se reconnecter" button is clicked', async () => {
    const mockToggleOnline = vi.fn()
    const mockIsOfflineSinceTooLong = vi.fn(() => true)

    mockUseOnlineManager.mockReturnValue({
      isOffline: true,
      hasNetwork: true,
      toggleOnline: mockToggleOnline
    })

    mockUseOfflineSince.mockReturnValue({
      offlineSince: new Date(),
      isOfflineSinceTooLong: mockIsOfflineSinceTooLong
    })

    render(<OfflineDialog />)

    // Show dialog first
    act(() => {
      vi.advanceTimersByTime(OFFLINE_CHECK_INTERVAL)
    })

    expect(screen.getByTestId('offline-dialog')).toBeInTheDocument()

    const reconnectButton = screen.getByText('Se reconnecter')
    fireEvent.click(reconnectButton)

    expect(mockToggleOnline).toHaveBeenCalledWith(true)
    expect(screen.queryByTestId('offline-dialog')).not.toBeInTheDocument()
  })

  it('should not hide dialog when "Se reconnecter" button is clicked without network', async () => {
    const mockToggleOnline = vi.fn()
    const mockIsOfflineSinceTooLong = vi.fn(() => true)

    mockUseOnlineManager.mockReturnValue({
      isOffline: true,
      hasNetwork: false, // No network
      toggleOnline: mockToggleOnline
    })

    mockUseOfflineSince.mockReturnValue({
      offlineSince: new Date(),
      isOfflineSinceTooLong: mockIsOfflineSinceTooLong
    })

    render(<OfflineDialog />)

    // Show dialog first
    act(() => {
      vi.advanceTimersByTime(OFFLINE_CHECK_INTERVAL)
    })

    expect(screen.getByTestId('offline-dialog')).toBeInTheDocument()
    const reconnectButton = screen.getByTitle(
      "Vous ne pouvez pas repasser en ligne car vous n'avez pas de réseau. Continuez en mode hors ligne."
    )
    expect(reconnectButton).toBeInTheDocument()
  })

  it('should update dialog visibility when offline status changes', async () => {
    const mockIsOfflineSinceTooLong = vi.fn()

    // Start with not offline for too long
    mockIsOfflineSinceTooLong.mockReturnValue(false)

    mockUseOfflineSince.mockReturnValue({
      offlineSince: new Date(),
      isOfflineSinceTooLong: mockIsOfflineSinceTooLong
    })

    render(<OfflineDialog />)

    // Initially no dialog
    expect(screen.queryByTestId('offline-dialog')).not.toBeInTheDocument()

    // Change the mock to return true
    mockIsOfflineSinceTooLong.mockReturnValue(true)

    // Trigger interval
    act(() => {
      vi.advanceTimersByTime(OFFLINE_CHECK_INTERVAL)
    })

    expect(screen.getByTestId('offline-dialog')).toBeInTheDocument()
  })
})
