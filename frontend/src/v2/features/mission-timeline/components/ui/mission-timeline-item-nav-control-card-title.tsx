import { FC } from 'react'
import { useControlRegistry } from '../../../mission-control/hooks/use-control-registry'
import { useVessel } from '../../../common/hooks/use-vessel'
import { ActionType } from '../../../common/types/action-type'
import { SectorType } from '../../../common/types/sector-types'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'

const MissionTimelineItemNavControlCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  const { getVesselTypeName } = useVessel()
  const { getControlMethod } = useControlRegistry()

  const getTitleRegistry: { [key in ActionType]?: (action: MissionTimelineAction) => string } = {
    [ActionType.OTHER_CONTROL]: () => `Autre`,
    [ActionType.CONTROL_SLEEPING_FISHING_GEAR]: () => `d'engins de pêche dormant`,
    [ActionType.CONTROL]: (action: MissionTimelineAction) =>
      `${getControlMethod(action?.controlMethod)} - ${getVesselTypeName(action?.vesselType)}`,
    [ActionType.CONTROL_NAUTICAL_LEISURE]: () => `de loisirs nautiques`,
    [ActionType.CONTROL_SECTOR]: (action: MissionTimelineAction) =>
      `d'établissement - filière ${!action.sectorType ? '' : action.sectorType === SectorType.FISHING ? 'pêche' : 'plaisance'}`
  }

  if (!action || !action.type) return <></>
  const getTitleFn = getTitleRegistry[action.type]
  if (!getTitleFn) return <></>
  return <MissionTimelineItemCardTitle text="Contrôles" bold={getTitleFn(action)} />
}

export default MissionTimelineItemNavControlCardTitle
