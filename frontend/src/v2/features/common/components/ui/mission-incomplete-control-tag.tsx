import { Accent, Tag, TagProps, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

type MissionIncompleteControlTagProps = TagProps & {
  nbrIncompleteControl?: number
}

const MissionIncompleteControlTag: FC<MissionIncompleteControlTagProps> = ({ nbrIncompleteControl, ...tagProps }) => {
  if (!nbrIncompleteControl || nbrIncompleteControl === 0) {
    return null
  }
  return (
    <Tag
      withCircleIcon={true}
      accent={Accent.PRIMARY}
      isLight={tagProps.isLight}
      data-testid={'controls-to-complete-tag'}
      style={{
        alignItems: 'center',
        paddingTop: 2,
        paddingBottom: 2,
        paddingLeft: 2,
        paddingRight: 6
      }}
    >
      <div
        style={{
          height: 20,
          width: 20,
          borderRadius: '50%',
          color: 'white',
          fontWeight: 'bold',
          backgroundColor: THEME.color.maximumRed,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          marginRight: 4
        }}
      >{`${nbrIncompleteControl}`}</div>
      <div>
        <b>{`${nbrIncompleteControl > 1 ? 'types' : 'type'} de contrôles à compléter`}</b>
      </div>
    </Tag>
  )
}

export default MissionIncompleteControlTag
