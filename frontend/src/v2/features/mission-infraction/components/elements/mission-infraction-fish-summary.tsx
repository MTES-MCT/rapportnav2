import React from 'react'
import MissionNatinfTagFish from '../../../common/components/ui/mission-natinfs-tag-fish.tsx'
import { FishInfraction } from '../../types/infraction-input.tsx'
import MissionInfractionSummary from '../layout/mission-infraction-summary.tsx'

interface MissionInfractionSummaryProps {
  title?: string
  showIndex?: boolean
  isActionDisabled?: boolean
  infractions: FishInfraction[]
}

const MissionInfractionFishSummary: React.FC<MissionInfractionSummaryProps> = ({
  title,
  showIndex,
  infractions,
  isActionDisabled
}) => {
  return (
    <MissionInfractionSummary
      title={title}
      showIndex={showIndex}
      infractions={infractions}
      isActionDisabled={isActionDisabled}
      summary={({ infraction }) => <MissionNatinfTagFish natinf={(infraction as FishInfraction).natinf} />}
    />
  )
}

export default MissionInfractionFishSummary
