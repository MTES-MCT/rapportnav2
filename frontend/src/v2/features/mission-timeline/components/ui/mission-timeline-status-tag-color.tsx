import { FC } from 'react'

type TimelineStatusColorTagProps = {
  style?: any
  color: string
}

export const TimelineStatusColorTag: FC<TimelineStatusColorTagProps> = ({ style, color }) => {
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
