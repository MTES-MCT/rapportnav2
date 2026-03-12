import React from 'react'
import useUpdateGeneralInfo from '../../../common/services/use-update-general-info.tsx'
import { MissionGeneralInfo2 } from '../../../common/types/mission-types.ts'
import MissionGeneralInformationForm from './mission-general-information-ulam-form.tsx'

type MissionGeneralInformationUlamBodyProps = {
  missionId?: string
  generalInfos?: MissionGeneralInfo2
}

const MissionGeneralInformationUlamBody: React.FC<MissionGeneralInformationUlamBodyProps> = ({
  missionId,
  generalInfos
}) => {
  const mutation = useUpdateGeneralInfo()

  const onChange = async (newGeneralInfo: MissionGeneralInfo2): Promise<void> => {
    await mutation.mutateAsync({ missionId, generalInfo: newGeneralInfo })
  }

  if (!generalInfos) return
  return <MissionGeneralInformationForm generalInfo2={generalInfos} onChange={onChange} />
}

export default MissionGeneralInformationUlamBody
