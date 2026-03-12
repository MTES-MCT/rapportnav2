import { FC } from 'react'
import { Stack } from 'rsuite'
import { string } from 'yup'
import { FormikTextInput } from '../../../common/components/ui/formik-text-input'
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
        <FormikTextInput name="trainingType" label="Nature de la formation (Nom)" />
      </Stack.Item>
    </MissionActionItemGenericDateObservation>
  )
}
export default MissionActionItemTraining
