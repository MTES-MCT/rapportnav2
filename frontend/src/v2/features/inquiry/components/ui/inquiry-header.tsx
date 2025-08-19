import { FC } from 'react'
import PageHeaderWrapper from '../../../common/components/layout/page-header-wrapper'
import TimeConversion from '../../../common/components/ui/time-conversion'
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
      utcTime={<TimeConversion />}
      onClickClose={onClickClose}
    />
  )
}

export default InquiryHeader
