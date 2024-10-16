import { THEME } from '@mtes-mct/monitor-ui'
import Text from '../../../../common/components/ui/text'

const MissionTimelinItemTitle: React.FC<{ text?: string; divider?: string; bold?: string }> = ({
  text,
  bold,
  divider
}) => {
  return (
    <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
      {text}
      {divider}
      {bold && <b data-testid={'theme'}>{` ${divider} ${bold}`}</b>}
    </Text>
  )
}

export default MissionTimelinItemTitle
