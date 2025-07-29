import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { useStore } from '@tanstack/react-store'
import { FC } from 'react'
import { store } from '../../../../store'
import ActionHeaderWrapper from '../../../common/components/layout/action-header-wrapper'
import ActionHeaderCompletenessForStats from '../../../common/components/ui/action-header-completeness-for-stats'
import { MissionAction } from '../../../common/types/mission-action'
import { OwnerType } from '../../../common/types/owner-type'
import { useInquiryTimelineRegistry } from '../../hooks/use-inquiry-timeline-registry'

export type InquiryActionHeaderProps = {
  inquiryId: string
  action: MissionAction
  missionStatus?: MissionStatusEnum
}

const InquiryActionHeader: FC<InquiryActionHeaderProps> = ({ action, inquiryId, missionStatus }) => {
  const { getTimeline } = useInquiryTimelineRegistry()
  const currentIndex = useStore(store, state => state.timeline.currentIndex)

  return (
    <ActionHeaderWrapper
      action={action}
      ownerId={inquiryId}
      ownerType={OwnerType.INQUIRY}
      missionStatus={missionStatus}
      icon={getTimeline(action.actionType).icon}
      title={`${getTimeline(action.actionType).title} ${currentIndex}`}
      completeness={
        <ActionHeaderCompletenessForStats
          isMissionFinished={false}
          networkSyncStatus={action?.networkSyncStatus}
          completenessForStats={action?.completenessForStats}
        />
      }
    />
  )
}

export default InquiryActionHeader
