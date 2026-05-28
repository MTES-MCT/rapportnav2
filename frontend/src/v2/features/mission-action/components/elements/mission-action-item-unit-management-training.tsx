import { FormikMultiSelect, FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { array, number, string } from 'yup'
import useAgentsQuery from '../../../common/services/use-agents.tsx'
import { Agent } from '../../../common/types/crew-type.ts'
import { MissionAction } from '../../../common/types/mission-action'
import { UnitManagementTrainingType } from '../../../common/types/unit-management-type'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'

const trainingTypeLabel = {
  [UnitManagementTrainingType.SHOOTING]: `Tir`,
  [UnitManagementTrainingType.DIVING]: `Plongée`,
  [UnitManagementTrainingType.MAN_OVERBOARD_RECOVERY]: `Récupération d'homme à la mer`,
  [UnitManagementTrainingType.INTERVENTION_TACTICS]: `Techniques d'interventions`
}

const MissionActionItemUnitManagementTraining: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const { data: agents } = useAgentsQuery()
  const schema = {
    unitManagementTrainingType: isMissionFinished ? string().required() : string().nullable(),
    agentIds: isMissionFinished ? array().of(number()).min(1).required() : array().of(number()).nullable()
  }
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      schema={schema}
      onChange={onChange}
      showDivingCheckBox={true}
      data-testid={'action-unit-mangement-form'}
    >
      <>
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
        <Stack.Item style={{ width: '100%', marginTop: '1rem' }}>
          <Field name="agentIds">
            {({ field, form }: FieldProps) => (
              <FormikMultiSelect
                {...field}
                label="Agents participants"
                placeholder=""
                searchable={true}
                isLight={true}
                isRequired={true}
                isErrorMessageHidden={true}
                options={
                  agents?.map((agent: Agent) => ({
                    value: agent.id,
                    label: `${agent.firstName} ${agent.lastName}`
                  })) ?? []
                }
                value={field.value}
                onChange={(value: any) => form.setFieldValue(field.name, value)}
              />
            )}
          </Field>
        </Stack.Item>
      </>
    </MissionActionItemGenericDateObservation>
  )
}
export default MissionActionItemUnitManagementTraining
