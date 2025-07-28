import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { useOnlineManager } from '../../../hooks/use-online-manager.tsx'
import useMission from '../../../services/use-mission.tsx'
import MissionPageFooter from '../mission-page-footer'

vi.mock('../../../services/use-mission.tsx', () => ({
  default: vi.fn()
}))

vi.mock('../../../hooks/use-online-manager.tsx', () => ({
  useOnlineManager: vi.fn()
}))

const exitMission = vi.fn()

describe('MissionPageFooter', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.clearAllTimers()
    vi.mocked(useOnlineManager).mockReturnValue({ isOnline: true } as any)
    vi.mocked(useMission).mockReturnValue({ dataUpdatedAt: 0 } as any)
  })
  it('should match the snapshot', () => {
    const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={'missionId'} />)
    expect(wrapper).toMatchSnapshot()
  })
  describe('synchro text', () => {
    beforeEach(() => {
      vi.clearAllMocks()
      vi.clearAllTimers()
    })
    it('should say offline when offline', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false } as any)
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={'missionId'} />)
      expect(wrapper.getByText('Connexion indisponible', { exact: false })).toBeInTheDocument()
    })
    it('should say online when online', () => {
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={'missionId'} />)
      expect(wrapper.getByText('Connexion disponible', { exact: false })).toBeInTheDocument()
    })
    it('should not show last sync text when dataUpdatedAt falsy', () => {
      vi.mocked(useMission).mockReturnValue({ dataUpdatedAt: 0 } as any)
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={'missionId'} />)
      expect(wrapper.queryAllByAltText('Dernière synchronisation ', { exact: false })).toHaveLength(0)
    })
    it('should show last sync text when dataUpdatedAt truthy', () => {
      vi.mocked(useMission).mockReturnValue({ dataUpdatedAt: 1234567890 } as any)
      const wrapper = render(<MissionPageFooter exitMission={exitMission} missionId={'missionId'} />)
      expect(wrapper.getByText('Dernière synchronisation ', { exact: false })).toBeInTheDocument()
    })
  })
})
