import { act, renderHook } from '@testing-library/react'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { useDelay } from '../use-delay'
import { useDelayFormik } from '../use-delay-formik'

vi.mock('../use-delay', () => ({
  useDelay: vi.fn()
}))

describe('useDelayFormik', () => {
  let mockHandleExecuteOnDelay: ReturnType<typeof vi.fn>

  beforeEach(() => {
    mockHandleExecuteOnDelay = vi.fn((callback, delay) => {
      setTimeout(callback, delay)
    })

    vi.mocked(useDelay).mockReturnValue({
      handleExecuteOnDelay: mockHandleExecuteOnDelay
    })

    vi.clearAllMocks()
    vi.useFakeTimers()
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  it('should initialize with undefined value when initValue is not provided', () => {
    const { result } = renderHook(() => useDelayFormik(undefined, vi.fn()))
    expect(result.current.value).toBeUndefined()
  })

  it('should initialize with initValue', () => {
    const initValue = 'test-value'
    const { result } = renderHook(() => useDelayFormik(initValue, vi.fn()))
    expect(result.current.value).toBe(initValue)
  })

  it('should initialize with numeric initValue', () => {
    const initValue = 42
    const { result } = renderHook(() => useDelayFormik(initValue, vi.fn()))
    expect(result.current.value).toBe(initValue)
  })

  it('should update value on onChange', () => {
    const onSubmit = vi.fn()
    const { result } = renderHook(() => useDelayFormik(undefined, onSubmit))
    act(() => {
      result.current.onChange('new-value')
    })
    expect(result.current.value).toBe('new-value')
  })

  it('should call handleExecuteOnDelay on onChange', () => {
    const onSubmit = vi.fn()
    const { result } = renderHook(() => useDelayFormik(undefined, onSubmit))
    act(() => {
      result.current.onChange('test-value')
    })
    expect(mockHandleExecuteOnDelay).toHaveBeenCalled()
  })

  it('should use default delay of 3000ms', () => {
    const onSubmit = vi.fn()
    const { result } = renderHook(() => useDelayFormik(undefined, onSubmit))
    act(() => {
      result.current.onChange('value')
    })
    expect(mockHandleExecuteOnDelay).toHaveBeenCalledWith(expect.any(Function), 3000)
  })

  it('should use custom delay when provided', () => {
    const onSubmit = vi.fn()
    const customDelay = 5000
    const { result } = renderHook(() => useDelayFormik(undefined, onSubmit, customDelay))
    act(() => {
      result.current.onChange('value')
    })
    expect(mockHandleExecuteOnDelay).toHaveBeenCalledWith(expect.any(Function), customDelay)
  })

  it('should handle numeric value changes', () => {
    const onSubmit = vi.fn()
    const { result } = renderHook(() => useDelayFormik(undefined, onSubmit))

    act(() => {
      result.current.onChange(123)
    })

    expect(result.current.value).toBe(123)
    expect(mockHandleExecuteOnDelay).toHaveBeenCalled()
  })

  it('should handle undefined value changes', () => {
    const onSubmit = vi.fn()
    const { result } = renderHook(() => useDelayFormik('initial', onSubmit))

    act(() => {
      result.current.onChange(undefined)
    })

    expect(result.current.value).toBeUndefined()
  })

  it('should update value when initValue prop changes', () => {
    const onSubmit = vi.fn()
    const { result, rerender } = renderHook(({ initValue }) => useDelayFormik(initValue, onSubmit), {
      initialProps: { initValue: 'initial-value' }
    })

    expect(result.current.value).toBe('initial-value')

    rerender({ initValue: 'updated-value' })

    expect(result.current.value).toBe('updated-value')
  })

  it('should not update value when initValue is falsy', () => {
    const onSubmit = vi.fn()
    const { result } = renderHook(() => useDelayFormik(0, onSubmit))
    expect(result.current.value).toBeUndefined()
  })

  it('should handle multiple onChange calls', () => {
    const onSubmit = vi.fn()
    const { result } = renderHook(() => useDelayFormik(undefined, onSubmit))

    act(() => {
      result.current.onChange('value1')
      result.current.onChange('value2')
      result.current.onChange('value3')
    })

    expect(result.current.value).toBe('value3')
    expect(mockHandleExecuteOnDelay).toHaveBeenCalledTimes(3)
  })

  it('should return onChange function', () => {
    const onSubmit = vi.fn()
    const { result } = renderHook(() => useDelayFormik(undefined, onSubmit))

    expect(result.current.onChange).toBeDefined()
    expect(typeof result.current.onChange).toBe('function')
  })
})
