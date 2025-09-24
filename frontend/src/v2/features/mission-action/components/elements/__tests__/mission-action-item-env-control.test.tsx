import MissionActionItemEnvControl from '../mission-action-item-env-control.tsx'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils.tsx'
import { MissionAction } from '../../../../common/types/mission-action.ts'

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
    render(<MissionActionItemEnvControl {...props()} />)
  })
})
