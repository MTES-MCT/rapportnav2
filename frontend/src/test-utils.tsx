// test-utils.tsx
import '@testing-library/jest-dom'
import { render, renderHook, RenderOptions } from '@testing-library/react'
import React, { ReactElement } from 'react'
import { BrowserRouter } from 'react-router-dom'
import UIThemeWrapper from './features/common/components/ui/ui-theme-wrapper'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { FormikContext, FormikContextType } from 'formik'
import { vi } from 'vitest'

const createFakeFormikContext = (values: any = {}): FormikContextType<any> => ({
  values,
  initialValues: values,
  errors: {},
  touched: {},
  isSubmitting: false,
  isValidating: false,
  submitCount: 0,
  submitForm: vi.fn(),
  validateForm: vi.fn(),
  validateField: vi.fn(),
  resetForm: vi.fn(),
  setErrors: vi.fn(),
  setTouched: vi.fn(),
  setStatus: vi.fn(),
  setSubmitting: vi.fn(),
  setValues: vi.fn(),
  setFieldValue: vi.fn(),
  setFieldError: vi.fn(),
  setFieldTouched: vi.fn(),
  handleBlur: vi.fn(),
  handleChange: vi.fn(),
  handleSubmit: vi.fn(),
  handleReset: vi.fn(),
  getFieldProps: vi.fn(nameOrOptions => {
    const name = typeof nameOrOptions === 'string' ? nameOrOptions : nameOrOptions.name
    return {
      name,
      value: values[name] || '',
      onChange: vi.fn(),
      onBlur: vi.fn()
    }
  }),
  getFieldMeta: vi.fn(name => ({
    value: values[name],
    error: undefined,
    touched: false,
    initialValue: values[name],
    initialTouched: false,
    initialError: undefined
  })),
  getFieldHelpers: vi.fn(name => ({
    setValue: vi.fn(),
    setTouched: vi.fn(),
    setError: vi.fn()
  })),
  validateOnBlur: true,
  validateOnChange: true,
  validateOnMount: false,
  isValid: true,
  dirty: false,
  status: undefined,
  registerField: vi.fn(),
  unregisterField: vi.fn()
})

const queryClient = new QueryClient()

interface AllTheProvidersProps {
  children: React.ReactNode
  formikValues?: any
}

const AllTheProviders = ({ children, formikValues }: AllTheProvidersProps) => {
  const fakeFormik = createFakeFormikContext(formikValues || { targets: [] })

  return (
    <FormikContext.Provider value={fakeFormik}>
      <UIThemeWrapper>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>{children}</BrowserRouter>
        </QueryClientProvider>
      </UIThemeWrapper>
    </FormikContext.Provider>
  )
}

interface CustomRenderOptions extends Omit<RenderOptions, 'wrapper'> {
  formikValues?: any
}

const customRender = (ui: ReactElement, options?: CustomRenderOptions) => {
  const { formikValues, ...renderOptions } = options || {}
  return render(ui, {
    wrapper: ({ children }) => <AllTheProviders formikValues={formikValues}>{children}</AllTheProviders>,
    ...renderOptions
  })
}

const customRenderHook = (hook: any, options?: Omit<RenderOptions, 'wrapper'>) =>
  renderHook(() => hook(), { wrapper: AllTheProviders, ...options })

export * from '@testing-library/react'
export { customRender as render, customRenderHook as renderHook }
