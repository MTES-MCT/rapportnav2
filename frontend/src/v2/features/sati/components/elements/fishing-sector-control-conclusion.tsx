import { FC } from 'react'
import { MissionActionData } from '../../../common/types/mission-action-data.ts'
import ControlConclusionLayout from '../layout/control-conclusion-layout.tsx'

interface FishingSectorControlConclusionProps {
  values: MissionActionData
}

const FishingSectorControlConclusion: FC<FishingSectorControlConclusionProps> = ({ values }) => {
  return <ControlConclusionLayout sati={values?.sati} />
}

export default FishingSectorControlConclusion
