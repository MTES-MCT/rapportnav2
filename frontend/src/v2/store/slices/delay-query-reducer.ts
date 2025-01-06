import { store } from '..'

export const setDebounceTime = (debounceTime: number) => {
  store.setState(state => {
    return {
      ...state,
      delayQuery: {
        debounceTime
      }
    }
  })
}

export const resetDebounceTime = () => {
  store.setState(state => {
    return {
      ...state,
      delayQuery: {
        debounceTime: undefined
      }
    }
  })
}
