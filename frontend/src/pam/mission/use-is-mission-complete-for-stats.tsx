import { CompletenessForStatsStatusEnum } from '../../types/mission-types.ts'
import client from '../../apollo-client.ts'
import { GET_MISSION_TIMELINE } from './timeline/use-mission-timeline.tsx'
import * as Sentry from '@sentry/react'

/**
 * A custom React hook to determine whether a mission is complete for stats based on its status,
 * without the need to re-query the server. It reads the mission excerpt from the cache
 * and checks if the status indicates the mission has ended.
 *
 * @param {string} missionId - The ID of the mission to check its finished status.
 * @returns {boolean} - Returns `true` if the mission is finished, `false` if it's not finished,
 * if the mission data is not available in the cache.
 * @example
 * const isMissionReportComplete = useIsMissionReportComplete('yourMissionId');
 * if (isMissionReportComplete === true) {
 *   console.log('Mission is ready for stats.');
 * } else if (isMissionReportComplete === false) {
 *   console.log('Mission is not ready yet.');
 * } else {
 *   console.log('Mission data not available in cache.');
 * }
 */

const useIsMissionCompleteForStats = (missionId?: number): boolean | undefined => {
  try {
    const { mission } = client.readQuery({
      query: GET_MISSION_TIMELINE,
      variables: { missionId }
    })

    return mission?.completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE
  } catch (error) {
    console.error("[useIsMissionCompleteForStats] An error occured : " + error)
    Sentry.captureException("[useIsMissionCompleteForStats] An error occured : " + error)
    return undefined
  }
}

export default useIsMissionCompleteForStats
