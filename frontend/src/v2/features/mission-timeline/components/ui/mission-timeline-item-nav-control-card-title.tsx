import { controlMethodToHumanString, vesselTypeToHumanString } from '@common/utils/control-utils'
import { FC } from 'react'
import { ActionType } from '../../../common/types/action-type'
import { SectorType } from '../../../common/types/sector-types'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'

type T = { [key in ActionType]?: (action: MissionTimelineAction) => string }
const TITLE_REGISTRY: T = {
  [ActionType.OTHER_CONTROL]: () => `Autre`,
  [ActionType.CONTROL_SLEEPING_FISHING_GEAR]: () => `d'engins de pêche dormant`,
  [ActionType.CONTROL]: (action: MissionTimelineAction) =>
    `${controlMethodToHumanString(action?.controlMethod)} - ${vesselTypeToHumanString(action?.vesselType)}`,
  [ActionType.CONTROL_NAUTICAL_LEISURE]: () => `de loisirs nautiques`,
  [ActionType.CONTROL_SECTOR]: (action: MissionTimelineAction) =>
    `d'établissement - filière ${!action.sectorType ? '' : action.sectorType === SectorType.FISHING ? 'pêche' : 'plaisance'}`
}

const MissionTimelineItemNavControlCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  if (!action || !action.type) return <></>
  const getTitleFn = TITLE_REGISTRY[action.type]
  if (!getTitleFn) return <></>
  return <MissionTimelineItemCardTitle text="Contrôles" bold={getTitleFn(action)} />
}

export default MissionTimelineItemNavControlCardTitle
