import { FormikTextInput } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { string } from 'yup'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemTraining: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const schema = {
    trainingType: string().required()
  }
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      schema={schema}
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
