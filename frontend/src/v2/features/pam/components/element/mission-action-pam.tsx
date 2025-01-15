import { useStore } from '@tanstack/react-store'
import { FC } from 'react'
import { store } from '../../../../store'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import useGetActionQuery from '../../../common/services/use-mission-action'
import MissionActionPamBody from './mission-action-pam-body'
import MissionActionPamHeader from './mission-action-pam-header'

interface MissionActionProps {
  actionId?: string
  missionId: number
}

const MissionActionPam: FC<MissionActionProps> = ({ missionId, actionId }) => {
  const query = useGetActionQuery(missionId, actionId)
  const status = useStore(store, state => state.mission.status)
  return (
    <MissionPageSectionWrapper
      hide={!actionId}
      sectionHeader={
        query.data && (
          <MissionActionPamHeader missionId={Number(missionId)} action={query.data} missionStatus={status} />
        )
      }
      sectionBody={
        <MissionActionPamBody
          action={query.data}
          error={query.error}
          isLoading={query.isLoading}
          missionId={Number(missionId)}
        />
      }
    />
  )
}

export default MissionActionPam
