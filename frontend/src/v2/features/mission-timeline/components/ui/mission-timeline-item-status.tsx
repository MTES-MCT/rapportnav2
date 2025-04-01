import { FC } from 'react'
import { ActionStatusType } from '@common/types/action-types'
import { CompletenessForStats, CompletenessForStatsStatusEnum } from '@common/types/mission-types'
import { Accent, Icon, IconButton, THEME } from '@mtes-mct/monitor-ui'
import { useActionStatus } from '../../../common/hooks/use-action-status'
import { MissionTimelineStatusTag } from './mission-timeline-status-tag'
import { ActionType } from '../../../common/types/action-type.ts'

type MissionTimelineItemStatusProps = {
  type: ActionType
  status?: ActionStatusType
  completenessForStats?: CompletenessForStats
  isUnsync: boolean
}

const MissionTimelineItemStatus: FC<MissionTimelineItemStatusProps> = ({
  type,
  status,
  completenessForStats,
  isUnsync
}: MissionTimelineItemStatusProps) => {
  const { color } = useActionStatus(status)

  if (isUnsync) {
    return (
      <div data-testid={'timeline-item-incomplete-report'}>
        <IconButton
          accent={Accent.TERTIARY}
          Icon={Icon.AttentionFilled}
          color={THEME.color.lightGray}
          title={
            'Cet évènement contient peut-être des données manquantes indispensables pour les statistiques. Le status sera actualisé quand vous repasserez en ligne.'
          }
          style={{ cursor: 'auto', width: '20px', marginLeft: '-5px' }}
        />
      </div>
    )
  }
  if (type !== ActionType.STATUS && completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE)
    return (
      <div style={{ width: '15px', height: '100%', padding: '5px 0 5px 5px' }} data-testid={'timeline-item-status'}>
        <MissionTimelineStatusTag
          style={{
            height: '100%',
            borderRadius: '5px',
            backgroundColor: color
          }}
          status={status}
        />
      </div>
    )
  if (completenessForStats?.status === CompletenessForStatsStatusEnum.INCOMPLETE)
    return (
      <div data-testid={'timeline-item-incomplete-report'}>
        <IconButton
          accent={Accent.TERTIARY}
          Icon={Icon.AttentionFilled}
          color={color === 'transparent' ? THEME.color.charcoal : color}
          title={'Cet évènement contient des données manquantes indispensables pour les statistiques.'}
          style={{ cursor: 'auto', width: '20px', marginLeft: '-5px' }}
        />
      </div>
    )
}

export default MissionTimelineItemStatus
