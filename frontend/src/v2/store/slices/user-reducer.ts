import { store } from '..'
import { User } from '../../features/common/types/user'

export const setUser = (user?: User) => {
  store.setState(state => {
    return {
      ...state,
      user
    }
  })
}
