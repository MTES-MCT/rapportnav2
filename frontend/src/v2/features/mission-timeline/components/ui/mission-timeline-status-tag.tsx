import { ActionStatusType } from '@common/types/action-types'
import { FC } from 'react'
import { useActionStatus } from '../../../common/hooks/use-action-status'
import { MissionTimelineStatusColorTag } from './mission-timeline-status-tag-color'

export const MissionTimelineStatusTag: FC<{ style?: any; status: ActionStatusType }> = ({ style, status }) => {
  const { color } = useActionStatus(status)
  return <MissionTimelineStatusColorTag style={style} color={color} />
}
