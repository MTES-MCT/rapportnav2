import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent, useEffect, useState } from 'react'
import MissionTimelineCardWrapper from '../../../mission-timeline/components/layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemCardTag from '../../../mission-timeline/components/ui/mission-timeline-item-card-tag'
import { TimelineAction } from '../../../mission-timeline/types/mission-timeline-output'

type InquiryTimelineItemCardProps = {
  title?: string
  action?: TimelineAction
  icon?: FunctionComponent<IconProps>
}
const InquiryTimelineItemCard: FC<InquiryTimelineItemCardProps> = ({ title, icon, action }) => {
  const [tags, setTags] = useState<string[]>([])

  useEffect(() => {
    const newTags = []
    if (!action) return
    if (action.nbrOfHours) newTags.push(`${action.nbrOfHours}h de travail`)
    setTags([...newTags, ...(action.summaryTags ?? [])])
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
