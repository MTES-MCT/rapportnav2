import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemOtherFieldActivity: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      onChange={onChange}
      showDivingCheckBox={true}
      data-testid={'action-contact-form'}
    />
  )
}

export default MissionActionItemOtherFieldActivity
