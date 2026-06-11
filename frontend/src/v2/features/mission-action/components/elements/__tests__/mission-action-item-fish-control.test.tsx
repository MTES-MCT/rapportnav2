import { MissionActionType } from '@common/types/fish-mission-types.ts'
import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import { MissionAction } from '../../../../common/types/mission-action.ts'
import * as useMissionActionFishControlModule from '../../../hooks/use-mission-action-fish-control.tsx'
import MissionActionItemFishControl from '../mission-action-item-fish-control.tsx'

vi.mock('../../../hooks/use-mission-action-fish-control.tsx', () => ({
  useMissionActionFishControl: vi.fn()
}))

const useMissionActionFishControlMock = vi.mocked(useMissionActionFishControlModule.useMissionActionFishControl)
const mockItems = [
  { key: '1', title: 'Infos du navire', component: () => null },
  { key: '2', title: 'Police des pêches', component: () => null },
  { key: '3', title: 'Autres polices', component: () => null },
  { key: '4', title: 'Conclusions', component: () => null }
]

const mockAction = {
  id: '1234',
  fishActionType: MissionActionType.SEA_CONTROL,
  data: { fishInfractions: [] }
} as MissionAction

const props = (action = mockAction, onChange = vi.fn(), isMissionFinished = false) => ({
  action,
  isMissionFinished,
  onChange
})

beforeEach(() => {
  useMissionActionFishControlMock.mockImplementation((action: any) => {
    const fishActionType = action?.data?.fishActionType ?? action?.fishActionType ?? MissionActionType.SEA_CONTROL
    const fishInfractions = action?.data?.fishInfractions ?? []

    return {
      initValue: { ...action.data, dates: [new Date('2025-01-01'), new Date('2025-01-01')], geoCoords: [1, 2] },
      items: mockItems,
      handleSubmit: vi.fn()
    }
  })
})

describe('MissionActionItemFishControl Component', () => {
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
      expect(screen.getByText("Lieu de l'opération (01° 12.600′ S 044° 58.800′ E)")).toBeInTheDocument()
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
