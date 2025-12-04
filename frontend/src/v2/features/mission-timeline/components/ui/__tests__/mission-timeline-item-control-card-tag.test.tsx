import { render, screen } from '../../../../../../test-utils'
import MissionTimelineItemControlCardTag from '../mission-timeline-item-control-card-tag'
import { MissionTimelineAction } from '../../../types/mission-timeline-output'
import { NetworkSyncStatus } from '../../../../common/types/network-types'

describe('MissionTimelineItemControlCardTag', () => {
  it('renders unsynced message when controls exist and network status is UNSYNC', () => {
    const action = {
      controlsToComplete: [{ id: 1 }, { id: 2 }],
      networkSyncStatus: NetworkSyncStatus.UNSYNC
    } as unknown as MissionTimelineAction

    render(<MissionTimelineItemControlCardTag action={action} />)

    expect(screen.getByText('Indisponible hors-ligne')).toBeInTheDocument()
  })

  it('renders MissionIncompleteControlTag when controls exist and network is synced', () => {
    const action = {
      controlsToComplete: [{ id: 1 }, { id: 2 }, { id: 3 }],
      networkSyncStatus: NetworkSyncStatus.SYNC
    } as unknown as MissionTimelineAction

    render(<MissionTimelineItemControlCardTag action={action} />)

    // MissionIncompleteControlTag usually displays the number → adjust if needed
    expect(screen.getByText('3')).toBeInTheDocument()
  })

  it('renders summary tags when no controlsToComplete', () => {
    const action = {
      summaryTags: ['Tag A', 'Tag B']
    } as MissionTimelineAction

    render(<MissionTimelineItemControlCardTag action={action} />)

    expect(screen.getByText('Tag A')).toBeInTheDocument()
    expect(screen.getByText('Tag B')).toBeInTheDocument()
  })

  it('renders summary tags when controlsToComplete is empty array', () => {
    const action = {
      controlsToComplete: [],
      summaryTags: ['Summary tag']
    } as MissionTimelineAction

    render(<MissionTimelineItemControlCardTag action={action} />)

    expect(screen.getByText('Summary tag')).toBeInTheDocument()
  })

  it('matches snapshot', () => {
    const action = {
      summaryTags: ['Hello']
    } as MissionTimelineAction

    const wrapper = render(<MissionTimelineItemControlCardTag action={action} />)
    expect(wrapper).toMatchSnapshot()
  })
})
