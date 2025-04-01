import { describe, it, expect } from 'vitest'
import { ActionType } from '../../../../common/types/action-type.ts'
import MissionTimelineItemSyncStatus from '../mission-timeline-item-sync-status.tsx'
import { render, screen } from '../../../../../../test-utils.tsx'

describe('MissionTimelineItemSyncStatus', () => {
  it('should render ActionStatusSyncStatus when actionType is STATUS', () => {
    render(<MissionTimelineItemSyncStatus actionType={ActionType.STATUS} />)

    const element = screen.getByTestId('action-status-sync-status')
    expect(element).toBeInTheDocument()

    const elementThatShouldNotBeThere = screen.queryByTestId('other-actions-sync-status')
    expect(elementThatShouldNotBeThere).toBeNull()
  })
  it('should render OtherActionsSyncStatus when actionType is other than STATUS', () => {
    render(<MissionTimelineItemSyncStatus actionType={ActionType.NOTE} />)

    const element = screen.getByTestId('other-actions-sync-status')
    expect(element).toBeInTheDocument()

    const elementThatShouldNotBeThere = screen.queryByTestId('action-status-sync-status')
    expect(elementThatShouldNotBeThere).toBeNull()
  })
})
