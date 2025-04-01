import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import MissionTimelineCardWrapper from '../mission-timeline-item-card-wrapper.tsx'
import { NetworkSyncStatus } from '../../../../common/types/network-types.ts'

describe('MissionTimelineCardWrapper', () => {
  describe('the network status', () => {
    it('should not be shown when networkSyncStatus undefined', () => {
      render(<MissionTimelineCardWrapper action={{ networkSyncStatus: undefined }} />)

      const element = screen.queryByTestId('network-sync-status')
      expect(element).toBeNull()
    })
    it('should not be shown when networkSyncStatus is sync', () => {
      render(<MissionTimelineCardWrapper action={{ networkSyncStatus: NetworkSyncStatus.SYNC }} />)

      const element = screen.queryByTestId('network-sync-status')
      expect(element).toBeNull()
    })
    it('should be shown when networkSyncStatus is unsync', () => {
      render(<MissionTimelineCardWrapper action={{ networkSyncStatus: NetworkSyncStatus.UNSYNC }} n />)

      const element = screen.queryByTestId('network-sync-status')
      expect(element).not.toBeNull()
    })
  })
})
