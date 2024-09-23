import { isNil } from 'lodash'

interface ActionHook<T> {
  getError: (data: T, isMissionFinished?: boolean, ...keys: (keyof T)[]) => string | undefined
}

export function useAction<T>(): ActionHook<T> {
  const getError = (data: T, isMissionFinished?: boolean, ...keys: (keyof T)[]) => {
    let isNullOrUndefined = false
    keys.forEach(key => {
      isNullOrUndefined = isNullOrUndefined || isNil(data[key])
    })
    return isNullOrUndefined && isMissionFinished ? 'error' : undefined
  }

  return { getError }
}
