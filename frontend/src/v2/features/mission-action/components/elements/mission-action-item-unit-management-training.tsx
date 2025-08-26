import { FormikSelect } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { string } from 'yup'
import { MissionAction } from '../../../common/types/mission-action'
import { UnitManagementTrainingType } from '../../../common/types/unit-management-type'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const trainingTypeLabel = {
  [UnitManagementTrainingType.DIVING]: `Plongée`,
  [UnitManagementTrainingType.MAN_OVERBOARD_RECOVERY]: `Récupération d'homme à la mer`,
  [UnitManagementTrainingType.TECHNICAL_INTERVENTION_SHOOTING]: `Tirs et techniques d'interventions`
}

const MissionActionItemUnitManagementTraining: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const schema = {
    unitManagementTrainingType: string().required()
  }
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      schema={schema}
      onChange={onChange}
      data-testid={'action-unit-mangement-form'}
    >
      <Stack.Item style={{ width: '100%' }}>
        <FormikSelect
          isLight={true}
          isRequired={true}
          isErrorMessageHidden={true}
          label="Type d'entraînement"
          name="unitManagementTrainingType"
          options={
            Object.keys(UnitManagementTrainingType)?.map(key => ({
              value: key,
              label: trainingTypeLabel[key as keyof typeof UnitManagementTrainingType]
            })) ?? []
          }
        />
      </Stack.Item>
    </MissionActionItemGenericDateObservation>
  )
}
export default MissionActionItemUnitManagementTraining
