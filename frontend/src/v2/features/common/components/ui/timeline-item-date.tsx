import Text from '@common/components/ui/text'
import { THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { useDate } from '../../hooks/use-date'

type TimelineItemDateProps = {
  date?: string
}

const TimelineItemDate: React.FC<TimelineItemDateProps> = ({ date }) => {
  const { formatTime, formatShortDate } = useDate()
  return (
    <Stack direction="column" alignItems="flex-start">
      <Stack.Item>
        <Text as="h3" color={THEME.color.slateGray} weight="bold">
          {formatShortDate(date)}
        </Text>
      </Stack.Item>
      <Stack.Item>
        <Text as="h3" color={THEME.color.slateGray} weight="normal">
          à {formatTime(date)}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default TimelineItemDate
