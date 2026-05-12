import { FC } from 'react'
import { StyledFormikToggle } from '../../../common/components/ui/formik-styled-toogle'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemNavSurveillance: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      onChange={onChange}
      showDivingCheckBox={true}
      data-testid={'action-nav-surveillance-form'}
    >
      <StyledFormikToggle name="isWithinDepartment" label="Surveillance dans le département" />
    </MissionActionItemGenericDateObservation>
  )
}

export default MissionActionItemNavSurveillance
