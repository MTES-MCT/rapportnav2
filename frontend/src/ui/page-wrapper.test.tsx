import React from 'react'
import { render, screen } from '../test-utils'
import PageWrapper from './page-wrapper'
import useAuth from '../auth/use-auth'

jest.mock('../auth/use-auth') // Mock the useAuth hook

describe('PageWrapper component', () => {
  it('should not render the sidebar when not authenticated', () => {
    ;(useAuth as jest.Mock).mockReturnValue({
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
  it('should render a custom header when provided', () => {
    ;(useAuth as jest.Mock).mockReturnValue({
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
    ;(useAuth as jest.Mock).mockReturnValue({
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
    ;(useAuth as jest.Mock).mockReturnValue({
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
