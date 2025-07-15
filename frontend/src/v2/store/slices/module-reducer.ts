import { store } from '..'
import { ModuleType } from '../../features/common/types/module-type'

export const setModuleType = (type: ModuleType) => {
  store.setState(state => {
    return {
      ...state,
      module: {
        ...state.module,
        type
      }
    }
  })
}

export const setHomeUrl = (homeUrl: string) => {
  store.setState(state => {
    return {
      ...state,
      module: {
        ...state.module,
        homeUrl
      }
    }
  })
}
