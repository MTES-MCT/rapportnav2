import { Action } from '@common/types/action-types'
import { FC } from 'react'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemVigimer: FC<{ action: Action; onChange: (newAction: Action) => void }> = ({
  action,
  onChange
}) => {
  return (
    <MissionActionItemGenericDateObservation action={action} onChange={onChange} data-testid={'action-vigimer-form'} />
  )
}

export default MissionActionItemVigimer
