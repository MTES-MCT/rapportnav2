import Text from '@common/components/ui/text'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

type MissionControlTitleProps = {
  text?: string
  isToComplete?: boolean
}

export const MissionControlTitle = styled((props: MissionControlTitleProps) => (
  <div style={{ width: '100%', display: 'flex', flexDirection: 'row' }}>
    <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
      {props.text}
    </Text>
    {props.isToComplete && (
      <Icon.AttentionFilled
        data-testid={`control-incomplete-title`}
        style={{ marginLeft: 6 }}
        color={THEME.color.maximumRed}
      />
    )}
  </div>
))({
  width: '100%'
})
