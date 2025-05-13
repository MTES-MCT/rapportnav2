import { render, screen } from '../../../../../../test-utils.tsx'
import OfflineTag from '../offline-tag.tsx'
import { useOnlineManager } from '../../../hooks/use-online-manager.tsx'
import { vi } from 'vitest'

// Mock the useOnlineManager hook
vi.mock('../../../hooks/use-online-manager', () => ({
  useOnlineManager: vi.fn()
}))

describe('OfflineTag', () => {
  it('renders correctly with the expected text when offline', () => {
    vi.mocked(useOnlineManager).mockReturnValue({
      isOnline: false,
      isOffline: true,
      setOnline: vi.fn()
    })
    render(<OfflineTag />)
    expect(screen.getByText('Hors ligne')).toBeInTheDocument()
  })
  it('renders nothing when online', () => {
    vi.mocked(useOnlineManager).mockReturnValue({
      isOnline: true,
      isOffline: false,
      setOnline: vi.fn()
    })
    const { container } = render(<OfflineTag />)

    // Check that nothing is rendered
    expect(container.firstChild).toBeNull()
  })
})
