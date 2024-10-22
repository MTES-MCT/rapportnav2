import { FC } from 'react'

type MissionTimelineStatusColorTagProps = {
  style?: any
  color: string
}

export const MissionTimelineStatusColorTag: FC<MissionTimelineStatusColorTagProps> = ({ style, color }) => {
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
