import { ActionIllegalImmigration } from '@common/types/action-types'
import { act, renderHook } from '@testing-library/react'
import { useAction } from './use-action'

describe('useAction hook', () => {
  it('it return error when mission is finished', async () => {
    const { result } = renderHook(() => useAction<ActionIllegalImmigration>())
    act(() => {
      const error = result.current.getError({} as ActionIllegalImmigration, true, 'nbOfSuspectedSmugglers')
      expect(error).toEqual('error')
    })
  })

  it('it return undefined when mission is finished', async () => {
    const { result } = renderHook(() => useAction<ActionIllegalImmigration>())
    act(() => {
      const error = result.current.getError({} as ActionIllegalImmigration, false, 'nbOfSuspectedSmugglers')
      expect(error).toBeUndefined()
    })
  })
})
