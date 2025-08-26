import { FormikTextInput } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemTraining: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      onChange={onChange}
      data-testid={'action-unit-mangement-form'}
    >
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput
          isLight={true}
          name="trainingType"
          isRequired={true}
          isErrorMessageHidden={true}
          label="Nature de la formation"
        />
      </Stack.Item>
    </MissionActionItemGenericDateObservation>
  )
}
export default MissionActionItemTraining
