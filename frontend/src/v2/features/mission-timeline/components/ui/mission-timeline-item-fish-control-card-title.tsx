import { formatMissionActionTypeForHumans } from '@common/types/fish-mission-types'
import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'
import { useVessel } from '../../../common/hooks/use-vessel.tsx'

const MissionTimelineItemFishControlCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  const { getFullVesselName } = useVessel()
  const parts: string[] = []

  parts.push(formatMissionActionTypeForHumans(action.fishActionType))
  parts.push(getFullVesselName(action?.vesselName, action?.flagState, action?.externalReferenceNumber))

  const text = parts.join(' - ')

  return <MissionTimelineItemCardTitle text={text} />
}

export default MissionTimelineItemFishControlCardTitle
