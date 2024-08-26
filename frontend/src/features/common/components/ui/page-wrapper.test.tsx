import { render, screen } from '../../../../test-utils.tsx'
import PageWrapper from './page-wrapper.tsx'
import useAuth from '../../../auth/hooks/use-auth.tsx'
import { vi } from 'vitest'

vi.mock('../auth/use-auth', () => ({
  default: vi.fn()
}))

describe('PageWrapper component', () => {
  it('should not render the sidebar when not authenticated', () => {
    ;(useAuth as any).mockReturnValue({
      isAuthenticated: false
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
    ;(useAuth as any).mockReturnValue({
      isAuthenticated: true
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
    ;(useAuth as any).mockReturnValue({
      isAuthenticated: true
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
    ;(useAuth as any).mockReturnValue({
      isAuthenticated: true
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
    ;(useAuth as any).mockReturnValue({
      isAuthenticated: true
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
    ;(useAuth as any).mockReturnValue({
      isAuthenticated: true
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
