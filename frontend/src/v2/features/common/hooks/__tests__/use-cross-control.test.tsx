import { renderHook } from '@testing-library/react'
import {
  CrossControlConclusionType,
  CrossControlOriginType,
  CrossControlStatusType,
  CrossControlTargetType
} from '../../types/crossed-control-type'
import { useCrossControl } from '../use-crossed-control'

describe('useCrossControl', () => {
  it('should return origin type options', () => {
    const { result } = renderHook(() => useCrossControl())
    expect(result.current.crossControlOriginOptions.length).toEqual(5)
  })

  it('should return all origin type name', () => {
    const { result } = renderHook(() => useCrossControl())
    Object.keys(CrossControlOriginType).forEach(type => {
      expect(result.current.getCrossControlOriginType(type as CrossControlOriginType)).toBeDefined()
    })
  })

  it('should return status type options', () => {
    const { result } = renderHook(() => useCrossControl())
    expect(result.current.crossControlStatusOptions.length).toEqual(2)
  })

  it('should return all status type name', () => {
    const { result } = renderHook(() => useCrossControl())
    Object.keys(CrossControlStatusType).forEach(type => {
      expect(result.current.getCrossControlStatusType(type as CrossControlStatusType)).toBeDefined()
    })
  })

  it('should return conclusion type options', () => {
    const { result } = renderHook(() => useCrossControl())
    expect(result.current.crossControlConclusionOptions.length).toEqual(2)
  })

  it('should return all conclusion type name', () => {
    const { result } = renderHook(() => useCrossControl())
    Object.keys(CrossControlConclusionType).forEach(type => {
      expect(result.current.getCrossControlConclusionType(type as CrossControlConclusionType)).toBeDefined()
    })
  })

  it('should return target type options', () => {
    const { result } = renderHook(() => useCrossControl())
    expect(result.current.crossControlTargetOptions.length).toEqual(2)
  })

  it('should return all target type name', () => {
    const { result } = renderHook(() => useCrossControl())
    Object.keys(CrossControlTargetType).forEach(type => {
      expect(result.current.getCrossControlTargetType(type as CrossControlTargetType)).toBeDefined()
    })
  })
})
