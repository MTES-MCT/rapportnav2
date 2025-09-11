import MissionActionItemFishControl from '../mission-action-item-fish-control.tsx'
import { useOnlineManager } from '../../../../common/hooks/use-online-manager.tsx'
import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import { MissionAction } from '../../../../common/types/mission-action.ts'
import { MissionActionType } from '@common/types/fish-mission-types.ts'

// Mock the useOnlineManager hook
vi.mock('../../../../common/hooks/use-online-manager.tsx', () => ({
  useOnlineManager: vi.fn()
}))

const mockAction = {
  id: '1234',
  fishActionType: MissionActionType.SEA_CONTROL
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

  describe('Sea Controls', () => {
    vi.mocked(useOnlineManager).mockReturnValue({ isOnline: true } as any)
    it('should show the coordinate input but not the port input', () => {
      const action = {
        data: {
          ...mockAction
        }
      }
      render(<MissionActionItemFishControl {...props(action)} />)
      expect(screen.getByText("Lieu de l'opération (12° 12.12′ N 121° 21.21′ E)")).toBeInTheDocument()
      expect(screen.queryByText("Lieu de l'opération")).toBeNull()
    })
  })

  describe('Land Controls', () => {
    vi.mocked(useOnlineManager).mockReturnValue({ isOnline: true } as any)
    it('should show port field but not the coordinate input', () => {
      const action = {
        data: {
          ...mockAction,
          fishActionType: MissionActionType.LAND_CONTROL
        }
      }
      render(<MissionActionItemFishControl {...props(action)} />)
      expect(screen.getByText("Lieu de l'opération")).toBeInTheDocument()
      expect(screen.queryByTestId('mission-coordinate-dmd-input')).toBeNull()
    })
    it('should show the portName with portLocode', () => {
      const action = {
        data: {
          ...mockAction,
          fishActionType: MissionActionType.LAND_CONTROL,
          portName: 'Audierne',
          portLocode: 'AUD'
        }
      }
      render(<MissionActionItemFishControl {...props(action)} />)
      expect(screen.getByText("Lieu de l'opération")).toBeInTheDocument()
      expect(screen.getByTestId('portName')).toHaveValue('Audierne (AUD)')
    })
    it('should show the portName only if no portLocode', () => {
      const action = {
        data: {
          ...mockAction,
          fishActionType: MissionActionType.LAND_CONTROL,
          portName: 'Audierne',
          portLocode: undefined
        }
      }
      render(<MissionActionItemFishControl {...props(action)} />)
      expect(screen.getByTestId('portName')).toHaveValue('Audierne ')
    })
  })
})
