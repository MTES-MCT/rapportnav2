import { ActionStatusType } from '@common/types/action-types'
import { ActionTypeEnum } from '@common/types/env-mission-types'
import { CompletenessForStats, CompletenessForStatsStatusEnum } from '@common/types/mission-types'
import { Accent, Icon, IconButton, THEME } from '@mtes-mct/monitor-ui'
import { TimelineStatusTag } from '../../../mission-timeline/components/ui/mission-timeline-status-tag'
import { useActionStatus } from '../../hooks/use-action-status'

type TimelineItemStatusProps = {
  type: ActionTypeEnum
  status?: ActionStatusType
  completenessForStats?: CompletenessForStats
}

const TimelineItemStatus: React.FC<TimelineItemStatusProps> = ({ type, status, completenessForStats }) => {
  const { color } = useActionStatus(status)
  if (type !== ActionTypeEnum.STATUS && completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE)
    return (
      <div style={{ width: '15px', height: '100%', padding: '5px 0 5px 5px' }}>
        <TimelineStatusTag
          style={{
            height: '100%',
            borderRadius: '5px',
            backgroundColor: color
          }}
          status={status}
          data-testid={'timeline-item-status'}
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

export default TimelineItemStatus
