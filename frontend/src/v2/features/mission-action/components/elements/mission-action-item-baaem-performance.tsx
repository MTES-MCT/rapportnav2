import { Action } from '@common/types/action-types'
import { FC } from 'react'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemBAAEMPermanence: FC<{ action: Action; onChange: (newAction: Action) => void }> = ({
  action,
  onChange
}) => {
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      onChange={onChange}
      data-testid={'action-baaem-permanence'}
    />
  )
}

export default MissionActionItemBAAEMPermanence
