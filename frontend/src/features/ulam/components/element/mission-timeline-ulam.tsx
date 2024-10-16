import { Action } from '@common/types/action-types'
import MissionTimelineEmpty from '@features/v2/common/components/ui/mission-timeline-empty'
import MissionTimelineError from '@features/v2/common/components/ui/mission-timeline-error'
import MissionTimelineLoader from '@features/v2/common/components/ui/mission-timeline-loader'
import { useDate } from '@features/v2/common/hooks/use-date'
import { useGetMissionTimelineQuery } from '@features/v2/common/services/use-mission-timeline'
import { THEME } from '@mtes-mct/monitor-ui'
import { find } from 'lodash'
import { FC, Fragment } from 'react'
import { Divider, Stack } from 'rsuite'
import MissionTimelineItemUlam from './mission-timeline-item-ulam'

interface MissionTimelineProps {
  missionId?: string
}

const MissionTimelineUlam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { groupByDay } = useDate()
  const { data: mission, loading, error } = useGetMissionTimelineQuery(missionId)

  if (loading) return <MissionTimelineLoader />
  if (error) return <MissionTimelineError error={error} />
  if (!mission || !mission.actions?.length) return <MissionTimelineEmpty />

  return (
    <Stack direction="column" spacing={'1rem'} style={{ width: '100%' }} alignItems="stretch">
      {Object.entries(groupByDay(mission.actions, 'startDateTimeUtc')).map(([day, actions], index) => (
        <Fragment key={day}>
          {index !== 0 && (
            <Stack.Item data-testid={'timeline-day-divider'}>
              <Divider style={{ backgroundColor: THEME.color.charcoal }} />
              <div style={{ padding: '0.25rem' }} />
            </Stack.Item>
          )}
          <Stack.Item style={{ marginRight: '1rem' }}>
            <Stack direction="column" spacing={'0.75rem'} style={{ width: '100%' }} alignItems="stretch">
              {(actions as Action[]).map(action => (
                <MissionTimelineItemUlam
                  key={action.id}
                  action={action}
                  prevAction={find(
                    mission.actions,
                    { type: action.type },
                    mission.actions.findIndex(item => item === action) + 1
                  )}
                  missionId={mission.id}
                />
              ))}
            </Stack>
          </Stack.Item>
        </Fragment>
      ))}
    </Stack>
  )
}

export default MissionTimelineUlam
