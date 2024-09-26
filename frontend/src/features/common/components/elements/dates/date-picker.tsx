import { FC, useState } from 'react'
import { DatePickerProps } from '@mtes-mct/monitor-ui/fields/DatePicker'
import { DatePicker as MonitorUIDatePicker } from '@mtes-mct/monitor-ui'
import { DatePicker as MonitorUIDatePicker32 } from 'rsuite'
import { formatInTimeZone } from 'date-fns-tz'
import { addMinutes, isValid } from 'date-fns'
import { TIME_ZONE } from '@common/utils/dates-for-machines.ts'
import {
  validate,
  preprocessDateForPicker,
  postprocessDateFromPicker
} from '@common/components/elements/dates/utils.ts'
import useMissionDates from '@features/pam/mission/hooks/use-mission-dates.tsx'

/**
 * An overlay to deal with correct timezones on MonitorUI's DatePicker
 * Their component retransforms dates so we need to re-add the timezone difference
 * @param props DatePickerProps
 */
const DatePicker: FC<DatePickerProps> = (props: DatePickerProps) => {
  const date = props.defaultValue && preprocessDateForPicker(new Date(props.defaultValue))

  const [missionStartDate, missionEndDate] = useMissionDates('11')

  const { error, setError } = useState<string | undefined>()

  const handleChange = (date?: Date) => {
    const correctedDate = date ? postprocessDateFromPicker(date) : undefined

    const validation = validate(date, date, date)

    if (validation?.ok) {
      if (props.onChange) {
        props.onChange(correctedDate)
      }
    } else {
      setError(validation?.message)
    }
  }

  return <MonitorUIDatePicker {...props} defaultValue={date} onChange={handleChange} error={error} />
}

export default DatePicker
