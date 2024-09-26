import { FC, useEffect, useState } from 'react'
import { DatePickerProps as MonitorUIDatePickerProps } from '@mtes-mct/monitor-ui/fields/DatePicker'
import { DateRange } from '@mtes-mct/monitor-ui/types/definitions'
import { validateRange } from '@common/components/elements/dates/utils.ts'
import DatePicker from '@common/components/elements/dates/date-picker.tsx'
import Text from '@common/components/ui/text.tsx'
import { Stack } from 'rsuite'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import useIsMissionFinished from '@features/pam/mission/hooks/use-is-mission-finished.tsx'
import { useParams } from 'react-router-dom'

type DateRangePickerProps = MonitorUIDatePickerProps & {
  allowedRange?: [Date | undefined, Date | undefined]
  onChange: (nextValue?: DateRange) => void
}

/**
 * An overlay to deal with correct timezones on MonitorUI's DateRangePicker
 * Their component retransforms dates so we need to re-add the timezone difference
 * @param props DateRangePickerProps
 */
const DateRangePicker: FC<DateRangePickerProps> = ({
  selectedRange,
  allowedRange,
  onChange,
  ...props
}: DateRangePickerProps) => {
  const [startDate, setStartDate] = useState<Date | undefined>(undefined)
  const [endDate, setEndDate] = useState<Date | undefined>(undefined)
  const [error, setError] = useState<{ startDate?: string; endDate?: string } | undefined>()
  const { missionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  useEffect(() => {
    if (selectedRange) {
      const newStartDate = new Date(selectedRange[0])
      const newEndDate = selectedRange[1] ? new Date(selectedRange[1]) : undefined
      setStartDate(newStartDate)
      setEndDate(newEndDate)

      const validation = validateRange([newStartDate, newEndDate], allowedRange)
      if (validation?.ok) {
        setError(undefined)
      } else {
        setError({
          startDate: validation?.field === 'startDate' ? validation?.message : undefined,
          endDate: validation?.field === 'endDate' ? validation?.message : undefined
        })
      }
    }
  }, [selectedRange, allowedRange])

  const handleChange = (date?: Date, field: 'startDate' | 'endDate') => {
    if (date) {
      field === 'startDate' ? setStartDate(date) : setEndDate(date)
      const correctedRange = [
        field === 'startDate' ? date : startDate,
        field === 'endDate' ? date : endDate
      ] as DateRange

      onChange(correctedRange)
    }
  }

  return (
    <Stack direction={'column'} alignItems={'flex-start'}>
      <Stack.Item>
        <Label>{props.label}</Label>
      </Stack.Item>
      <Stack.Item>
        <Stack direction={'row'} spacing={'0.5rem'}>
          <Stack.Item>
            <DatePicker
              {...props}
              defaultValue={startDate}
              error={error?.startDate ? error?.startDate : isMissionFinished && !startDate ? 'error' : undefined}
              isErrorMessageHidden={true}
              onChange={(date: Date | undefined) => handleChange(date, 'startDate')}
              label={''}
              name={'startDate'}
              allowedRange={allowedRange}
            />
          </Stack.Item>
          <Stack.Item alignSelf={'center'}>
            <Text as={'h4'}>au</Text>
          </Stack.Item>
          <Stack.Item>
            <DatePicker
              {...props}
              defaultValue={endDate}
              error={error?.endDate ? error?.endDate : isMissionFinished && !endDate ? 'error' : undefined}
              isErrorMessageHidden={true}
              onChange={(date: Date | undefined) => handleChange(date, 'endDate')}
              label={''}
              name={'endDate'}
              allowedRange={allowedRange}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ marginTop: '0.2rem' }}>
        <Text as={'h3'} color={THEME.color.maximumRed}>
          {error?.endDate ?? error?.startDate ?? ''}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default DateRangePicker
