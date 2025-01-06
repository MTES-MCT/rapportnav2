import { Store } from '@tanstack/store'

export interface State {
  delayQuery: {
    debounceTime?: number
  }
}
export const store = new Store<State>({
  delayQuery: {
    debounceTime: undefined
  }
})
