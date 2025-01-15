import {} from '@common/types/env-mission-types'
import { CompletenessForStats, MissionStatusEnum } from '@common/types/mission-types'
import { useStore } from '@tanstack/react-store'
import { isAfter, isBefore, isSameDay, parseISO } from 'date-fns'
import { isNil } from 'lodash'
import { useEffect, useState } from 'react'
import { store } from '../../../store'
import { setMissionStatus } from '../../../store/slices/mission-reducer'
import { CompletenessForStatsStatusEnum, Mission } from '../types/mission-types'

interface MissionHook {
  status?: MissionStatusEnum
  exportRapportEnabled: boolean
  completeForStats?: CompletenessForStats
}

export function useMission(mission?: Mission): MissionHook {
  const [status, setStatus] = useState<MissionStatusEnum>()
  const [completeForStats, setCompleteForStats] = useState<CompletenessForStats>()
  const timelieCompleteForStats = useStore(store, state => state.timeline.completnessForStats)
  const exportRapportEnabled = completeForStats?.status === CompletenessForStatsStatusEnum.COMPLETE

  useEffect(() => {
    if (!mission) return
    const status = computeMissionStatus(mission.startDateTimeUtc, mission.endDateTimeUtc)
    setStatus(status)
    setMissionStatus(status)
  }, [mission])

  useEffect(() => {
    setCompleteForStats({
      status: timelieCompleteForStats?.status, //TODO: compute with generalIformation
      sources: timelieCompleteForStats?.sources ////TODO: compute with generalIformation
    })
  }, [timelieCompleteForStats])

  const computeMissionStatus = (startDate: string, endDate?: string) => {
    const today = new Date()
    if (isNil(endDate) || isNil(startDate)) return MissionStatusEnum.UNAVAILABLE
    try {
      if (isAfter(parseISO(endDate), today)) return MissionStatusEnum.IN_PROGRESS
      if (isBefore(parseISO(endDate), today) || isSameDay(parseISO(endDate), today)) return MissionStatusEnum.ENDED
      if (isAfter(parseISO(startDate), today) || isSameDay(parseISO(startDate), today))
        return MissionStatusEnum.UPCOMING
    } catch (e) {
      console.error(`calculateMissionStatus - error with startDate: ${startDate}, endDate: ${endDate}`, e)
    }
    return MissionStatusEnum.UNAVAILABLE
  }

  return { status, completeForStats, exportRapportEnabled }
}
