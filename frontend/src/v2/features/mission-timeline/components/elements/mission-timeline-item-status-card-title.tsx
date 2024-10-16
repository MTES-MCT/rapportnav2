import Text from '@common/components/ui/text'
import { Action, ActionStatus } from '@common/types/action-types'
import { mapStatusToText, statusReasonToHumanString } from '@common/utils/status-utils'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

const MissionTimelineItemStatusCardTitle: FC<{ action?: Action; isSelected?: boolean }> = ({ action, isSelected }) => {
  const data = action?.data as unknown as ActionStatus
  return (
    <div style={{ display: 'flex', alignItems: 'center' }}>
      <Text
        as="h3"
        weight="medium"
        decoration={isSelected ? 'underline' : 'normal'}
        color={isSelected ? THEME.color.charcoal : THEME.color.slateGray}
        data-testid="timeline-item-status-description"
      >
        <b>{`${mapStatusToText(data?.status)} - début${
          !!data?.reason ? ' - ' + statusReasonToHumanString(data?.reason) : ''
        }`}</b>
        {!!data?.observations ? ' - ' + data?.observations : ''}
      </Text>
      <Icon.EditUnbordered size={20} color={THEME.color.slateGray} style={{ marginLeft: '0.5rem' }} />
    </div>
  )
}

export default MissionTimelineItemStatusCardTitle
