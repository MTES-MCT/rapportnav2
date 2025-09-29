import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'
import { EnvTheme } from '@common/types/env-themes.ts'

const MissionTimelineItemSurveillanceCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  return (
    <MissionTimelineItemCardTitle
      text="Surveillance"
      bold={action?.themes?.length ? action?.themes?.map((t: EnvTheme) => t.name).join(', ') : 'environnement marin'}
    />
  )
}

export default MissionTimelineItemSurveillanceCardTitle
