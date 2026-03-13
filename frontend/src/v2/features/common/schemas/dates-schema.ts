import { array, date } from 'yup'

interface DateRangeSchemaOptions {
  required?: boolean
  startBeforeEndOnly?: boolean
}

const startBeforeEndTest = {
  name: 'start-before-end',
  message: 'La date de début doit être antérieure ou égale à la date de fin',
  test: (dates: (Date | undefined)[] | null | undefined) => {
    if (!dates || dates.length !== 2) return true
    const [start, end] = dates
    if (!start || !end) return true
    return start <= end
  }
}

export const createDateRangeSchema = (options: DateRangeSchemaOptions = {}) => {
  const { required = false, startBeforeEndOnly = false } = options

  if (startBeforeEndOnly) {
    return {
      dates: array()
        .of(date())
        .nullable()
        .test(startBeforeEndTest.name, startBeforeEndTest.message, startBeforeEndTest.test)
    }
  }

  if (!required) {
    return {
      dates: array().of(date()).nullable()
    }
  }

  return {
    dates: array()
      .of(date().required('Date manquante ou malformatée'))
      .length(2, 'Les deux dates sont requises')
      .required('Les dates sont requises')
      .test(startBeforeEndTest.name, startBeforeEndTest.message, startBeforeEndTest.test)
  }
}

// Keep for backwards compatibility
export const DateRangeDefaultSchema = createDateRangeSchema({ required: true })

const getDateRangeSchema = (options: DateRangeSchemaOptions = {}) => {
  return createDateRangeSchema(options)
}

export default getDateRangeSchema
