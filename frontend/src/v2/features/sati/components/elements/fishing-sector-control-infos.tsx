import { FormikProps } from 'formik'
import { FC } from 'react'
import { MissionActionData } from '../../../common/types/mission-action-data.ts'
import MissionActionItemSectorFishingControlForm from '../../../mission-action/components/ui/mission-action-control-sector-fishing-form.tsx'
import { ActionControlInput } from '../../../mission-action/types/action-type.ts'

interface FishingSectorControlInfosProps {
  values: MissionActionData
  formik: FormikProps<ActionControlInput>
}

const FishingSectorControlInfos: FC<FishingSectorControlInfosProps> = ({ values, formik }) => {
  return <MissionActionItemSectorFishingControlForm formik={formik} />
}

export default FishingSectorControlInfos
