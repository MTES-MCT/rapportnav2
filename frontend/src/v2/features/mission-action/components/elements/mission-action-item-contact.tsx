import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemContact: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericDateObservation action={action} onChange={onChange} data-testid={'action-contact-form'} />
  )
}

export default MissionActionItemContact
