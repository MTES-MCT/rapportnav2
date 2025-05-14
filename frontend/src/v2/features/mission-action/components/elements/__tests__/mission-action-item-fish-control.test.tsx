import MissionActionItemFishControl from '../mission-action-item-fish-control.tsx'
import { useOnlineManager } from '../../../../common/hooks/use-online-manager.tsx'
import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import { MissionAction } from '../../../../common/types/mission-action.ts'

// Mock the useOnlineManager hook
vi.mock('../../../../common/hooks/use-online-manager.tsx', () => ({
  useOnlineManager: vi.fn()
}))

const mockAction = {
  id: '1234'
} as MissionAction

const props = (action = mockAction, onChange = vi.fn(), isMissionFinished = false) => ({
  action,
  isMissionFinished,
  onChange
})

describe('MissionActionItemEnvControl Component', () => {
  it('renders', () => {
    vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false } as any)
    render(<MissionActionItemFishControl {...props()} />)
  })

  describe('The datepicker', () => {
    it('should be disabled when offline', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false } as any)
      render(<MissionActionItemFishControl {...props()} />)
      expect(
        screen.queryAllByTitle(
          "Non disponible hors ligne, il est nécessaire d'être synchronisé avec les centres pour saisir/modifier cette donnée."
        )
      ).not.toBeNull()
    })
    it('should be enabled when online', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: true } as any)
      render(<MissionActionItemFishControl {...props()} />)
      expect(screen.queryByText('Non disponible hors ligne,', { exact: false })).toBeNull()
    })
  })
})
