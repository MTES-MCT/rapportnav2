import { MissionStatusEnum } from '@common/types/mission-types'
import { store } from '..'

export const setMissionStatus = (status: MissionStatusEnum) => {
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
