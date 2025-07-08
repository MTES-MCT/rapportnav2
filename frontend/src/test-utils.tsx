import { MockedProvider } from '@apollo/client/testing'
import '@testing-library/jest-dom'
import { render, renderHook, RenderOptions } from '@testing-library/react'
import React, { ReactElement } from 'react'
import { BrowserRouter } from 'react-router-dom'
import UIThemeWrapper from './features/common/components/ui/ui-theme-wrapper'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

const queryClient = new QueryClient()

const AllTheProviders = ({ children }: { children: React.ReactNode }) => {
  return (
    <UIThemeWrapper>
      <MockedProvider addTypename={false}>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>{children}</BrowserRouter>
        </QueryClientProvider>
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
