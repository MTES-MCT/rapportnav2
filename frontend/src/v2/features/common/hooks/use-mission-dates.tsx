import { useQueryClient } from '@tanstack/react-query'
import { inquiriesKeys, missionsKeys } from '../services/query-keys.ts'
import { Mission2 } from '../types/mission-types.ts'
import { Inquiry } from '../types/inquiry.ts'

export type MissionDates = {
  startDateTimeUtc?: string
  endDateTimeUtc?: string
}

export function useMissionDates(id?: string, type: 'mission' | 'inquiry' = 'mission'): MissionDates {
  const queryClient = useQueryClient()

  const getMissionDates = () => {
    const mission: Mission2 | undefined = id ? queryClient.getQueryData(missionsKeys.byId(id)) : undefined
    return {
      startDateTimeUtc: mission?.data?.startDateTimeUtc,
      endDateTimeUtc: mission?.data?.endDateTimeUtc
    }
  }

  const getInquiryDates = () => {
    const inquiry: Inquiry | undefined = id ? queryClient.getQueryData(inquiriesKeys.byId(id)) : undefined
    return {
      startDateTimeUtc: inquiry?.startDateTimeUtc,
      endDateTimeUtc: inquiry?.endDateTimeUtc
    }
  }

  return type === 'mission' ? getMissionDates() : getInquiryDates()
}
