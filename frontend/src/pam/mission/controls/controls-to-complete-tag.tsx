import { Accent, Tag, TagBullet, TagProps, THEME } from '@mtes-mct/monitor-ui'

type ControlsToCompleteTagProps = TagProps & {
  amountOfControlsToComplete?: number
}

const ControlsToCompleteTag: React.FC<ControlsToCompleteTagProps> = ({amountOfControlsToComplete, ...tagProps}) => {
  if (!amountOfControlsToComplete || amountOfControlsToComplete == 0) {
    return null
  }
  return (
    <Tag
      bullet={TagBullet.DISK}
      bulletColor={THEME.color.maximumRed}
      accent={Accent.PRIMARY}
      isLight={tagProps.isLight}
    >
      <b>{`${amountOfControlsToComplete} ${
        amountOfControlsToComplete > 1 ? 'types' : 'type'
      } de contrôles à compléter`}</b>
    </Tag>
  )
}

export default ControlsToCompleteTag
