import { FC } from 'react'
import MissionPageSectionWrapper from '../../../../common/components/layout/mission-page-section-wrapper.tsx'
import useGetMissionGeneralInformationQuery from '../../../../mission-general-infos/services/use-mission-general-information.tsx'
import MissionGeneralInformationPamBody from './mission-general-information-pam-body.tsx'
import MissionGeneralInformationHeader from '../../../../mission-general-infos/ui/mission-general-information-header.tsx'

interface MissionGeneralInformationProps {
  missionId: number
}

const MissionGeneralInformationPam: FC<MissionGeneralInformationProps> = ({ missionId }) => {
  const { data: generalInfos, isLoading } = useGetMissionGeneralInformationQuery(missionId)
  if (isLoading) return <div>Chargement...</div>
  return (
    <MissionPageSectionWrapper
      sectionHeader={<MissionGeneralInformationHeader />}
      sectionBody={<MissionGeneralInformationPamBody missionId={missionId} generalInfos={generalInfos} />}
    />
  )
}

export default MissionGeneralInformationPam
