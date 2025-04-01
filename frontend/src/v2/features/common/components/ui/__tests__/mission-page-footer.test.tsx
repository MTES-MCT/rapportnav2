import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import MissionPageFooter from '../mission-page-footer'
import { useOnlineManager } from '../../../hooks/use-online-manager.tsx'
import { useOfflineSince } from '../../../hooks/use-offline-since.tsx'

vi.mock('../../../hooks/use-online-manager.tsx', () => ({
  useOnlineManager: vi.fn()
}))
vi.mock('../../../hooks/use-offline-since.tsx', () => ({
  useOfflineSince: vi.fn()
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
    const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={1} />)
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
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={1} />)
      expect(wrapper.queryByText('Connexion', { exact: false })).not.toBeInTheDocument()
    })
    it('should say available connexion', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false, hasNetwork: true } as any)
      vi.mocked(useOfflineSince).mockReturnValue({ offlineSince: '2024-01-09T09:00Z' } as any)
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={1} />)
      expect(wrapper.getByText('Connexion disponible', { exact: false })).toBeInTheDocument()
      expect(wrapper.getByText('Dernière synchronisation', { exact: false })).toBeInTheDocument()
    })
    it('should say unavailable connexion', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false, hasNetwork: false } as any)
      vi.mocked(useOfflineSince).mockReturnValue({ offlineSince: '2024-01-09T09:00Z' } as any)
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={1} />)
      expect(wrapper.getByText('Connexion indisponible', { exact: false })).toBeInTheDocument()
      expect(wrapper.queryAllByAltText('Dernière synchronisation ', { exact: false })).toHaveLength(0)
    })
  })
})
