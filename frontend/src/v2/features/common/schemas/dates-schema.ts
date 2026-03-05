import { array, date } from 'yup'

export type DateRangeSchemaOptions = {
  isMissionFinished?: boolean
  missionStartDate?: string
  missionEndDate?: string
}

export const DateRangeDefaultSchema = {
  dates: array().length(2, 'Les dates de début et de fin doivent être renseignées').required('Les dates sont requises')
}

const getDateRangeSchema = (options?: DateRangeSchemaOptions | boolean) => {
  // Handle backward compatibility: if boolean passed, convert to options object
  const opts: DateRangeSchemaOptions = typeof options === 'boolean' ? { isMissionFinished: options } : (options ?? {})

  const { isMissionFinished, missionStartDate, missionEndDate } = opts

  const baseSchema = isMissionFinished
    ? array()
        .length(2, 'Les dates de début et de fin doivent être renseignées')
        .required('Les dates sont requises')
        .test('end-date-defined', `La date et l'heure de fin doit être renseignée`, function (value) {
          return !!value[1]
        })
        .test('is-after-start', `La date et l'heure de fin doit être antérieure à la date de début`, function (value) {
          if (!value[0] || !value[1]) return true
          return value && value[0] && new Date(value[1]) > new Date(value[0])
        })
    : array().of(date()).nullable()

  // Add mission date boundary validation
  const schemaWithMissionValidation = baseSchema.test(
    'within-mission-dates',
    "Les dates de l'action doivent être comprises dans les dates de la mission",
    dates => {
      if (!dates || dates.length === 0) return true
      if (!missionStartDate) return true

      const missionStartTime = new Date(missionStartDate).getTime()
      const missionEndTime = missionEndDate ? new Date(missionEndDate).getTime() : null

      // Normalize action dates to timestamps (dates can be Date objects or strings)
      const actionStartTime = dates[0] ? new Date(dates[0]).getTime() : null
      const actionEndTime = dates[1] ? new Date(dates[1]).getTime() : null

      // Action start must be >= mission start
      if (actionStartTime && actionStartTime < missionStartTime) return false

      // If mission has end date, validate against it
      if (missionEndTime) {
        // Action start must be <= mission end
        if (actionStartTime && actionStartTime > missionEndTime) return false

        // Action end must be <= mission end
        if (actionEndTime && actionEndTime > missionEndTime) return false
      }

      return true
    }
  )

  return {
    dates: schemaWithMissionValidation
  }
}

export type SingleDateSchemaOptions = {
  isMissionFinished?: boolean
  missionStartDate?: string
  missionEndDate?: string
}

export const getSingleDateSchema = (options?: SingleDateSchemaOptions) => {
  const { isMissionFinished, missionStartDate, missionEndDate } = options ?? {}

  const baseSchema = isMissionFinished ? date().required('La date doit être bien renseignée') : date().nullable()

  return baseSchema.test(
    'within-mission-dates',
    "Les dates de l'action doivent être comprises dans les dates de la mission",
    (dateValue: Date | null | undefined) => {
      if (!dateValue) return true
      if (!missionStartDate) return true

      const missionStartTime = new Date(missionStartDate).getTime()
      const missionEndTime = missionEndDate ? new Date(missionEndDate).getTime() : null

      const dateTime = dateValue.getTime()

      if (dateTime < missionStartTime) return false
      return !(missionEndTime && dateTime > missionEndTime)
    }
  )
}

export default getDateRangeSchema
