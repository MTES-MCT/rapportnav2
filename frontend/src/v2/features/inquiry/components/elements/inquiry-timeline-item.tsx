import { createElement, FC, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import { setTimelineCurrentIndex } from '../../../../store/slices/timeline-reducer'
import TimelineItemWrapper from '../../../common/components/layout/timeline-item-wrapper'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import { useInquiryTimelineRegistry } from '../../hooks/use-inquiry-timeline-registry'

interface InquiryTimelineItemProps {
  index: number
  baseUrl: string
  action: MissionTimelineAction
  prevAction?: MissionTimelineAction
}

const InquiryTimelineItem: FC<InquiryTimelineItemProps> = ({ index, action, prevAction, baseUrl }) => {
  const { actionId } = useParams()
  const { isIncomplete } = useTimeline()
  const { getTimeline } = useInquiryTimelineRegistry()
  const isSelected = action.id === actionId

  useEffect(() => {
    if (action.id === actionId) setTimelineCurrentIndex(index)
  }, [action.id, actionId, index, setTimelineCurrentIndex])

  return (
    <TimelineItemWrapper
      action={action}
      baseUrl={baseUrl}
      isSelected={isSelected}
      isIncomplete={isIncomplete(action)}
      style={getTimeline(action.type)?.style}
      card={createElement(getTimeline(action.type).component, {
        action,
        prevAction,
        isSelected,
        icon: getTimeline(action.type).icon,
        title: `${getTimeline(action.type).title} ${index}`
      })}
    />
  )
}

export default InquiryTimelineItem
