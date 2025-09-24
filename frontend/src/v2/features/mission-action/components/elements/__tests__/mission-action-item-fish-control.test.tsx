import MissionActionItemFishControl from '../mission-action-item-fish-control.tsx'
import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import { MissionAction } from '../../../../common/types/mission-action.ts'
import { MissionActionType } from '@common/types/fish-mission-types.ts'

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
    render(<MissionActionItemFishControl {...props()} />)
  })

  describe('The datepicker', () => {
    it('should be displayed', () => {
      render(<MissionActionItemFishControl {...props()} />)
      expect(screen.getByText('Date et heure de début et de fin (utc)')).toBeInTheDocument()
    })
  })

  describe('Sea Controls', () => {
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
