import { FC } from 'react'
import { DatePickerProps } from '@mtes-mct/monitor-ui/fields/DatePicker'
import { DatePicker as MonitorUIDatePicker } from '@mtes-mct/monitor-ui'
import { formatInTimeZone } from 'date-fns-tz'
import { addMinutes, isValid } from 'date-fns'
import { TIME_ZONE } from '@common/utils/dates-for-machines.ts'

// Preprocessing before sending to datepicker
export const preprocessDateForPicker = (date?: Date): Date | undefined => {
  if (!date || !isValid(date)) {
    return
  }
  // Convert the local date to a UTC date that, when converted back to ISO string,
  // will represent the correct local time
  const localISOString = formatInTimeZone(date, TIME_ZONE, "yyyy-MM-dd'T'HH:mm:ss.SSS")
  return new Date(localISOString + 'Z') // Append 'Z' to treat it as UTC
}

// Postprocessing after receiving from datepicker
export const postprocessDateFromPicker = (date?: Date): Date | undefined => {
  if (!date || !isValid(date)) {
    return
  }
  // Get the local timezone offset in minutes
  const localOffset = new Date().getTimezoneOffset()

  // Add the offset to correct the date
  return addMinutes(date, localOffset)
}

/**
 * An overlay to deal with correct timezones on MonitorUI's DatePicker
 * Their component retransforms dates so we need to re-add the timezone difference
 * @param props DatePickerProps
 */
const DatePicker: FC<DatePickerProps> = (props: DatePickerProps) => {
  const date = props.defaultValue && preprocessDateForPicker(new Date(props.defaultValue))

  const handleChange = (date?: Date) => {
    const correctedDate = date ? postprocessDateFromPicker(date) : undefined
    if (props.onChange) {
      props.onChange(correctedDate)
    }
  }

  return <MonitorUIDatePicker {...props} defaultValue={date} onChange={handleChange} />
}

export default DatePicker
