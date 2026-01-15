import { Store } from '@tanstack/store'
import { CompletenessForStats, MissionStatusEnum } from '../features/common/types/mission-types'
import { ModuleType } from '../features/common/types/module-type'
import { User } from '../features/common/types/user'

export interface State {
  delayQuery: {
    debounceTime?: number
  }
  timeline: {
    currentIndex: number
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
  module: {
    homeUrl?: string
    type?: ModuleType
  }
}

export const store = new Store<State>({
  delayQuery: {},
  timeline: { currentIndex: 0 },
  generalInformations: {},
  mission: {},
  user: {} as User,
  connectivity: {},
  module: {
    type: ModuleType.PAM
  }
})
