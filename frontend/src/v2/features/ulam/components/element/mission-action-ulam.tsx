import { useStore } from '@tanstack/react-store'
import { FC } from 'react'
import { store } from '../../../../store'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import useGetActionQuery from '../../../common/services/use-mission-action'
import MissionActionUlamBody from './mission-action-ulam-body'
import MissionActionUlamHeader from './mission-action-ulam-header'

interface MissionActionProps {
  actionId?: string
  missionId: number
}

const MissionActionUlam: FC<MissionActionProps> = ({ missionId, actionId }) => {
  const query = useGetActionQuery(missionId, actionId)
  const status = useStore(store, state => state.mission.status)
  return (
    <MissionPageSectionWrapper
      hide={!actionId}
      sectionHeader={
        query.data && (
          <MissionActionUlamHeader missionId={Number(missionId)} action={query.data} missionStatus={status} />
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
