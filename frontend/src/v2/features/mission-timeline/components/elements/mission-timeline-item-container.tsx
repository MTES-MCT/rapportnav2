import { THEME } from '@mtes-mct/monitor-ui'
import { DetailedHTMLProps, FC } from 'react'
import { useParams } from 'react-router-dom'
import styled from 'styled-components'
import { ActionStyle } from '../../../common/hooks/use-action-registry'

const DivStyled = styled(
  ({
    isCurrent,
    ...props
  }: { isCurrent: Boolean } & DetailedHTMLProps<React.HTMLAttributes<HTMLDivElement>, HTMLDivElement>) => (
    <div {...props} />
  )
)(({ style, isCurrent }) => ({
  cursor: 'pointer',
  minHeight: `${style?.minHeight ? 0 : style?.borderColor ? '48px' : '52px'}`,
  background: `${style?.backgroundColor ?? 'inherit'} 0% 0% no-repeat padding-box`,
  border: `${style?.borderColor ? (isCurrent ? `3px solid ${THEME.color.blueGray}` : `1px solid ${style.borderColor}`) : 'none'}`,
  textAlign: 'left',
  letterSpacing: 0
}))

interface MissionTimelineItemContainerProps {
  actionId?: string
  style?: ActionStyle
  children: JSX.Element
  onClick: (actionId?: string) => void
}

const MissionTimelineItemContainer: FC<MissionTimelineItemContainerProps> = ({
  style,
  actionId,
  onClick,
  children
}) => {
  const { actionId: currentActionId } = useParams()
  return (
    <DivStyled style={style} onClick={() => onClick(actionId)} isCurrent={actionId === currentActionId}>
      {children}
    </DivStyled>
  )
}

export default MissionTimelineItemContainer
