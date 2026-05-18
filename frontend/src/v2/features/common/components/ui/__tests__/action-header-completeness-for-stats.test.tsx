import { render, screen } from '../../../../../../test-utils.tsx'
import { vi } from 'vitest'
import { CompletenessForStatsStatusEnum } from '../../../types/mission-types.ts'
import { NetworkSyncStatus } from '../../../types/network-types.ts'
import ActionHeaderCompletenessForStats from '../action-header-completeness-for-stats.tsx'
import { useMissionCompletenessForStats } from '../../../hooks/use-mission-completeness-for-stats.tsx'
import { setFormValidation, resetFormValidation } from '../../../../../store/slices/form-validation-reducer'

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
  beforeEach(() => {
    resetFormValidation()
    vi.clearAllMocks()
  })

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

  it('should override completeness to INVALID when formValidation.isValid is false', () => {
    const completeness = { status: CompletenessForStatsStatusEnum.VALID, sources: [] }
    setFormValidation(false)

    render(
      <ActionHeaderCompletenessForStats
        isMissionFinished={false}
        completenessForStats={completeness}
        networkSyncStatus={NetworkSyncStatus.SYNC}
      />
    )

    expect(useMissionCompletenessForStats).toHaveBeenCalledWith(
      expect.objectContaining({ status: CompletenessForStatsStatusEnum.INVALID }),
      false
    )
  })

  it('should pass original completeness when formValidation.isValid is true', () => {
    const completeness = { status: CompletenessForStatsStatusEnum.VALID, sources: [] }
    setFormValidation(true)

    render(
      <ActionHeaderCompletenessForStats
        isMissionFinished={false}
        completenessForStats={completeness}
        networkSyncStatus={NetworkSyncStatus.SYNC}
      />
    )

    expect(useMissionCompletenessForStats).toHaveBeenCalledWith(completeness, false)
  })

  it('should pass original completeness when formValidation.isValid is undefined', () => {
    const completeness = { status: CompletenessForStatsStatusEnum.VALID, sources: [] }

    render(
      <ActionHeaderCompletenessForStats
        isMissionFinished={false}
        completenessForStats={completeness}
        networkSyncStatus={NetworkSyncStatus.SYNC}
      />
    )

    expect(useMissionCompletenessForStats).toHaveBeenCalledWith(completeness, false)
  })
})
