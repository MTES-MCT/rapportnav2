import { ActionStatusType } from '@common/types/action-types'
import { FC } from 'react'
import { useActionStatus } from '../../../common/hooks/use-action-status'
import { TimelineStatusColorTag } from './mission-timeline-status-tag-color'

export const TimelineStatusTag: FC<{ style?: any; status?: ActionStatusType }> = ({ style, status }) => {
  const { color } = useActionStatus(status)
  return <TimelineStatusColorTag style={style} color={color} />
}
