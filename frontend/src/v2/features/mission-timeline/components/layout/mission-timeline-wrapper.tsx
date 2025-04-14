import { THEME } from '@mtes-mct/monitor-ui'
import { find } from 'lodash'
import { createElement, FC, Fragment, FunctionComponent, useEffect } from 'react'
import { Divider, Stack } from 'rsuite'
import { setTimelineCompleteForStats } from '../../../../store/slices/timeline-complete-for-stats-reducer'
import { useDate } from '../../../common/hooks/use-date'
import { useTimelineCompleteForStats } from '../../hooks/use-timeline-complete-for-stats'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineEmpty from '../ui/mission-timeline-empty'
import MissionTimelineError from '../ui/mission-timeline-error'
import MissionTimelineLoader from '../ui/mission-timeline-loader'
import useGetMissionGeneralInformationQuery
  from '../../../mission-general-infos/services/use-mission-general-information.tsx'
import { useMissionType } from '../../../common/hooks/use-mission-type.tsx'

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
  const { computeCompleteForStats } = useTimelineCompleteForStats()
  const { data: generalInfos } = useGetMissionGeneralInformationQuery(missionId)
  const { isExternalReinforcementTime } = useMissionType()
  useEffect(() => {
    const completenessForStats = computeCompleteForStats(actions)
    setTimelineCompleteForStats(completenessForStats)
  }, [actions])

  if (isLoading) return <MissionTimelineLoader />
  if (isExternalReinforcementTime(generalInfos.missionReportType)) return <MissionTimelineEmpty isReinforcementTime />
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
