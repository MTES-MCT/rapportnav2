import Text from '@common/components/ui/text'
import { actionTargetTypeLabels, MissionSourceEnum } from '@common/types/env-mission-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'

const MissionTimelineItemControlCardSubtitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  if (action?.source !== MissionSourceEnum.MONITORENV) return <></>
  const plural = action.actionNumberOfControls && action.actionNumberOfControls > 1 ? 's' : ''
  return (
    <Text as="h3" weight="normal" color={THEME.color.slateGray}>
      <b>{plural ? `${action.actionNumberOfControls} contrôle${plural}` : 'Nombre de contrôles inconnu'}</b>
      &nbsp;
      {`réalisé${plural} sur des cibles de type`}
      &nbsp;
      <b>
        {action?.actionTargetType ? actionTargetTypeLabels[action.actionTargetType]?.libelle?.toLowerCase() : 'inconnu'}
      </b>
    </Text>
  )
}

export default MissionTimelineItemControlCardSubtitle
