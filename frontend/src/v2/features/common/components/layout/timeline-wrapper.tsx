import { THEME } from '@mtes-mct/monitor-ui'
import { find } from 'lodash'
import { createElement, FC, Fragment, FunctionComponent, useEffect } from 'react'
import { Divider, Stack } from 'rsuite'
import { setTimelineCompleteForStats } from '../../../../store/slices/timeline-complete-for-stats-reducer'
import MissionTimelineEmpty from '../../../mission-timeline/components/ui/mission-timeline-empty'
import MissionTimelineError from '../../../mission-timeline/components/ui/mission-timeline-error'
import MissionTimelineLoader from '../../../mission-timeline/components/ui/mission-timeline-loader'
import { useTimelineCompleteForStats } from '../../../mission-timeline/hooks/use-timeline-complete-for-stats'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import { useDate } from '../../hooks/use-date'

interface TimelineProps {
  isError?: any
  baseUrl: string
  isLoading?: boolean
  noTimelineMessage?: string
  actions: MissionTimelineAction[]
  groupBy: 'startDateTimeUtc' | 'endDateTimeUtc'
  item: FunctionComponent<{
    index: number
    baseUrl: string
    action: MissionTimelineAction
    prevAction?: MissionTimelineAction
  }>
}

const TimelineWrapper: FC<TimelineProps> = ({
  item,
  isError,
  baseUrl,
  actions,
  isLoading,
  groupBy,
  noTimelineMessage
}) => {
  console.log(baseUrl)
  const { groupByDay } = useDate()
  const { computeCompleteForStats } = useTimelineCompleteForStats()
  useEffect(() => {
    const completenessForStats = computeCompleteForStats(actions)
    setTimelineCompleteForStats(completenessForStats)
  }, [actions])

  if (noTimelineMessage) return <MissionTimelineEmpty message={noTimelineMessage} />
  if (isLoading) return <MissionTimelineLoader />
  if (actions?.length === 0) return <MissionTimelineEmpty />
  if (isError) return <MissionTimelineError error={isError} />

  return (
    <Stack direction="column" spacing={'1rem'} style={{ width: '100%' }} alignItems="stretch">
      {Object.entries(groupByDay(actions, groupBy)).map(([day, values], indexGroupBy) => (
        <Fragment key={day}>
          {indexGroupBy !== 0 && (
            <Stack.Item data-testid={'timeline-day-divider'}>
              <Divider style={{ backgroundColor: THEME.color.charcoal }} />
              <div style={{ padding: '0.25rem' }} />
            </Stack.Item>
          )}
          <Stack.Item style={{ marginRight: '1rem' }}>
            <Stack direction="column" spacing={'0.75rem'} style={{ width: '100%' }} alignItems="stretch">
              {(values as MissionTimelineAction[]).map((action, index) =>
                createElement(item, {
                  index,
                  action,
                  baseUrl,
                  key: action.id,
                  prevAction: find(actions, { type: action.type }, actions.findIndex(value => value === action) + 1)
                })
              )}
            </Stack>
          </Stack.Item>
        </Fragment>
      ))}
    </Stack>
  )
}

export default TimelineWrapper
