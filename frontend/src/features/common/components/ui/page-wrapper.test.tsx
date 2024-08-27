import { render, screen } from '../../../../test-utils.tsx'
import PageWrapper from './page-wrapper.tsx'
import * as useAuthModule from '@features/auth/hooks/use-auth'

import { vi } from 'vitest'

describe('PageWrapper component', () => {
  it('should not render the sidebar when not authenticated', () => {
    vi.spyOn(useAuthModule, 'default').mockReturnValue({
      isAuthenticated: false,
      logout: vi.fn()
    })
    render(
      <PageWrapper>
        <div data-testid="child">Child Content</div>
      </PageWrapper>
    )

    const sidebarElement = screen.queryByTitle('missions')
    expect(sidebarElement).toBeNull()
  })
  it('should not render the sidebar when the showMenu prop is false', () => {
    vi.spyOn(useAuthModule, 'default').mockReturnValue({
      isAuthenticated: true,
      logout: vi.fn()
    })
    render(
      <PageWrapper showMenu={false}>
        <div data-testid="child">Child Content</div>
      </PageWrapper>
    )

    const sidebarElement = screen.queryByTitle('missions')
    expect(sidebarElement).toBeNull()
  })
  it('should render the sidebar when the showMenu prop is true and authenticated', () => {
    vi.spyOn(useAuthModule, 'default').mockReturnValue({
      isAuthenticated: true,
      logout: vi.fn()
    })
    render(
      <PageWrapper>
        <div data-testid="child">Child Content</div>
      </PageWrapper>
    )

    const sidebarElement = screen.queryByTitle('missions')
    expect(sidebarElement).not.toBeNull()
  })
  it('should render a custom header when provided', () => {
    vi.spyOn(useAuthModule, 'default').mockReturnValue({
      isAuthenticated: true,
      logout: vi.fn()
    })
    render(
      <PageWrapper header={<p>custom-header</p>}>
        <div data-testid="child">Child Content</div>
      </PageWrapper>
    )

    const headerElement = screen.getByText('custom-header')

    expect(headerElement).toBeInTheDocument()
  })
  it('should render the default header when no extra header provided', () => {
    vi.spyOn(useAuthModule, 'default').mockReturnValue({
      isAuthenticated: true,
      logout: vi.fn()
    })
    render(
      <PageWrapper>
        <div data-testid="child">Child Content</div>
      </PageWrapper>
    )

    const headerElement = screen.getByText('Rapport Nav')

    expect(headerElement).toBeInTheDocument()
  })
  it('should render the sidebar when authenticated', () => {
    vi.spyOn(useAuthModule, 'default').mockReturnValue({
      isAuthenticated: true,
      logout: vi.fn()
    })
    render(
      <PageWrapper>
        <div data-testid="child">Child Content</div>
      </PageWrapper>
    )

    const sidebarElement = screen.queryByTitle('missions')

    expect(sidebarElement).toBeInTheDocument()
  })
})
