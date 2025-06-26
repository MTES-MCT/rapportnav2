import { FC } from 'react'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import useGetMissionQuery from '../../../common/services/use-mission'
import useGetActionQuery from '../../../common/services/use-mission-action'
import MissionActionUlamBody from './mission-action-ulam-body'
import MissionActionUlamHeader from './mission-action-ulam-header'

interface MissionActionProps {
  actionId?: string
  missionId?: string
}

const MissionActionUlam: FC<MissionActionProps> = ({ missionId, actionId }) => {
  const { data: mission } = useGetMissionQuery(missionId)
  const { data: action, error, isLoading } = useGetActionQuery(missionId, actionId)
  return (
    <MissionPageSectionWrapper
      hide={!actionId}
      sectionHeader={
        action && missionId ? (
          <MissionActionUlamHeader missionId={missionId} action={action} missionStatus={mission?.status} />
        ) : undefined
      }
      sectionBody={
        missionId ? (
          <MissionActionUlamBody action={action} error={error} isLoading={isLoading} missionId={missionId} />
        ) : undefined
      }
    />
  )
}

export default MissionActionUlam
