import { MissionStatusEnum } from '@common/types/mission-types'
import { FC } from 'react'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import useGetActionQuery from '../../../common/services/use-mission-action'
import MissionActionPamBody from './mission-action-pam-body'
import MissionActionPamHeader from './mission-action-pam-header'

interface MissionActionProps {
  actionId?: string
  missionId: number
  missionStatus?: MissionStatusEnum
}

const MissionActionPam: FC<MissionActionProps> = ({ missionId, actionId, missionStatus }) => {
  const { data: action, isLoading, error } = useGetActionQuery(missionId, actionId)
  return (
    <MissionPageSectionWrapper
      hide={!actionId}
      sectionHeader={
        action && <MissionActionPamHeader missionId={Number(missionId)} action={action} missionStatus={missionStatus} />
      }
      sectionBody={
        <MissionActionPamBody action={action} error={error} isLoading={isLoading} missionId={Number(missionId)} />
      }
    />
  )
}

export default MissionActionPam
