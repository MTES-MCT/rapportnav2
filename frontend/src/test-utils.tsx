import React, { ReactElement } from 'react'
import { render, renderHook, RenderOptions } from '@testing-library/react'
import UIThemeWrapper from './ui/ui-theme-wrapper'
import { BrowserRouter } from 'react-router-dom'

const AllTheProviders = ({ children }: { children: React.ReactNode }) => {
  return (
    <UIThemeWrapper>
      <BrowserRouter>{children}</BrowserRouter>
    </UIThemeWrapper>
  )
}

const customRender = (ui: ReactElement, options?: Omit<RenderOptions, 'wrapper'>) =>
  render(ui, { wrapper: AllTheProviders, ...options })

const customRenderHook = (hook: any, options?: Omit<RenderOptions, 'wrapper'>) =>
  renderHook(() => hook(), { wrapper: AllTheProviders, ...options })

export * from '@testing-library/react'
export { customRender as render }
export { customRenderHook as renderHook }
