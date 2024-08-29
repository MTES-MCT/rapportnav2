import { MockedProvider } from '@apollo/client/testing'
import '@testing-library/jest-dom/extend-expect'
import { render, renderHook, RenderOptions } from '@testing-library/react'
import React, { ReactElement } from 'react'
import { BrowserRouter } from 'react-router-dom'
import UIThemeWrapper from './features/common/components/ui/ui-theme-wrapper'

const AllTheProviders = ({ children }: { children: React.ReactNode }) => {
  return (
    <UIThemeWrapper>
      <MockedProvider addTypename={false}>
        <BrowserRouter>{children}</BrowserRouter>
      </MockedProvider>
    </UIThemeWrapper>
  )
}

const customRender = (ui: ReactElement, options?: Omit<RenderOptions, 'wrapper'>) =>
  render(ui, { wrapper: AllTheProviders, ...options })

const customRenderHook = (hook: any, options?: Omit<RenderOptions, 'wrapper'>) =>
  renderHook(() => hook(), { wrapper: AllTheProviders, ...options })

export * from '@testing-library/react'
export { customRender as render, customRenderHook as renderHook }

export const mockQueryResult = (data?: unknown, loading: boolean = false, error: any = undefined) => ({
  data,
  loading,
  error
})
