import { useStore } from '@tanstack/react-store'
import { FC } from 'react'
import { store } from '../../../../store'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer'
import PageSectionWrapper from '../../../common/components/layout/page-section-wrapper'
import GeneralInformationHeader from '../../../common/components/ui/general-information-header'
import { useDelay } from '../../../common/hooks/use-delay'
import { Inquiry } from '../../../common/types/inquiry'
import useGetInquiryQuery from '../../services/use-inquiry'
import useUpdateInquiryMutation from '../../services/use-update-inquiry'
import InquiryGeneralInfoForm from './inquiry-general-information-form'

interface InquiryGeneralInformationProps {
  inquiryId?: string
}

const InquiryGeneralInformation: FC<InquiryGeneralInformationProps> = ({ inquiryId }) => {
  const { handleExecuteOnDelay } = useDelay()
  const mutation = useUpdateInquiryMutation(inquiryId)
  const { data: inquiry, isLoading } = useGetInquiryQuery(inquiryId)

  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)

  const onChange = async (inquiryForm: Inquiry): Promise<void> => {
    await handleExecuteOnDelay(async () => {
      await mutation.mutateAsync(inquiryForm)
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
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
