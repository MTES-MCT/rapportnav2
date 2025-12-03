import { FC } from 'react'
import { ActionStatusType } from '@common/types/action-types'
import { Accent, Icon, IconButton, THEME } from '@mtes-mct/monitor-ui'
import { TimelineStatusTag } from '../../../mission-timeline/components/ui/mission-timeline-status-tag'
import { useActionStatus } from '../../hooks/use-action-status'
import { ActionType } from '../../types/action-type.ts'
import { CompletenessForStats, CompletenessForStatsStatusEnum } from '../../types/mission-types.ts'

type TimelineItemStatusProps = {
  type: ActionType
  status?: ActionStatusType
  completenessForStats?: CompletenessForStats
  isUnsync: boolean
}

const TimelineItemStatus: FC<TimelineItemStatusProps> = ({ type, status, completenessForStats, isUnsync }) => {
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
        <TimelineStatusTag
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

export default TimelineItemStatus
