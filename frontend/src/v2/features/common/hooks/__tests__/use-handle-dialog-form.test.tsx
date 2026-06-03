import { renderHook, waitFor } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import useHandleDialogForm from '../use-handle-dialog-form'

describe('useHandleDialogForm', () => {
  it('should clear errors when validation succeeds', async () => {
    const formik = {
      values: { foo: 'bar' },
      setErrors: vi.fn()
    } as any
    const schema = {
      validate: vi.fn(() => Promise.resolve())
    } as any

    renderHook(() => useHandleDialogForm({ schema, formik }))

    await waitFor(() => {
      expect(schema.validate).toHaveBeenCalledWith(formik.values, { abortEarly: false })
      expect(formik.setErrors).toHaveBeenCalledWith({})
    })
  })

  it('should set formik errors when validation fails', async () => {
    const formik = {
      values: { foo: 'bar' },
      setErrors: vi.fn()
    } as any
    const schema = {
      validate: vi.fn(() =>
        Promise.reject({
          inner: [
            { path: 'foo', message: 'Foo is required' },
            { path: 'bar', message: 'Bar is invalid' }
          ]
        })
      )
    } as any

    renderHook(() => useHandleDialogForm({ schema, formik }))

    await waitFor(() => {
      expect(formik.setErrors).toHaveBeenCalledWith({
        foo: 'Foo is required',
        bar: 'Bar is invalid'
      })
    })
  })
})
