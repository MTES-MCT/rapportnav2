import { store } from '..'
import { MissionStatusEnum } from '../../features/common/types/mission-types.ts'

export const setMissionStatus = (status?: MissionStatusEnum) => {
  store.setState(state => {
    return {
      ...state,
      mission: {
        status,
        isMissionFinished: status === MissionStatusEnum.ENDED
      }
    }
  })
}
