import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'
import { EnvTheme } from '@common/types/env-themes.ts'

const MissionTimelineItemEnvControlCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  return (
    <MissionTimelineItemCardTitle
      text="Contrôle"
      bold={
        action && 'themes' in action && !!action?.themes?.length
          ? action?.themes?.map((theme: EnvTheme) => theme.name).join(', ')
          : 'environnement'
      }
    />
  )
}

export default MissionTimelineItemEnvControlCardTitle
