import { THEME } from '@mtes-mct/monitor-ui'
import { DetailedHTMLProps, FC, JSX } from 'react'
import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import { ActionStyle } from '../../hooks/use-timeline-action'
import TimelineItemDate from '../ui/timeline-item-date'
import TimelineItemStatus from '../ui/timeline-item-status'

const DivStyled = styled(
  ({
    isSelected,
    ...props
  }: { isSelected: Boolean } & DetailedHTMLProps<React.HTMLAttributes<HTMLDivElement>, HTMLDivElement>) => (
    <div {...props} />
  )
)(({ style, isSelected }) => ({
  cursor: 'pointer',
  minHeight: `${style?.minHeight ? 0 : style?.borderColor ? '48px' : '52px'}`,
  background: `${style?.backgroundColor ?? 'inherit'} 0% 0% no-repeat padding-box`,
  border: `${style?.borderColor ? (isSelected ? `3px solid ${THEME.color.blueGray} !important` : `1px solid ${style.borderColor}`) : 'none'}`,
  textAlign: 'left',
  letterSpacing: 0
}))

interface TimelineItemWrapperProps {
  baseUrl: string
  card: JSX.Element
  style?: ActionStyle
  isSelected: boolean
  isIncomplete: boolean
  action: MissionTimelineAction
}

const TimelineItemWrapper: FC<TimelineItemWrapperProps> = ({
  baseUrl,
  action,
  card,
  style,
  isSelected,
  isIncomplete
}) => {
  const navigate = useNavigate()
  const handleClick = (actionId?: string) => navigate(`${baseUrl}/${actionId}`)

  return (
    <Stack direction="row" spacing={'0.5rem'} style={{ overflow: 'hidden' }}>
      <Stack.Item style={{ minWidth: '50px' }}>
        <TimelineItemDate date={action.startDateTimeUtc} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%', maxWidth: 'calc(100% - 5.4rem', overflow: 'hidden' }}>
        <DivStyled style={style} onClick={() => handleClick(action.id)} isSelected={isSelected}>
          {card}
        </DivStyled>
      </Stack.Item>
      <Stack.Item alignSelf={isIncomplete ? 'center' : 'stretch'}>
        <TimelineItemStatus
          type={action.type}
          status={action.status}
          completenessForStats={action.completenessForStats}
        />
      </Stack.Item>
    </Stack>
  )
}

export default TimelineItemWrapper
