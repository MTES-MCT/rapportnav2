import Text from '@common/components/ui/text'
import { mapStatusToText, statusReasonToHumanString } from '@common/utils/status-utils'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'

const MissionTimelineItemStatusCardTitle: FC<{ action?: MissionTimelineAction; isSelected?: boolean }> = ({
  action,
  isSelected
}) => {
  return (
    <div style={{ display: 'flex', alignItems: 'center', maxWidth: 'calc(100% - 3rem)' }}>
      <Text
        as="h3"
        weight="medium"
        decoration={isSelected ? 'underline' : 'normal'}
        color={isSelected ? THEME.color.charcoal : THEME.color.slateGray}
        data-testid="timeline-item-status-description"
        style={{
          whiteSpace: 'nowrap',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
          textDecoration: isSelected ? 'underline' : 'none'
        }}
      >
        <b>{`${mapStatusToText(action?.status)} - début${
          !!action?.reason ? ' - ' + statusReasonToHumanString(action?.reason) : ''
        }`}</b>
        {!!action?.observations ? ' - ' + action?.observations : ''}
      </Text>
      <Icon.EditUnbordered size={20} color={THEME.color.slateGray} style={{ marginLeft: '0.5rem' }} />
    </div>
  )
}

export default MissionTimelineItemStatusCardTitle
