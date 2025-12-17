import { boolean, mixed, number, object, string } from 'yup'
import { DateRangeDefaultSchema } from '../../common/schemas/dates-schema'
import { InquiryOriginType, InquiryTargetType } from '../../common/types/inquiry'

const inquiryDateObjectSchema = {
  endDateTimeUtc: string().required(''),
  startDateTimeUtc: string().required('')
}

const inquiryValidationSchema = {
  agentId: string().required('Un agent en charge est requis'),
  isSignedByInspector: boolean().required(),
  establishment: object({
    name: string().nullable()
  }).test('iscompleteCompany', `Un etablissement est requis pour ce type de cible`, function (value) {
    const { type } = this.parent
    if (InquiryTargetType.COMPANY !== type) return true
    return !!value?.name
  }),
  vessel: object({
    vesselId: number().nullable()
  }).test('iscompleteVessel', `Un navire est requis pour ce type de cible`, function (value) {
    const { type } = this.parent
    if (InquiryTargetType.VEHICLE !== type) return true
    return !!value?.vesselId
  }),
  type: mixed<InquiryTargetType>().oneOf(Object.values(InquiryTargetType)).required('Le type de cible requis'),
  origin: mixed<InquiryOriginType>().oneOf(Object.values(InquiryOriginType)).required('')
}

export const getInquirySchema = (isForm?: boolean) => {
  return object().shape({
    ...inquiryValidationSchema,
    ...(isForm ? DateRangeDefaultSchema : inquiryDateObjectSchema)
  })
}
