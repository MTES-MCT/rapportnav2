import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { ActionType } from '../../common/types/action-type'
import { Timeline, TimelineRegistry } from '../../mission-timeline/hooks/use-timeline'
import InquiryTimelineItemCard from '../components/ui/inquiry-timeline-item-card'

const TIMELINE_INQUIRY_REGISTRY: TimelineRegistry = {
  [ActionType.INQUIRY]: {
    style: {
      borderColor: THEME.color.lightGray
    },
    icon: Icon.ControlUnit,
    title: 'Session de travail',
    component: InquiryTimelineItemCard
  }
}

interface UlamTimelineRegistrHook {
  getTimeline: (actionType: ActionType) => Timeline
}

export function useInquiryTimelineRegistry(): UlamTimelineRegistrHook {
  const getTimeline = (actionType: ActionType) => TIMELINE_INQUIRY_REGISTRY[actionType] ?? ({} as Timeline)

  return { getTimeline }
}
