import React from 'react'
import useUpdateGeneralInfo from '../../../../common/services/use-update-general-info.tsx'
import { MissionGeneralInfo2 } from '../../../../common/types/mission-types.ts'
import MissionGeneralInformationFormPam from './mission-general-information-form-pam.tsx'

type MissionGeneralInformationPamProps = {
  missionId?: string
  generalInfos?: MissionGeneralInfo2
}

const MissionGeneralInformationPamBody: React.FC<MissionGeneralInformationPamProps> = ({ missionId, generalInfos }) => {
  const mutation = useUpdateGeneralInfo()

  const onChange = async (newGeneralInfo: MissionGeneralInfo2): Promise<void> => {
    debugger
    await mutation.mutateAsync({ missionId, generalInfo: newGeneralInfo })
  }

  if (!generalInfos) return
  return <MissionGeneralInformationFormPam generalInfo2={generalInfos} onChange={onChange} />
}

export default MissionGeneralInformationPamBody
