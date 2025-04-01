import { render, screen } from '../../../../../../test-utils.tsx'
import { vi } from 'vitest'
import { NetworkSyncStatus } from '../../../types/network-types.ts'
import ActionHeaderCompletenessForStats from '../action-header-completeness-for-stats.tsx'

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

describe('ActionHeaderCompletenessForStats', () => {
  it('renders icon and message when network is synced', () => {
    render(
      <ActionHeaderCompletenessForStats
        isMissionFinished={true}
        completenessForStats={{} as any}
        networkSyncStatus={NetworkSyncStatus.SYNC}
      />
    )

    expect(screen.getByTestId('mock-icon')).toBeInTheDocument()
    expect(screen.getByText('Mocked status message')).toBeInTheDocument()
  })

  it('does not render icon and text when network is unsynced', () => {
    render(
      <ActionHeaderCompletenessForStats
        isMissionFinished={true}
        completenessForStats={{} as any}
        networkSyncStatus={NetworkSyncStatus.UNSYNC}
      />
    )

    expect(screen.queryByTestId('mock-icon')).not.toBeInTheDocument()
    expect(screen.queryByText('Mocked status message')).not.toBeInTheDocument()
  })

  it('still renders when missionStatus and completenessForStats are undefined', () => {
    render(<ActionHeaderCompletenessForStats />)

    expect(screen.getByText('Mocked status message')).toBeInTheDocument()
  })
})
