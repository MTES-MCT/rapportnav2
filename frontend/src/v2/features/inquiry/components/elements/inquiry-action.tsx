import { FC } from 'react'
import PageSectionWrapper from '../../../common/components/layout/page-section-wrapper'
import useGetActionQuery from '../../../common/services/use-action'
import InquiryActionBody from './inquiry-action-body'
import InquiryActionHeader from './inquiry-action-header'

interface InquiryActionProps {
  actionId?: string
  inquiryId?: string
}

const InquiryAction: FC<InquiryActionProps> = ({ inquiryId, actionId }) => {
  const { data: action, error, isLoading } = useGetActionQuery(inquiryId, actionId)
  return (
    <PageSectionWrapper
      hide={!actionId}
      sectionHeader={action && inquiryId ? <InquiryActionHeader inquiryId={inquiryId} action={action} /> : undefined}
      sectionBody={
        inquiryId ? (
          <InquiryActionBody action={action} error={error} isLoading={isLoading} ownerId={inquiryId} />
        ) : undefined
      }
    />
  )
}

export default InquiryAction
