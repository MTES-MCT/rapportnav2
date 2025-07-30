import { boolean, mixed, object, string } from 'yup'
import { DateRangeDefaultSchema } from '../../common/schemas/dates-schema'
import { InquiryOriginType, InquiryTargetType } from '../../common/types/inquiry'

const inquiryDateObjectSchema = {
  endDateTimeUtc: string().required(''),
  startDateTimeUtc: string().required('')
}

const inquiryValidationSchema = {
  agentId: string().required('Un agent en charge est requis'),
  isSignedByInspector: boolean().required(),
  vesselId: string()
    .nullable()
    .when('type', {
      is: InquiryTargetType.VEHICLE,
      then: schema => schema.nonNullable().required('Un navire est requis pour ce type de cible')
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
