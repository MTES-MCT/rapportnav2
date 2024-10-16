import { ActionStatusType } from '@common/types/action-types'
import { useActionStatus } from '@features/v2/common/hooks/use-action-status'
import { FC } from 'react'

export const MissionTimelineStatusColorTag: FC<{ style?: any; status: ActionStatusType }> = ({ style, status }) => {
  const { color } = useActionStatus(status)
  return (
    <div
      style={
        style || {
          backgroundColor: color,
          width: '16px',
          height: '16px',
          borderRadius: '16px'
        }
      }
    >
      &nbsp;
    </div>
  )
}
