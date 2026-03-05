import { FormikDateRangePicker, FormikDateRangePickerProps } from '../ui/formik-date-range-picker.tsx'
import { allowedRange } from 'rsuite/cjs/DateRangePicker/disabledDateUtils'
import { UTCDate } from '@date-fns/utc'
import { useMissionDates } from '../../hooks/use-mission-dates.tsx'

type MissionBoundFormikDateRangePickerProps = FormikDateRangePickerProps & {
  missionId?: string
}

const MissionBoundFormikDateRangePicker = ({ missionId, ...props }: MissionBoundFormikDateRangePickerProps) => {
  const { startDateTimeUtc, endDateTimeUtc } = useMissionDates(missionId)

  return (
    <FormikDateRangePicker
      shouldDisableDate={allowedRange(new UTCDate(startDateTimeUtc), new UTCDate(endDateTimeUtc))}
      {...props}
    />
  )
}

export default MissionBoundFormikDateRangePicker
