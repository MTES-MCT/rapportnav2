import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { fireEvent, screen } from '@testing-library/react'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { render } from '../../../../../test-utils'
import ImpersonationDropdown from '../impersonation-dropdown'

const startImpersonationMock = vi.fn()
const stopImpersonationMock = vi.fn()

vi.mock('../../hooks/use-impersonation', () => ({
  default: vi.fn()
}))

vi.mock('../../../admin/services/use-admin-services-service', () => ({
  default: vi.fn()
}))

import useImpersonation from '../../hooks/use-impersonation'
import useAdminServiceListQuery from '../../../admin/services/use-admin-services-service'

describe('ImpersonationDropdown', () => {
  let queryClient: QueryClient

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false
        }
      }
    })

    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
  })

  const renderComponent = () => {
    return render(
      <QueryClientProvider client={queryClient}>
        <ImpersonationDropdown />
      </QueryClientProvider>
    )
  }

  it('renders nothing when user cannot impersonate', () => {
    vi.mocked(useImpersonation).mockReturnValue({
      isImpersonating: false,
      targetServiceId: null,
      targetServiceName: null,
      targetServiceType: null,
      canImpersonate: false,
      startImpersonation: startImpersonationMock,
      stopImpersonation: stopImpersonationMock
    })

    vi.mocked(useAdminServiceListQuery).mockReturnValue({
      data: [],
      isLoading: false
    } as any)

    const { container } = renderComponent()
    expect(container.firstChild).toBeNull()
  })

  it('renders select dropdown when user can impersonate and is not impersonating', () => {
    vi.mocked(useImpersonation).mockReturnValue({
      isImpersonating: false,
      targetServiceId: null,
      targetServiceName: null,
      targetServiceType: null,
      canImpersonate: true,
      startImpersonation: startImpersonationMock,
      stopImpersonation: stopImpersonationMock
    })

    vi.mocked(useAdminServiceListQuery).mockReturnValue({
      data: [
        { id: 1, name: 'PAM Service', serviceType: 'PAM', controlUnits: [] },
        { id: 2, name: 'ULAM Service', serviceType: 'ULAM', controlUnits: [] }
      ],
      isLoading: false
    } as any)

    renderComponent()

    expect(screen.getByText('Voir comme un service...')).toBeInTheDocument()
  })

  it('renders impersonation tag and quit button when impersonating', () => {
    vi.mocked(useImpersonation).mockReturnValue({
      isImpersonating: true,
      targetServiceId: 1,
      targetServiceName: 'PAM Service',
      targetServiceType: 'PAM',
      canImpersonate: true,
      startImpersonation: startImpersonationMock,
      stopImpersonation: stopImpersonationMock
    })

    vi.mocked(useAdminServiceListQuery).mockReturnValue({
      data: [],
      isLoading: false
    } as any)

    renderComponent()

    expect(screen.getByText('Mode service : PAM Service')).toBeInTheDocument()
    expect(screen.getByText('Quitter')).toBeInTheDocument()
  })

  it('calls stopImpersonation when quit button is clicked', () => {
    vi.mocked(useImpersonation).mockReturnValue({
      isImpersonating: true,
      targetServiceId: 1,
      targetServiceName: 'PAM Service',
      targetServiceType: 'PAM',
      canImpersonate: true,
      startImpersonation: startImpersonationMock,
      stopImpersonation: stopImpersonationMock
    })

    vi.mocked(useAdminServiceListQuery).mockReturnValue({
      data: [],
      isLoading: false
    } as any)

    renderComponent()

    fireEvent.click(screen.getByText('Quitter'))

    expect(stopImpersonationMock).toHaveBeenCalledTimes(1)
  })

  it('calls useAdminServiceListQuery with active=true', () => {
    vi.mocked(useImpersonation).mockReturnValue({
      isImpersonating: false,
      targetServiceId: null,
      targetServiceName: null,
      targetServiceType: null,
      canImpersonate: true,
      startImpersonation: startImpersonationMock,
      stopImpersonation: stopImpersonationMock
    })

    vi.mocked(useAdminServiceListQuery).mockReturnValue({
      data: [],
      isLoading: false
    } as any)

    renderComponent()

    expect(useAdminServiceListQuery).toHaveBeenCalledWith({ active: true })
  })

  it('disables select when services are loading', () => {
    vi.mocked(useImpersonation).mockReturnValue({
      isImpersonating: false,
      targetServiceId: null,
      targetServiceName: null,
      targetServiceType: null,
      canImpersonate: true,
      startImpersonation: startImpersonationMock,
      stopImpersonation: stopImpersonationMock
    })

    vi.mocked(useAdminServiceListQuery).mockReturnValue({
      data: undefined,
      isLoading: true
    } as any)

    renderComponent()

    const select = screen.getByText('Voir comme un service...')
    expect(select.closest('[class*="disabled"]') || select.closest('[disabled]')).toBeTruthy
  })
})
