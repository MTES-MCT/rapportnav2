import Text from '@common/components/ui/text'
import { Action, ActionStatus } from '@common/types/action-types'
import { mapStatusToText, statusReasonToHumanString } from '@common/utils/status-utils'
import { THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

const MissionTimelineItemStatusCardFooter: FC<{ prevAction?: Action }> = ({ prevAction }) => {
  const data = prevAction?.data as unknown as ActionStatus
  return (
    <>
      {!!data && (
        <Text as={'h3'} color={THEME.color.slateGray} fontStyle={'italic'}>
          {`${mapStatusToText(data?.status)} ${data?.reason ? `- ${statusReasonToHumanString(data?.reason)} ` : ''}- fin`}
        </Text>
      )}
    </>
  )
}

export default MissionTimelineItemStatusCardFooter
