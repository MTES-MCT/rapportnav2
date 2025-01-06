import { MissionStatusEnum } from '@common/types/mission-types'
import { FC } from 'react'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import useGetActionQuery from '../../../common/services/use-mission-action'
import MissionActionUlamBody from './mission-action-ulam-body'
import MissionActionUlamHeader from './mission-action-ulam-header'

interface MissionActionProps {
  actionId?: string
  missionId: number
  status?: MissionStatusEnum
}

const MissionActionUlam: FC<MissionActionProps> = ({ missionId, actionId, status }) => {
  const query = useGetActionQuery(missionId, actionId)
  return (
    <MissionPageSectionWrapper
      hide={!actionId}
      sectionHeader={
        query.data && (
          <MissionActionUlamHeader missionId={Number(missionId)} action={query.data} missionStatus={status} />
        )
      }
      sectionBody={<MissionActionUlamBody missionId={Number(missionId)} actionId={actionId} />}
    />
  )
}

export default MissionActionUlam
