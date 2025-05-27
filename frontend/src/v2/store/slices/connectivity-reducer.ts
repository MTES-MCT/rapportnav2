import { store } from '..'

export const setOfflineSince = (offlineSince?: string) => {
  store.setState(state => {
    return {
      ...state,
      connectivity: {
        ...state.connectivity,
        offlineSince
      }
    }
  })
}
