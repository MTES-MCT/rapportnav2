import { THEME } from '@mtes-mct/monitor-ui'
import Text, { TextProps } from '../../../../common/components/ui/text'

type MissionTimelineItemCardTitleProps = {
  text?: string
  bold?: string
} & Omit<TextProps, 'as' | 'children'>

const MissionTimelineItemCardTitle: React.FC<MissionTimelineItemCardTitleProps> = ({ text, bold, ...props }) => {
  return (
    <Text as="h3" weight="medium" color={THEME.color.gunMetal} {...props}>
      {`${text} `}
      <b>{bold}</b>
    </Text>
  )
}

export default MissionTimelineItemCardTitle
