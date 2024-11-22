import Text from '@common/components/ui/text'
import { mapStatusToText, statusReasonToHumanString } from '@common/utils/status-utils'
import { THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'

const MissionTimelineItemStatusCardFooter: FC<{ prevAction?: MissionTimelineAction }> = ({ prevAction }) => {
  return (
    <>
      {!!prevAction && (
        <Text as={'h3'} color={THEME.color.slateGray} fontStyle={'italic'}>
          {`${mapStatusToText(prevAction?.status)} ${prevAction?.reason ? `- ${statusReasonToHumanString(prevAction?.reason)} ` : ''}- fin`}
        </Text>
      )}
    </>
  )
}

export default MissionTimelineItemStatusCardFooter
