import { ControlType } from '@common/types/control-types.ts'
import { FieldArray, FieldArrayRenderProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import MissionIncompleteControlTag from '../../../common/components/ui/mission-incomplete-control-tag.tsx'
import MissionTargetControlNav from '../../../mission-target/components/elements/mission-target-control-nav.tsx'
import { ActionFishControlInput } from '../../types/action-type.ts'

const FishControlOthers: FC<{
  values: ActionFishControlInput
  controlsToComplete: ControlType[]
}> = ({ controlsToComplete }) => {
  return (
    <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%', paddingTop: '2rem' }}>
      {(controlsToComplete?.length ?? 0) > 0 && (
        <Stack.Item style={{ width: '100%' }}>
          <Stack.Item alignSelf="flex-end">
            <MissionIncompleteControlTag isLight={true} nbrIncompleteControl={controlsToComplete?.length} />
          </Stack.Item>
        </Stack.Item>
      )}
      <Stack.Item style={{ width: '100%' }}>
        <FieldArray name="targets">
          {(fieldArray: FieldArrayRenderProps) => (
            <MissionTargetControlNav
              name="targets"
              fieldArray={fieldArray}
              controlsToComplete={controlsToComplete}
              label={`Autre(s) contrôle(s) effectué(s) par l’unité sur le navire`}
            />
          )}
        </FieldArray>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlOthers
