import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent, useMemo } from 'react'
import MissionTimelineCardWrapper from '../../../mission-timeline/components/layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemCardTag from '../../../mission-timeline/components/ui/mission-timeline-item-card-tag'
import { TimelineAction } from '../../../mission-timeline/types/mission-timeline-output'

type InquiryTimelineItemCardProps = {
  title?: string
  action?: TimelineAction
  icon?: FunctionComponent<IconProps>
}
const InquiryTimelineItemCard: FC<InquiryTimelineItemCardProps> = ({ title, icon, action }) => {
  const tags = useMemo(() => {
    if (!action) return []
    const newTags: string[] = []
    if (action.nbrOfHours) newTags.push(`${action.nbrOfHours}h de travail`)
    return [...newTags, ...(action.summaryTags ?? [])]
  }, [action])

  return (
    <MissionTimelineCardWrapper
      icon={icon}
      title={<>{title}</>}
      actionType={action?.type}
      networkSyncStatus={action?.networkSyncStatus}
      tags={<MissionTimelineItemCardTag tags={tags} />}
    />
  )
}

export default InquiryTimelineItemCard
