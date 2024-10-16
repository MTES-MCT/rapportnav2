import Text from '@common/components/ui/text'
import { Action } from '@common/types/action-types'
import { actionTargetTypeLabels, EnvActionControl, MissionSourceEnum } from '@common/types/env-mission-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

const MissionTimelineItemControlCardSubtitle: FC<{ action?: Action }> = ({ action }) => {
  if (action?.source !== MissionSourceEnum.MONITORENV) return <></>
  const data = action?.data as unknown as EnvActionControl
  const plural = data.actionNumberOfControls && data.actionNumberOfControls > 1 ? 's' : ''
  return (
    <Text as="h3" weight="normal" color={THEME.color.slateGray}>
      <b>{plural ? `${data.actionNumberOfControls} contrôle${plural}` : 'Nombre de contrôles inconnu'}</b>
      &nbsp;
      {`réalisé${plural} sur des cibles de type`}
      &nbsp;
      <b>
        {data?.actionTargetType ? actionTargetTypeLabels[data.actionTargetType]?.libelle?.toLowerCase() : 'inconnu'}
      </b>
    </Text>
  )
}

export default MissionTimelineItemControlCardSubtitle
