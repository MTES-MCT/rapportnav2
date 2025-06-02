import Text from '@common/components/ui/text.tsx'
import { THEME } from '@mtes-mct/monitor-ui'
import { UTCDate } from '@date-fns/utc'
import { useDate } from '../../hooks/use-date.tsx'
import { formatInTimeZone } from 'date-fns-tz'

const MissionPageHeaderTimeConversion = () => {
  const { formatTime } = useDate()
  const utcNow = new UTCDate()
  const browserTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone
  const localBrowserNow = formatInTimeZone(utcNow, browserTimezone, 'HH:mm')
  return (
    <Text as={'h3'} color={THEME.color.white} weight={'bold'}>
      UTC: {formatTime(utcNow)}
      {' | '}
      Heure locale: {localBrowserNow}
    </Text>
  )
}

export default MissionPageHeaderTimeConversion
