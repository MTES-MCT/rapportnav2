import { Accent, Icon, Tag, TagProps, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

type ControlsToCompleteTagProps = TagProps & {
  amountOfControlsToComplete?: number
}

const ControlsToCompleteTag: FC<ControlsToCompleteTagProps> = ({ amountOfControlsToComplete, ...tagProps }) => {
  if (!amountOfControlsToComplete || amountOfControlsToComplete === 0) {
    return null
  }
  return (
    <Tag
      Icon={Icon.CircleFilled}
      iconColor={THEME.color.maximumRed}
      withCircleIcon={true}
      accent={Accent.PRIMARY}
      isLight={tagProps.isLight}
      data-testid={'controls-to-complete-tag'}
    >
      <b>{`${amountOfControlsToComplete} ${
        amountOfControlsToComplete > 1 ? 'types' : 'type'
      } de contrôles à compléter`}</b>
    </Tag>
  )
}

export default ControlsToCompleteTag
