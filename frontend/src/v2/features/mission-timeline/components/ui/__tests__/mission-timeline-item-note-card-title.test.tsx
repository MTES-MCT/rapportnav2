import { render, screen } from '../../../../../../test-utils'
import { MissionTimelineAction } from '../../../types/mission-timeline-output'
import MissionTimelineItemNoteCardTitle from '../mission-timeline-item-note-card-title'

describe('MissionTimelineItemNoteCardTitle', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<MissionTimelineItemNoteCardTitle text="Note libre" />)
    expect(wrapper).toMatchSnapshot()
  })

  it('should display observations if observations, if not display note', () => {
    const action: MissionTimelineAction = {
      observations: 'my observations'
    } as MissionTimelineAction
    render(<MissionTimelineItemNoteCardTitle action={action} />)
    expect(screen.getByText('my observations')).toBeInTheDocument()
  })
})
