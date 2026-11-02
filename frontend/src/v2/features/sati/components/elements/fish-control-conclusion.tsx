import { FC } from 'react'
import { ActionFishControlInput } from '../../../mission-action/types/action-type.ts'
import FishControlSeizureSection from '../../../mission-control/components/ui/fish-control-seizure-section.tsx'
import InfractionFishSummary from '../../../mission-infraction/components/ui/infraction-fish-summary.tsx'
import ControlConclusionLayout from '../layout/control-conclusion-layout.tsx'

interface FishControlConclusionProps {
  values: ActionFishControlInput
}

const FishControlConclusion: FC<FishControlConclusionProps> = ({ values }) => {
  return (
    <ControlConclusionLayout
      sati={values?.sati}
      seizure={<FishControlSeizureSection action={values} />}
      infractionSummary={<InfractionFishSummary infractions={values?.fishInfractions ?? []} />}
    />
  )
}

export default FishControlConclusion
