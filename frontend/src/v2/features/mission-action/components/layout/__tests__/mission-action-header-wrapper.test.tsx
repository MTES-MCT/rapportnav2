import MissionActionHeaderWrapper from '../mission-action-header-wrapper.tsx'
import { render, screen } from '../../../../../../test-utils.tsx'
import { NetworkSyncStatus } from '../../../../common/types/network-types.ts'
import { ModuleType } from '../../../../common/types/module-type.ts'
import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { FunctionComponent } from 'react'
import { IconProps } from '@mtes-mct/monitor-ui'
import { ActionType } from '../../../../common/types/action-type.ts'

const MockIcon: FunctionComponent<IconProps> = () => <div data-testid="mock-icon">Icon</div>

describe('MissionActionHeaderWrapper', () => {
  const defaultProps = (
    networkSyncStatus: NetworkSyncStatus | undefined = undefined,
    actionType: ActionType | undefined = undefined
  ) => ({
    missionId: 123,
    action: {
      id: 'action-123',
      actionType,
      networkSyncStatus,
      source: 'SOURCE',
      completenessForStats: { isComplete: true },
      data: {
        startDateTimeUtc: '2023-01-01T00:00:00Z'
      }
    },
    moduleType: ModuleType.PAM,
    title: 'Test Action',
    icon: MockIcon,
    missionStatus: MissionStatusEnum.IN_PROGRESS
  })

  describe('The offline sync status banner', () => {
    it('should not be displayed when action  is undefined', () => {
      const props = { ...defaultProps(), action: undefined }
      render(<MissionActionHeaderWrapper {...props} />)
      expect(screen.queryByTestId('mission-report-status-banner')).not.toBeInTheDocument()
      expect(screen.queryByText("Attention, cette action n'est pas encore synchronisée avec le serveur")).toBeNull()
    })
    it('should not be displayed when action sync status is undefined', () => {
      const props = defaultProps(undefined)
      render(<MissionActionHeaderWrapper {...props} />)
      expect(screen.queryByTestId('mission-report-status-banner')).not.toBeInTheDocument()
      expect(screen.queryByText("Attention, cette action n'est pas encore synchronisée avec le serveur")).toBeNull()
    })
    it('should not be displayed when action sync status is synced', () => {
      const props = defaultProps(NetworkSyncStatus.SYNC)
      render(<MissionActionHeaderWrapper {...props} />)
      expect(screen.queryByTestId('mission-report-status-banner')).not.toBeInTheDocument()
      expect(screen.queryByText("Attention, cette action n'est pas encore synchronisée avec le serveur")).toBeNull()
    })
    it('should be displayed when action sync status is synced', () => {
      const props = defaultProps(NetworkSyncStatus.UNSYNC)
      render(<MissionActionHeaderWrapper {...props} />)
      expect(screen.queryByTestId('mission-report-status-banner')).toBeInTheDocument()
      expect(screen.queryByText("Attention, cette action n'est pas encore synchronisée avec le serveur")).not.toBeNull()
    })
  })

  describe('The title wrapper', () => {
    it('should not be displayed when action is undefined', () => {
      const props = { ...defaultProps(), action: undefined }
      render(<MissionActionHeaderWrapper {...props} />)
      expect(screen.queryByText('Test Action')).toBeNull()
    })
    it('should not be displayed when action has no actionType', () => {
      const props = defaultProps(undefined, undefined)
      render(<MissionActionHeaderWrapper {...props} />)
      expect(screen.queryByText('Test Action')).toBeNull()
    })
    it('should be displayed when action has a type', () => {
      const props = defaultProps(undefined, ActionType.NOTE)
      render(<MissionActionHeaderWrapper {...props} />)
      expect(screen.queryByText('Test Action')).not.toBeNull()
    })
  })
})
