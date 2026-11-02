import { Field, FieldProps } from 'formik'
import { FC } from 'react'
import { MissionActionData } from '../../../common/types/mission-action-data.ts'
import { Target, TargetType } from '../../../common/types/target-types.ts'
import TargetItemDefault from '../../../mission-target/components/elements/target-item-default.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import ControlConclusionLayout from '../layout/control-conclusion-layout.tsx'

interface FishingSectorControlConclusionProps {
  values: MissionActionData
}

const FishingSectorControlConclusion: FC<FishingSectorControlConclusionProps> = ({ values }) => {
  const { defaultControlTypes } = useTarget()

  return (
    <ControlConclusionLayout
      sati={values?.sati}
      target={
        <Field name={`targets[0]`}>
          {(fieldFormik: FieldProps<Target>) => (
            <TargetItemDefault
              name={`targets[0]`}
              fieldFormik={fieldFormik}
              title={`Ajout d'infraction`}
              targetType={TargetType.DEFAULT}
              buttonLabel={`Ajouter une infraction`}
              availableControlTypes={defaultControlTypes}
            />
          )}
        </Field>
      }
    />
  )
}

export default FishingSectorControlConclusion
