import { act, renderHook } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { AdminActionType } from '../../types/basic-action'
import useBasicTableItem from '../use-table-basic-item'

describe('useBasicTableItem', () => {
  it('should initialize with default values', () => {
    const onSubmit = vi.fn(async () => undefined)
    const { result } = renderHook(() => useBasicTableItem({ onSubmit }))

    expect(result.current.currentData).toBeUndefined()
    expect(result.current.currentAction).toBeUndefined()
    expect(result.current.showDialogForm).toBe(false)
  })

  it('should open dialog and store row data when an action is triggered', () => {
    const onSubmit = vi.fn(async () => undefined)
    const { result } = renderHook(() => useBasicTableItem({ onSubmit }))

    const action = {
      key: AdminActionType.MANAGE_UPDATE,
      form: () => null
    } as any
    const rowData = { id: 'row-1', name: 'Test' }

    act(() => {
      result.current.handleAction(action, rowData)
    })

    expect(result.current.currentData).toEqual(rowData)
    expect(result.current.currentAction).toEqual(action)
    expect(result.current.showDialogForm).toBe(true)
  })

  it('should not call onSubmit when handleSubmit response is false', async () => {
    const onSubmit = vi.fn(async () => undefined)
    const { result } = renderHook(() => useBasicTableItem({ onSubmit }))

    act(() => {
      result.current.handleSubmit(false, { foo: 'bar' })
    })

    expect(onSubmit).not.toHaveBeenCalled()
    expect(result.current.currentData).toBeUndefined()
    expect(result.current.currentAction).toBeUndefined()
    expect(result.current.showDialogForm).toBe(false)
  })

  it('should call onSubmit with current action and value when response is true', async () => {
    const onSubmit = vi.fn(async () => undefined)
    const { result } = renderHook(() => useBasicTableItem({ onSubmit }))

    const action = {
      key: AdminActionType.MANAGE_DELETE,
      form: () => null
    } as any
    const rowData = { id: 'row-2' }

    act(() => {
      result.current.handleAction(action, rowData)
    })

    await act(async () => {
      await result.current.handleSubmit(true, { id: 'row-2' })
    })

    expect(onSubmit).toHaveBeenCalledTimes(1)
    expect(onSubmit).toHaveBeenCalledWith(AdminActionType.MANAGE_DELETE, { id: 'row-2' })
    expect(result.current.currentData).toBeUndefined()
    expect(result.current.currentAction).toBeUndefined()
    expect(result.current.showDialogForm).toBe(false)
  })
})
