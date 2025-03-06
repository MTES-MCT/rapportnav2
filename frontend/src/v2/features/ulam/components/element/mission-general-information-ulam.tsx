import { FC } from 'react'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import MissionGeneralInformationHeader from '../../../common/components/ui/mission-general-information-header'
import useGetMissionGeneralInformationQuery from '../../../mission-general-infos/services/use-mission-general-information'
import MissionGeneralInformationUlamBody from './mission-general-information-ulam-body'

interface MissionGeneralInformationProps {
  missionId: number
}

const MissionGeneralInformationUlam: FC<MissionGeneralInformationProps> = ({ missionId }) => {
  const { data: generalInfos, isLoading } = useGetMissionGeneralInformationQuery(missionId)
  if (isLoading) return <div>Chargement...</div>
  return (
    <MissionPageSectionWrapper
      sectionHeader={<MissionGeneralInformationHeader />}
      sectionBody={<MissionGeneralInformationUlamBody missionId={missionId} generalInfos={generalInfos} />}
    />
  )
}

export default MissionGeneralInformationUlam
