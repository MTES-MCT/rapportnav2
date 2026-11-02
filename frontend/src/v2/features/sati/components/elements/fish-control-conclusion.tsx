import { Message } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import { FC } from 'react'
import { Target, TargetType } from '../../../common/types/target-types.ts'
import { ActionFishControlInput } from '../../../mission-action/types/action-type.ts'
import TargetItemDefault from '../../../mission-target/components/elements/target-item-default.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import FishControlSeizureSection from '../../../mission-control/components/ui/fish-control-seizure-section.tsx'
import InfractionFishSummary from '../../../mission-infraction/components/ui/infraction-fish-summary.tsx'
import ControlConclusionLayout from '../layout/control-conclusion-layout.tsx'

interface FishControlConclusionProps {
  values: ActionFishControlInput
}

const FishControlConclusion: FC<FishControlConclusionProps> = ({ values }) => {
  const { defaultControlTypes } = useTarget()

  return (
    <ControlConclusionLayout
      sati={values?.sati}
      seizure={<FishControlSeizureSection action={values} />}
      infractionSummary={<InfractionFishSummary infractions={values?.fishInfractions ?? []} />}
      target={
        <Field name={`targets[0]`}>
          {(fieldFormik: FieldProps<Target>) => (
            <TargetItemDefault
              name={`targets[0]`}
              message={
                <Message level="WARNING">
                  Veuillez vous assurez que l'infraction n'a pas déjà été saisie par le CNSP
                </Message>
              }
              fieldFormik={fieldFormik}
              targetType={TargetType.DEFAULT}
              availableControlTypes={defaultControlTypes}
              title={`Ajout d'infraction (hors pol. pêche)`}
              buttonLabel={`Ajouter une infraction (hors pol. pêche)`}
            />
          )}
        </Field>
      }
    />
  )
}

export default FishControlConclusion
