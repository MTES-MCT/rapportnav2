import { UTCDate } from '@date-fns/utc'
import { FormikDatePicker, FormikDatePickerWithDateDateProps } from '@mtes-mct/monitor-ui'
import { allowedRange } from 'rsuite/cjs/DateRangePicker/disabledDateUtils'
import { useMissionDates } from '../../hooks/use-mission-dates.tsx'

type MissionBoundFormikDatePickerProps = FormikDatePickerWithDateDateProps & {
  missionId: string
}

const MissionBoundFormikDatePicker = ({ missionId, ...props }: MissionBoundFormikDatePickerProps) => {
  const { startDateTimeUtc, endDateTimeUtc } = useMissionDates(missionId)

  return (
    <FormikDatePicker
      shouldDisableDate={allowedRange(new UTCDate(startDateTimeUtc), new UTCDate(endDateTimeUtc))}
      {...props}
    />
  )
}

export default MissionBoundFormikDatePicker
