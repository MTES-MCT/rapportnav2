import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemCommunication: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      onChange={onChange}
      data-testid={'action-communication-form'}
    />
  )
}
export default MissionActionItemCommunication
