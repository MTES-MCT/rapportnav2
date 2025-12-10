import { FC } from 'react'
import PageSectionWrapper from '../../../common/components/layout/page-section-wrapper'
import GeneralInformationHeader from '../../../common/components/ui/general-information-header'
import { Inquiry } from '../../../common/types/inquiry'
import useGetInquiryQuery from '../../services/use-inquiry'
import useUpdateInquiryMutation from '../../services/use-update-inquiry'
import InquiryGeneralInfoForm from './inquiry-general-information-form'

interface InquiryGeneralInformationProps {
  inquiryId?: string
}

const InquiryGeneralInformation: FC<InquiryGeneralInformationProps> = ({ inquiryId }) => {
  const mutation = useUpdateInquiryMutation(inquiryId)
  const { data: inquiry, isLoading } = useGetInquiryQuery(inquiryId)

  const onChange = async (inquiryForm: Inquiry): Promise<void> => {
    await mutation.mutateAsync(inquiryForm)
  }

  if (isLoading) return <div>Chargement...</div>

  return (
    <PageSectionWrapper
      sectionHeader={<GeneralInformationHeader title="Contrôle croisé" />}
      sectionBody={inquiry ? <InquiryGeneralInfoForm inquiry={inquiry} onChange={onChange} /> : undefined}
    />
  )
}

export default InquiryGeneralInformation
