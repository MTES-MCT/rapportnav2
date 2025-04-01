import { render, screen } from '../../../../../../test-utils.tsx'
import { vi } from 'vitest'
import { MissionStatusEnum } from '@common/types/mission-types'
import { NetworkSyncStatus } from '../../../../common/types/network-types'
import { MissionActionHeaderCompletenessForStats } from '../mission-action-header-completeness-for-stats.tsx'

// Mock the hook
vi.mock('../../../../common/hooks/use-mission-completeness-for-stats', async () => {
  const React = await import('react')
  return {
    useMissionCompletenessForStats: vi.fn(() => ({
      icon: () => <span data-testid="mock-icon">Icon</span>,
      statusMessage: 'Mocked status message',
      color: 'red'
    }))
  }
})

describe('MissionActionHeaderCompletenessForStats', () => {
  it('renders icon and message when network is synced', () => {
    render(
      <MissionActionHeaderCompletenessForStats
        missionStatus={MissionStatusEnum.ENDED}
        completenessForStats={{} as any}
        networkSyncStatus={NetworkSyncStatus.SYNC}
      />
    )

    expect(screen.getByTestId('mock-icon')).toBeInTheDocument()
    expect(screen.getByText('Mocked status message')).toBeInTheDocument()
  })

  it('does not render icon and text when network is unsynced', () => {
    render(
      <MissionActionHeaderCompletenessForStats
        missionStatus={MissionStatusEnum.ENDED}
        completenessForStats={{} as any}
        networkSyncStatus={NetworkSyncStatus.UNSYNC}
      />
    )

    expect(screen.queryByTestId('mock-icon')).not.toBeInTheDocument()
    expect(screen.queryByText('Mocked status message')).not.toBeInTheDocument()
  })

  it('still renders when missionStatus and completenessForStats are undefined', () => {
    render(<MissionActionHeaderCompletenessForStats />)

    expect(screen.getByText('Mocked status message')).toBeInTheDocument()
  })
})
