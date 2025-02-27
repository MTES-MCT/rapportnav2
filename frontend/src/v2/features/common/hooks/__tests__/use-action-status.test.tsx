import { ActionStatusType } from '@common/types/action-types'
import { renderHook } from '@testing-library/react'
import { useActionStatus } from '../use-action-status'

describe('useActionStatus', () => {
  it('should return action status fir unknow', () => {
    const { result } = renderHook(() => useActionStatus(ActionStatusType.UNKNOWN))
    expect(result.current.text).toEqual('Inconnu')
    expect(result.current.color).toEqual('transparent')
  })

  it('should return action status fir unknow', () => {
    const { result } = renderHook(() => useActionStatus(ActionStatusType.ANCHORED))
    expect(result.current.text).toEqual('Mouillage')
    expect(result.current.color).toEqual('#567A9E')
  })

  it('should return action status fir unknow', () => {
    const { result } = renderHook(() => useActionStatus(ActionStatusType.DOCKED))
    expect(result.current.text).toEqual('Présence à quai')
    expect(result.current.color).toEqual('#FAC11A')
  })

  it('should return action status fir unknow', () => {
    const { result } = renderHook(() => useActionStatus(ActionStatusType.NAVIGATING))
    expect(result.current.text).toEqual('Navigation')
    expect(result.current.color).toEqual('#5697D2')
  })

  it('should return action status fir unknow', () => {
    const { result } = renderHook(() => useActionStatus(ActionStatusType.UNAVAILABLE))
    expect(result.current.text).toEqual('Indisponibilité')
    expect(result.current.color).toEqual('#E1000F')
  })
})
