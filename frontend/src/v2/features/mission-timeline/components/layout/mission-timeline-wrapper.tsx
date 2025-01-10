import { THEME } from '@mtes-mct/monitor-ui'
import { find } from 'lodash'
import { createElement, FC, Fragment, FunctionComponent } from 'react'
import { Divider, Stack } from 'rsuite'
import { useDate } from '../../../common/hooks/use-date'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineEmpty from '../ui/mission-timeline-empty'
import MissionTimelineError from '../ui/mission-timeline-error'
import MissionTimelineLoader from '../ui/mission-timeline-loader'

interface MissionTimelineProps {
  isError?: any
  missionId?: number
  isLoading?: boolean
  actions: MissionTimelineAction[]
  groupBy: 'startDateTimeUtc' | 'endDateTimeUtc'
  item: FunctionComponent<{ action: MissionTimelineAction; missionId?: number; prevAction?: MissionTimelineAction }>
}

const MissionTimelineWrapper: FC<MissionTimelineProps> = ({
  item,
  isError,
  missionId,
  actions,
  isLoading,
  groupBy
}) => {
  const { groupByDay } = useDate()
  if (isLoading) return <MissionTimelineLoader />
  if (actions?.length === 0) return <MissionTimelineEmpty />
  if (isError) return <MissionTimelineError error={isError} />

  return (
    <Stack direction="column" spacing={'1rem'} style={{ width: '100%' }} alignItems="stretch">
      {Object.entries(groupByDay(actions, groupBy)).map(([day, values], index) => (
        <Fragment key={day}>
          {index !== 0 && (
            <Stack.Item data-testid={'timeline-day-divider'}>
              <Divider style={{ backgroundColor: THEME.color.charcoal }} />
              <div style={{ padding: '0.25rem' }} />
            </Stack.Item>
          )}
          <Stack.Item style={{ marginRight: '1rem' }}>
            <Stack direction="column" spacing={'0.75rem'} style={{ width: '100%' }} alignItems="stretch">
              {(values as MissionTimelineAction[]).map(action =>
                createElement(item, {
                  key: action.id,
                  action,
                  missionId,
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

export default MissionTimelineWrapper
