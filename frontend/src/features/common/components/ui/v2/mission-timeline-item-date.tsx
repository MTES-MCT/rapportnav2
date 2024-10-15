import { useDate } from '@common/hooks/use-date'
import { THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import Text from '../text'

type MissionTimelineItemDateProps = {
  date?: string
}

const MissionTimelineItemDate: React.FC<MissionTimelineItemDateProps> = ({ date }) => {
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

export default MissionTimelineItemDate
