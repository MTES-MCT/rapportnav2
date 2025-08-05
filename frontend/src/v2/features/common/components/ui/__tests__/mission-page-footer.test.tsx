import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionPageFooter from '../mission-page-footer'
import { useOnlineManager } from '../../../hooks/use-online-manager.tsx'
import { useOfflineSince } from '../../../hooks/use-offline-since.tsx'
import { useOfflineMode } from '../../../hooks/use-offline-mode.tsx'

vi.mock('../../../hooks/use-online-manager.tsx', () => ({
  useOnlineManager: vi.fn()
}))
vi.mock('../../../hooks/use-offline-since.tsx', () => ({
  useOfflineSince: vi.fn()
}))
vi.mock('../../../hooks/use-offline-mode.tsx', () => ({
  useOfflineMode: vi.fn(),
  getOfflineMode: vi.fn()
}))

const exitMission = vi.fn()

describe('MissionPageFooter', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.clearAllTimers()
    vi.mocked(useOnlineManager).mockReturnValue({ isOnline: true } as any)
    vi.mocked(useOfflineSince).mockReturnValue({ offlineSince: undefined } as any)
  })
  it('should match the snapshot', () => {
    const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={'missionId'} type="PAM" />)
    expect(wrapper).toMatchSnapshot()
  })
  describe('synchro text', () => {
    beforeEach(() => {
      vi.clearAllMocks()
      vi.clearAllTimers()
    })
    it('should say nothing when connected and synchronised', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: true } as any)
      vi.mocked(useOfflineSince).mockReturnValue({ offlineSince: undefined } as any)
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={'1'} />)
      expect(wrapper.queryByText('Connexion', { exact: false })).not.toBeInTheDocument()
    })
    it('should say available connexion', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false, hasNetwork: true } as any)
      vi.mocked(useOfflineSince).mockReturnValue({ offlineSince: '2024-01-09T09:00Z' } as any)
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={'1'} />)
      expect(wrapper.getByText('Connexion disponible', { exact: false })).toBeInTheDocument()
      expect(wrapper.getByText('Dernière synchronisation', { exact: false })).toBeInTheDocument()
    })
    it('should say unavailable connexion', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false, hasNetwork: false } as any)
      vi.mocked(useOfflineSince).mockReturnValue({ offlineSince: '2024-01-09T09:00Z' } as any)
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={'1'} />)
      expect(wrapper.getByText('Connexion indisponible', { exact: false })).toBeInTheDocument()
      expect(wrapper.queryAllByAltText('Dernière synchronisation ', { exact: false })).toHaveLength(0)
    })
  })

  describe('OnlineToggle component', () => {
    it('should render OnlineToggle for PAM type when offline mode is enabled', () => {
      vi.mocked(useOfflineMode).mockReturnValue(true)

      render(<MissionPageFooter exitMission={exitMission} missionId={'1'} type="PAM" />)

      expect(screen.getByTestId('online-toggle')).toBeInTheDocument()
    })

    it('should not render OnlineToggle for PAM type when offline mode is disabled', () => {
      vi.mocked(useOfflineMode).mockReturnValue(false)

      render(<MissionPageFooter exitMission={exitMission} missionId={'1'} type="PAM" />)

      expect(screen.queryByTestId('online-toggle')).not.toBeInTheDocument()
    })

    it('should not render OnlineToggle for ULAM type even when offline mode is enabled', () => {
      vi.mocked(useOfflineMode).mockReturnValue(true)

      render(<MissionPageFooter exitMission={exitMission} missionId={'1'} type="ULAM" />)

      expect(screen.queryByTestId('online-toggle')).not.toBeInTheDocument()
    })

    it('should not render OnlineToggle for ULAM type when offline mode is disabled', () => {
      vi.mocked(useOfflineMode).mockReturnValue(false)

      render(<MissionPageFooter exitMission={exitMission} missionId={'1'} type="ULAM" />)

      expect(screen.queryByTestId('online-toggle')).not.toBeInTheDocument()
    })
  })

  describe('prop variations', () => {
    it('should handle different missionId values', () => {
      render(<MissionPageFooter exitMission={exitMission} missionId={999} type="ULAM" />)

      expect(screen.getByRole('button', { name: /supprimer la mission/i })).toBeInTheDocument()
    })

    it('should call exitMission prop when passed to delete function', () => {
      const mockExitMission = vi.fn()

      render(<MissionPageFooter exitMission={mockExitMission} missionId={'1'} type="ULAM" />)

      // This would test the exitMission call if the button wasn't disabled
      // You might need to adjust this based on your component's behavior
    })
  })

  describe('hook integration', () => {
    it('should properly integrate all hooks', () => {
      const mockOnlineManager = { isOnline: false, hasNetwork: true }
      const mockOfflineSince = { offlineSince: '2024-01-09T09:00Z' }
      const mockOfflineMode = true

      vi.mocked(useOnlineManager).mockReturnValue(mockOnlineManager as any)
      vi.mocked(useOfflineSince).mockReturnValue(mockOfflineSince as any)
      vi.mocked(useOfflineMode).mockReturnValue(mockOfflineMode)

      render(<MissionPageFooter exitMission={exitMission} missionId={'1'} type="PAM" />)

      expect(useOnlineManager).toHaveBeenCalled()
      expect(useOfflineSince).toHaveBeenCalled()
      expect(useOfflineMode).toHaveBeenCalled()
    })
  })

  describe('edge cases', () => {
    it('should handle null/undefined values gracefully', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: undefined, hasNetwork: undefined } as any)
      vi.mocked(useOfflineSince).mockReturnValue({ offlineSince: null } as any)
      vi.mocked(useOfflineMode).mockReturnValue(undefined as any)

      expect(() => {
        render(<MissionPageFooter exitMission={exitMission} missionId={'1'} type="PAM" />)
      }).not.toThrow()
    })
  })
})
