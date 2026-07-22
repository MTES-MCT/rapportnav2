import { FC } from 'react'
import { Stack } from 'rsuite'
import { string } from 'yup'
import { StyledFormikTextInputDelay } from '../../../common/components/ui/formik-text-input'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemTraining: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const schema = {
    trainingType: isMissionFinished ? string().required() : string().nullable()
  }
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      schema={schema}
      onChange={onChange}
      data-testid={'action-unit-mangement-form'}
    >
      <Stack.Item style={{ width: '100%' }}>
        <StyledFormikTextInputDelay name="trainingType" label="Nature de la formation (Nom)" />
      </Stack.Item>
    </MissionActionItemGenericDateObservation>
  )
}
export default MissionActionItemTraining
