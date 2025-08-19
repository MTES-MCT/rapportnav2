import Text from '@common/components/ui/text.tsx'
import { UTCDate } from '@date-fns/utc'
import { THEME } from '@mtes-mct/monitor-ui'
import { formatInTimeZone } from 'date-fns-tz'
import { useEffect, useState } from 'react'
import { useDate } from '../../hooks/use-date.tsx'

const TimeConversion = () => {
  const { formatTime } = useDate()
  const browserTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone

  const [now, setNow] = useState(() => new UTCDate())

  useEffect(() => {
    const interval = setInterval(() => {
      setNow(new UTCDate())
    }, 1000) // update every second

    return () => clearInterval(interval) // cleanup on unmount
  }, [])

  const utcTime = formatTime(now)
  const localBrowserNow = formatInTimeZone(now, browserTimezone, 'HH:mm')

  return (
    <Text as="h3" color={THEME.color.white} weight="bold">
      UTC: {utcTime} {' | '} Heure locale: {localBrowserNow}
    </Text>
  )
}

export default TimeConversion
