import { FC } from 'react'
import PageSectionWrapper from '../../../common/components/layout/page-section-wrapper'
import useGetInquiryTimelineQuery from '../../hooks/use-inquiry-timeline'
import InquiryTimelineBody from './inquiry-timeline-body'
import InquiryTimelineHeader from './inquiry-timeline-header'

interface InquiryTimelineProps {
  inquiryId: string
}

const InquiryTimeline: FC<InquiryTimelineProps> = ({ inquiryId }) => {
  const { data: actions, isError, isLoading } = useGetInquiryTimelineQuery(inquiryId)

  return (
    <PageSectionWrapper
      sectionHeader={<InquiryTimelineHeader inquiryId={inquiryId} />}
      sectionBody={
        <InquiryTimelineBody actions={actions} isError={isError} isLoading={isLoading} inquiryId={inquiryId} />
      }
    />
  )
}

export default InquiryTimeline
