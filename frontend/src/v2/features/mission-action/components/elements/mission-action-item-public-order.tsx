import { Action } from '@common/types/action-types'
import { FC } from 'react'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemPublicOrder: FC<{ action: Action; onChange: (newAction: Action) => Promise<unknown> }> = ({
  action,
  onChange
}) => {
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      onChange={onChange}
      data-testid={'action-public-order-form'}
    />
  )
}

export default MissionActionItemPublicOrder
