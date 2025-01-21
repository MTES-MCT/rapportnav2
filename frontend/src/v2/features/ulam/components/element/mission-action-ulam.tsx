import { MissionStatusEnum } from '@common/types/mission-types'
import { FC } from 'react'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import useGetActionQuery from '../../../common/services/use-mission-action'
import MissionActionUlamBody from './mission-action-ulam-body'
import MissionActionUlamHeader from './mission-action-ulam-header'

interface MissionActionProps {
  actionId?: string
  missionId: number
  missionStatus?: MissionStatusEnum
}

const MissionActionUlam: FC<MissionActionProps> = ({ missionStatus, missionId, actionId }) => {
  const query = useGetActionQuery(missionId, actionId)
  return (
    <MissionPageSectionWrapper
      hide={!actionId}
      sectionHeader={
        query.data && (
          <MissionActionUlamHeader missionId={Number(missionId)} action={query.data} missionStatus={missionStatus} />
        )
      }
      sectionBody={
        <MissionActionUlamBody
          action={query.data}
          error={query.error}
          isLoading={query.isLoading}
          missionId={Number(missionId)}
        />
      }
    />
  )
}

export default MissionActionUlam
