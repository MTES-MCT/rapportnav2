import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import React from 'react'
import { useParams } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import PageWrapper from '../features/common/components/layout/page-wrapper.tsx'
import { inquiriesKeys } from '../features/common/services/query-keys.ts'
import { OwnerType } from '../features/common/types/owner-type.ts'
import InquiryAction from '../features/inquiry/components/elements/inquiry-action.tsx'
import InquiryGeneralInformation from '../features/inquiry/components/elements/inquiry-general-information.tsx'
import InquiryTimeline from '../features/inquiry/components/elements/inquiry-timeline.tsx'
import InquiryFooter from '../features/inquiry/components/ui/inquiry-footer.tsx'
import InquiryHeader from '../features/inquiry/components/ui/inquiry-header.tsx'

const InquiryPage: React.FC = () => {
  const { getUrl } = useGlobalRoutes()
  let { inquiryId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const exitMission = async () => navigateAndResetCache(getUrl(OwnerType.INQUIRY), inquiriesKeys.all())

  return (
    <PageWrapper
      header={<InquiryHeader onClickClose={exitMission} inquiryId={inquiryId} />}
      generalInformations={inquiryId ? <InquiryGeneralInformation inquiryId={inquiryId} /> : undefined}
      timeline={inquiryId ? <InquiryTimeline inquiryId={inquiryId} /> : undefined}
      action={<InquiryAction inquiryId={inquiryId} actionId={actionId} />}
      footer={<InquiryFooter exitMission={exitMission} inquiryId={inquiryId} />}
    />
  )
}

export default InquiryPage
