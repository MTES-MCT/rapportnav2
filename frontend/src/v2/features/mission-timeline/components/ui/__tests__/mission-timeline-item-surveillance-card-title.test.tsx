import { render } from '../../../../../../test-utils'
import { MissionTimelineAction } from '../../../types/mission-timeline-output'
import MissionTimelineItemSurveillanceCardTitle from '../mission-timeline-item-surveillance-card-title'

describe('MissionTimelineItemSurveillanceCardTitle', () => {
  it('should match the snapshot', () => {
    const action: MissionTimelineAction = {} as MissionTimelineAction
    const wrapper = render(<MissionTimelineItemSurveillanceCardTitle action={action} />)
    expect(wrapper).toMatchSnapshot()
  })
})
