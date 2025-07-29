import { array, boolean, date, mixed, object, string } from 'yup'
import { InquiryOriginType, InquiryTargetType } from '../../common/types/inquiry'

export const inquiryValidationSchema = object().shape({
  agentId: string().required('Un agent en charge est requis'),
  isSignedByInspector: boolean().required(),
  vesselId: string()
    .nullable()
    .when('type', {
      is: InquiryTargetType.VEHICLE,
      then: schema => schema.nonNullable().required('Un navire est requis pour ce type de cible')
    }),
  type: mixed<InquiryTargetType>().oneOf(Object.values(InquiryTargetType)).required('Le type de cible requis'),
  origin: mixed<InquiryOriginType>().oneOf(Object.values(InquiryOriginType)).required(''),
  dates: array().of(date().required('')).length(2, '').required('Les dates de début et de fin sont obligatoires.')
})
