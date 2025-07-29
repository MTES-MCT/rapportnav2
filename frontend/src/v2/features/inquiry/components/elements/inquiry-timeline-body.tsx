import { THEME } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import TimelineWrapper from '../../../common/components/layout/timeline-wrapper'
import { OwnerType } from '../../../common/types/owner-type'
import { TimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import InquiryTimelineItem from './inquiry-timeline-item'
import InquirySummary from './inquiry-timeline-summary'

interface InquiryTimelineBodyProps {
  inquiryId?: string
  isError?: boolean
  isLoading?: boolean
  actions: TimelineAction[]
}

const InquiryTimelineBody: FC<InquiryTimelineBodyProps> = ({ inquiryId, actions, isError, isLoading }) => {
  const { getUrl } = useGlobalRoutes()
  return (
    <Stack direction="column">
      <Stack.Item style={{ width: '100%' }}>
        <InquirySummary inquiryId={inquiryId} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Divider style={{ backgroundColor: THEME.color.charcoal }} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <TimelineWrapper
          isError={isError}
          actions={actions}
          isLoading={isLoading}
          groupBy="startDateTimeUtc"
          item={InquiryTimelineItem}
          baseUrl={`${getUrl(OwnerType.INQUIRY)}/${inquiryId}`}
        />
      </Stack.Item>
    </Stack>
  )
}

export default InquiryTimelineBody
