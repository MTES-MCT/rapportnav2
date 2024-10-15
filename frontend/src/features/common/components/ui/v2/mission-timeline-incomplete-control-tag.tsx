import { Accent, Icon, Tag, TagProps, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

type MissionTimelineIncompleteControlTagProps = TagProps & {
  nbrIncompleteControl?: number
}

const MissionTimelineIncompleteControlTag: FC<MissionTimelineIncompleteControlTagProps> = ({
  nbrIncompleteControl,
  ...tagProps
}) => {
  if (!nbrIncompleteControl || nbrIncompleteControl === 0) {
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
      <b>{`${nbrIncompleteControl} ${nbrIncompleteControl > 1 ? 'types' : 'type'} de contrôles à compléter`}</b>
    </Tag>
  )
}

export default MissionTimelineIncompleteControlTag
