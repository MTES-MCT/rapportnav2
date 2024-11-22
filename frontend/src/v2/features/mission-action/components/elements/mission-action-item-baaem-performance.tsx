import { FC } from 'react'
import { MissionActionOutput } from '../../../common/types/mission-action-output'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemBAAEMPermanence: FC<{
  action: MissionActionOutput
  onChange: (newAction: MissionActionOutput) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      onChange={onChange}
      data-testid={'action-baaem-permanence'}
    />
  )
}

export default MissionActionItemBAAEMPermanence
