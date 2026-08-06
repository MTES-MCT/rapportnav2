import { ControlType } from '@common/types/control-types.ts'
import { FieldArray, FieldArrayRenderProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import MissionIncompleteControlTag from '../../../common/components/ui/mission-incomplete-control-tag.tsx'
import { SatiModuleType } from '../../../common/types/sati.ts'
import { ActionFishControlInput } from '../../../mission-action/types/action-type.ts'
import MissionTargetControl from '../../../mission-target/components/elements/mission-target-control.tsx'

interface FishControlOthersProps {
  values: ActionFishControlInput
  controlsToComplete: ControlType[]
}

const FishControlOthers: FC<FishControlOthersProps> = ({ values, controlsToComplete }) => {
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
            <MissionTargetControl
              name="targets"
              fieldArray={fieldArray}
              controlsToComplete={controlsToComplete}
              label={`Autre(s) contrôle(s) effectué(s) par l’unité sur le navire`}
              controlsToExclude={values?.sati?.module === SatiModuleType.M3 ? [ControlType.NAVIGATION] : []}
            />
          )}
        </FieldArray>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlOthers
