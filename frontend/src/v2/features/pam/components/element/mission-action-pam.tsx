import { MissionStatusEnum } from '@common/types/mission-types'
import { FC } from 'react'
import PageSectionWrapper from '../../../common/components/layout/page-section-wrapper'
import useGetActionQuery from '../../../common/services/use-action'
import MissionActionPamBody from './mission-action-pam-body'
import MissionActionPamHeader from './mission-action-pam-header'

interface MissionActionProps {
  actionId?: string
  missionId: string
  missionStatus?: MissionStatusEnum
}

const MissionActionPam: FC<MissionActionProps> = ({ missionId, actionId, missionStatus }) => {
  const { data: action, isLoading, error } = useGetActionQuery(missionId, actionId)
  return (
    <PageSectionWrapper
      hide={!actionId}
      sectionHeader={
        action && <MissionActionPamHeader missionId={missionId} action={action} missionStatus={missionStatus} />
      }
      sectionBody={<MissionActionPamBody action={action} error={error} isLoading={isLoading} missionId={missionId} />}
    />
  )
}

export default MissionActionPam
