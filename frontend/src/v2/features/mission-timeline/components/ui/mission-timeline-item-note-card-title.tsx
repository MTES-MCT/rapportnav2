import Text, { TextProps } from '@common/components/ui/text'
import { THEME } from '@mtes-mct/monitor-ui'
import { MissionTimelineAction } from '../../types/mission-timeline-output'

type MissionTimelineItemNoteCardTitleProps = {
  text?: string
  action?: MissionTimelineAction
} & Omit<TextProps, 'as' | 'children'>

const MissionTimelineItemNoteCardTitle: React.FC<MissionTimelineItemNoteCardTitleProps> = ({
  text,
  action,
  ...props
}) => {
  return (
    <Text as="h3" weight="medium" color={THEME.color.gunMetal} {...props}>
      {`${action?.observations ?? text} `}
    </Text>
  )
}

export default MissionTimelineItemNoteCardTitle
