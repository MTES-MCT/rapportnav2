import { THEME } from '@mtes-mct/monitor-ui'
import { DetailedHTMLProps, FC } from 'react'
import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { ActionStyle } from '../../../common/hooks/use-action-registry'
import { ModuleType } from '../../../common/types/module-type'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemDate from '../ui/mission-timeline-item-date'
import MissionTimelineItemStatus from '../ui/mission-timeline-item-status'

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

interface MissionTimelineItemWrapperProps {
  card: JSX.Element
  style?: ActionStyle
  missionId?: number
  isSelected: boolean
  isIncomplete: boolean
  moduleType: ModuleType
  action: MissionTimelineAction
}

const MissionTimelineItemWrapper: FC<MissionTimelineItemWrapperProps> = ({
  action,
  card,
  style,
  missionId,
  moduleType,
  isSelected,
  isIncomplete
}) => {
  const navigate = useNavigate()
  const handleClick = (actionId?: string) => {
    navigate(`/v2/${moduleType}/missions/${missionId}/${actionId}`)
  }
  return (
    <Stack direction="row" spacing={'0.5rem'} style={{ overflow: 'hidden' }}>
      <Stack.Item style={{ minWidth: '50px' }}>
        <MissionTimelineItemDate date={action.startDateTimeUtc} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%', maxWidth: 'calc(100% - 5.4rem', overflow: 'hidden' }}>
        <DivStyled style={style} onClick={() => handleClick(action.id)} isSelected={isSelected}>
          {card}
        </DivStyled>
      </Stack.Item>
      <Stack.Item alignSelf={isIncomplete ? 'center' : 'stretch'}>
        <MissionTimelineItemStatus
          type={action.type}
          status={action.status}
          completenessForStats={action.completenessForStats}
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionTimelineItemWrapper
