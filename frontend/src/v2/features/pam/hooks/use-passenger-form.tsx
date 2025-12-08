import { useDate } from '../../common/hooks/use-date.tsx'
import { boolean, object, string } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { useMemo } from 'react'
import { MissionPassenger } from '../../common/types/passenger-type.ts'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { endOfDay, startOfDay } from 'date-fns'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'

export type MissionPassengerInput = Omit<MissionPassenger, 'startDate' | 'endDate'> & {
  dates: (Date | undefined)[]
}

export const usePassengerForm = (missionId?: string) => {
  const isMissionFinished = useMissionFinished(missionId)
  const missionDates = useMissionDates(missionId)
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const EMPTY_PASSENGER: MissionPassenger = {
    fullName: '',
    organization: undefined,
    isIntern: false,
    startDate: '',
    endDate: ''
  }

  const fromPassengerToInput = (data: MissionPassenger): MissionPassengerInput => {
    const startDate = preprocessDateForPicker(data.startDate)
    const endDate = preprocessDateForPicker(data.endDate)
    return {
      ...data,
      dates: [startDate, endDate]
    }
  }

  const fromInputToPassenger = (value: MissionPassengerInput): MissionPassenger => {
    const { dates, ...rest } = value
    const startDate = postprocessDateFromPicker(dates[0])
    const endDate = postprocessDateFromPicker(dates[1])
    return {
      ...rest,
      startDate: startDate ?? '',
      endDate: endDate ?? ''
    }
  }

  const getInitValue = (passenger?: MissionPassenger): MissionPassengerInput => {
    return fromPassengerToInput(passenger ?? EMPTY_PASSENGER)
  }

  const validationSchema = useMemo(
    () =>
      object().shape({
        fullName: string().required('Nom requis.'),
        organization: string().required('Organisation requise.'),
        isIntern: boolean(),
        ...getDateRangeSchema({
          isMissionFinished,
          missionStartDate: missionDates.startDateTimeUtc
            ? startOfDay(missionDates.startDateTimeUtc).toISOString()
            : undefined,
          missionEndDate: missionDates.endDateTimeUtc
            ? endOfDay(missionDates.endDateTimeUtc).toISOString()
            : undefined
        })
      }),
    [isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc]
  )

  return {
    EMPTY_PASSENGER,
    getInitValue,
    fromInputToPassenger,
    validationSchema
  }
}
