import { MissionStatusEnum } from '../../types/mission-types.ts'
import client from '../../apollo-client.ts'
import { GET_MISSION_TIMELINE } from './timeline/use-mission-timeline.tsx'

/**
 * A custom React hook to determine whether a mission is finished based on its status,
 * without the need to re-query the server. It reads the mission excerpt from the cache
 * and checks if the status indicates the mission has ended.
 *
 * @param {string} missionId - The ID of the mission to check its finished status.
 * @returns {boolean} - Returns `true` if the mission is finished, `false` if it's not finished,
 * if the mission data is not available in the cache.
 * @example
 * const isMissionFinished = useIsMissionFinished('yourMissionId');
 * if (isMissionFinished === true) {
 *   console.log('Mission is finished.');
 * } else if (isMissionFinished === false) {
 *   console.log('Mission is not finished yet.');
 * } else {
 *   console.log('Mission data not available in cache.');
 * }
 */

const useIsMissionFinished = (missionId?: string): boolean | undefined => {
  try {
    const { mission } = client.readQuery({
      query: GET_MISSION_TIMELINE,
      variables: { missionId }
    })
    return mission?.status === MissionStatusEnum.ENDED
  } catch (error) {
    return undefined
  }
}

export default useIsMissionFinished
