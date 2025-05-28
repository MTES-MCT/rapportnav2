import { MissionStatusEnum } from '@common/types/mission-types'
import { Store } from '@tanstack/store'
import { CompletenessForStats } from '../features/common/types/mission-types'
import { User } from '../features/common/types/user'

export interface State {
  delayQuery: {
    debounceTime?: number
  }
  timeline: {
    isCompleteForStats?: boolean
    completnessForStats?: CompletenessForStats
  }
  generalInformations: {
    isCompleteForStats?: boolean
    completnessForStats?: CompletenessForStats
  }
  mission: {
    status?: MissionStatusEnum
    isMissionFinished?: boolean
  }
  user?: User
  connectivity: {
    offlineSince?: string
  }
}

export const store = new Store<State>({
  delayQuery: {},
  timeline: {},
  generalInformations: {},
  mission: {},
  user: {} as User,
  connectivity: {}
})
