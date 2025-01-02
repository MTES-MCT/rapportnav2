import { FC } from 'react'
import useGetActionQuery from '../../../common/services/use-mission-action'
import MissionActionWrapper from '../../../mission-action/components/layout/mission-action-wrapper'
import MissionActionItemPam from './mission-action-item-pam'

interface MissionActionProps {
  actionId?: string
  missionId: number
}

const MissionActionPamBody: FC<MissionActionProps> = ({ missionId, actionId }) => {
  const query = useGetActionQuery(missionId, actionId)
  return (
    <MissionActionWrapper
      action={query.data}
      missionId={missionId}
      isError={query.error}
      isLoading={query.isLoading}
      item={MissionActionItemPam}
    />
  )
}

export default MissionActionPamBody
