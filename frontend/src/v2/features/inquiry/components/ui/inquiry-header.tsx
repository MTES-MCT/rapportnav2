import { FC } from 'react'
import PageHeaderWrapper from '../../../common/components/layout/page-header-wrapper'
import MissionPageHeaderTimeConversion from '../../../common/components/ui/mission-page-header-time-conversion'
import { useDate } from '../../../common/hooks/use-date'
import useGetInquiryQuery from '../../services/use-inquiry'
import InquiryStatusTag from './inquiry-status-tag'

interface InquiryHeaderProps {
  inquiryId?: string
  onClickClose: () => void
}

const InquiryHeader: FC<InquiryHeaderProps> = ({ inquiryId, onClickClose }) => {
  const { formatInquiryName } = useDate()
  const { data: inquiry } = useGetInquiryQuery(inquiryId)
  return (
    <PageHeaderWrapper
      tags={<InquiryStatusTag status={inquiry?.status} />}
      date={<> {formatInquiryName(inquiry?.startDateTimeUtc)}</>}
      utcTime={<MissionPageHeaderTimeConversion />}
      onClickClose={onClickClose}
    />
  )
}

export default InquiryHeader
