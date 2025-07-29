import { FC } from 'react'
import PageSectionWrapper from '../../../../common/components/layout/page-section-wrapper.tsx'
import useGetMissionGeneralInformationQuery from '../../../../mission-general-infos/services/use-mission-general-information.tsx'
import MissionGeneralInformationHeader from '../../../../mission-general-infos/ui/mission-general-information-header.tsx'
import MissionGeneralInformationPamBody from './mission-general-information-pam-body.tsx'

interface MissionGeneralInformationProps {
  missionId?: string
}

const MissionGeneralInformationPam: FC<MissionGeneralInformationProps> = ({ missionId }) => {
  const { data: generalInfos, isLoading } = useGetMissionGeneralInformationQuery(missionId)
  if (isLoading) return <div>Chargement...</div>
  return (
    <PageSectionWrapper
      sectionHeader={<MissionGeneralInformationHeader />}
      sectionBody={<MissionGeneralInformationPamBody missionId={missionId} generalInfos={generalInfos} />}
    />
  )
}

export default MissionGeneralInformationPam
