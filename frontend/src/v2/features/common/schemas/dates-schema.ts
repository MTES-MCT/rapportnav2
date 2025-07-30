import { array, date } from 'yup'

export const DateRangeDefaultSchema = {
  dates: array()
    .of(date().required('Dates must be dates'))
    .length(2, 'DateRange must have exactly two elements')
    .required('Dates are required')
}

const getDateRangeSchema = (isMissionFinished?: boolean) => {
  return isMissionFinished
    ? DateRangeDefaultSchema
    : {
        dates: array().of(date()).nullable()
      }
}

export default getDateRangeSchema
