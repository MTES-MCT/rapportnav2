import { describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../../test-utils.tsx'
import MissionTimelinePamBody from '../mission-timeline-pam-body.tsx'
import { MissionTimelineAction } from '../../../../../mission-timeline/types/mission-timeline-output.ts'
import { ActionType } from '../../../../../common/types/action-type.ts'
import { ActionStatusType } from '@common/types/action-types.ts'
import { NetworkSyncStatus } from '../../../../../common/types/network-types.ts'
import { useGlobalSyncStatus } from '../../../../../common/hooks/use-global-sync-status.tsx'

vi.mock('../../../../../common/hooks/use-global-sync-status.tsx', () => ({
  useGlobalSyncStatus: vi.fn().mockReturnValue({
    active: false,
    mutatingCount: 0
  })
}))

const mockActions: MissionTimelineAction[] = [
  { id: 1, startDateTimeUtc: '2023-12-01T10:00:00Z', type: ActionType.STATUS, status: ActionStatusType.NAVIGATING },
  { id: 2, startDateTimeUtc: '2023-11-15T14:00:00Z', type: ActionType.NOTE, observations: 'note libre' }
]

const defaultProps = {
  missionId: 123,
  isLoading: false,
  isError: null,
  actions: mockActions
}

describe('MissionTimelinePamBody', () => {
  beforeEach(() => {
    vi.restoreAllMocks()
    ;(useGlobalSyncStatus as vi.Mock).mockReturnValue({
      active: false,
      mutatingCount: 0
    })
  })

  it('renders the component correctly', () => {
    render(<MissionTimelinePamBody {...defaultProps} />)

    expect(screen.getByText('Navigation - début')).toBeInTheDocument()
    expect(screen.getByText('note libre')).toBeInTheDocument()
  })

  it('shows offline banner when at least one action is UNSYNC', () => {
    const unsynced = [
      {
        ...mockActions[0],
        networkSyncStatus: NetworkSyncStatus.UNSYNC
      }
    ]

    render(<MissionTimelinePamBody {...defaultProps} actions={unsynced} />)

    expect(screen.getByText(/vous êtes actuellement hors connexion/i)).toBeInTheDocument()
  })

  it('does not show any banner when everything is synced and no global sync', () => {
    render(<MissionTimelinePamBody {...defaultProps} />)

    expect(screen.queryByText(/Synchronisation/i)).not.toBeInTheDocument()

    expect(screen.queryByText(/hors connexion/i)).not.toBeInTheDocument()
  })

  it('shows sync banner when sync is active and multiple mutations are pending', () => {
    ;(useGlobalSyncStatus as vi.Mock).mockReturnValue({
      active: true,
      mutatingCount: 3
    })
    render(<MissionTimelinePamBody {...defaultProps} />)
    expect(screen.getByText(/Synchronisation de 3 action\(s\) avec le serveur/i)).toBeInTheDocument()
  })

  it('does not show the banner when only one item pending', () => {
    ;(useGlobalSyncStatus as vi.Mock).mockReturnValue({
      active: true,
      mutatingCount: 0
    })
    render(<MissionTimelinePamBody {...defaultProps} />)
    expect(screen.queryByText(/Synchronisation/i)).not.toBeInTheDocument()
  })

  it('renders error state of TimelineWrapper', () => {
    render(<MissionTimelinePamBody {...defaultProps} isError={new Error('Boom')} />)
    expect(
      screen.getByText("Une erreur s'est produite lors du chargement de la timeline.", { exact: false })
    ).toBeInTheDocument()
  })
})
