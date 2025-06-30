import { array, date } from 'yup'

const getDateRangeSchema = (isMissionFinished?: boolean) => {
  return isMissionFinished
    ? {
        dates: array()
          .of(date().required('Dates must be dates'))
          .length(2, 'DateRange must have exactly two elements')
          .required('Dates are required')
      }
    : {
        dates: array().of(date()).nullable()
      }
}

export default getDateRangeSchema
