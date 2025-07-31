import { ActionStatusType } from '@common/types/action-types.ts'
import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { IconProps } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import { render, screen } from '../../../../../../test-utils.tsx'
import { ActionType } from '../../../types/action-type.ts'
import { NetworkSyncStatus } from '../../../types/network-types.ts'
import { OwnerType } from '../../../types/owner-type.ts'
import ActionHeaderCompletenessForStats from '../../ui/action-header-completeness-for-stats.tsx'
import ActionHeaderWrapper from '../action-header-wrapper.tsx'

const MockIcon: FunctionComponent<IconProps> = () => <div data-testid="mock-icon">Icon</div>

describe('ActionHeaderWrapper', () => {
  const defaultProps = (actionType: ActionType, networkSyncStatus: NetworkSyncStatus | undefined = undefined) => ({
    ownerId: '123',
    action: {
      source: 'MONITORENV',
      id: '4c7fae0f-a6f1-419b-adee-615ec08f40a7',
      missionId: '20309',
      actionType,
      isCompleteForStats: true,
      completenessForStats: {},
      status: ActionStatusType.UNAVAILABLE,
      summaryTags: ['Sans PV', 'Sans infraction'],
      controlsToComplete: [],
      sourcesOfMissingDataForStats: [],
      networkSyncStatus,
      data: {
        startDateTimeUtc: '2023-01-01T00:00:00Z',
        endDateTimeUtc: '2025-01-22T14:30:00Z',
        completedBy: 'KLP',
        facade: 'NAMO',
        department: '29',
        openBy: 'PPP',
        observations: 'Surveillance rade de Brest - Mission embarquée (zodiac). \n\nRAS',
        formattedControlPlans: [
          {
            theme: 'Espèce protégée et leur habitat (faune et flore)',
            subThemes: ['Dérangement / perturbation intentionnelle des espèces animales protégées'],
            tags: []
          },
          {
            theme: 'Pêche de loisir (autre que PAP)',
            subThemes: ['Pêche sous-marine', 'Pêche embarquée'],
            tags: []
          }
        ],
        availableControlTypesForInfraction: []
      }
    },
    ownerType: OwnerType.MISSION,
    title: 'Test Action',
    icon: MockIcon,
    missionStatus: MissionStatusEnum.IN_PROGRESS,
    completeness: (
      <ActionHeaderCompletenessForStats
        isMissionFinished={true}
        networkSyncStatus={networkSyncStatus}
        completenessForStats={{}}
      />
    )
  })

  describe('The offline sync status banner', () => {
    it('should not be displayed when action  is undefined', () => {
      const props = { ...defaultProps(ActionType.NOTE), action: undefined }
      render(<ActionHeaderWrapper {...props} />)
      expect(screen.queryByTestId('report-status-banner')).not.toBeInTheDocument()
      expect(screen.queryByText("Attention, cette action n'est pas encore synchronisée avec le serveur")).toBeNull()
    })
    it('should not be displayed when action sync status is undefined', () => {
      const props = defaultProps(ActionType.NOTE, undefined)
      render(<ActionHeaderWrapper {...props} />)
      expect(screen.queryByTestId('report-status-banner')).not.toBeInTheDocument()
      expect(screen.queryByText("Attention, cette action n'est pas encore synchronisée avec le serveur")).toBeNull()
    })
    it('should not be displayed when action sync status is synced', () => {
      const props = defaultProps(ActionType.NOTE, NetworkSyncStatus.SYNC)
      render(<ActionHeaderWrapper {...props} />)
      expect(screen.queryByTestId('report-status-banner')).not.toBeInTheDocument()
      expect(screen.queryByText("Attention, cette action n'est pas encore synchronisée avec le serveur")).toBeNull()
    })
    it('should be displayed when action sync status is synced', () => {
      const props = defaultProps(ActionType.NOTE, NetworkSyncStatus.UNSYNC)
      render(<ActionHeaderWrapper {...props} />)
      expect(screen.queryByTestId('report-status-banner')).toBeInTheDocument()
      expect(screen.queryByText("Attention, cette action n'est pas encore synchronisée avec le serveur")).not.toBeNull()
    })
  })

  describe('The title wrapper', () => {
    it('should not be displayed when action is undefined', () => {
      const props = { ...defaultProps(ActionType.NOTE), action: undefined }
      render(<ActionHeaderWrapper {...props} />)
      expect(screen.queryByText('Test Action')).toBeNull()
    })

    it('should be displayed when action has a type', () => {
      const props = defaultProps(ActionType.NOTE)
      render(<ActionHeaderWrapper {...props} />)
      expect(screen.queryByText('Test Action')).not.toBeNull()
    })
  })
})
