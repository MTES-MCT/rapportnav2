import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input.tsx'
import { ActionFishControlInput } from '../../types/action-type.ts'

const FishControlConclusion: FC<{
  values: ActionFishControlInput
}> = ({ values }) => {
  return (
    <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%', paddingTop: '2rem' }}>
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextAreaInput
          name="observationsByUnit"
          data-testid="observations-by-unit"
          label="Observation de l'unité sur le contrôle"
        />
      </Stack.Item>
    </Stack>
  )
}

export default FishControlConclusion
