import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import MissionPageFooter from '../mission-page-footer'

const exitMission = vi.fn()

describe('MissionPageFooter', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.clearAllTimers()
  })
  it('should match the snapshot', () => {
    const wrapper = render(<MissionPageFooter exitMission={exitMission} />)
    expect(wrapper).toMatchSnapshot()
  })
})
