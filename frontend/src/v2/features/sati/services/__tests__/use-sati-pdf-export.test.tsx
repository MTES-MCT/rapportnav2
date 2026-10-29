import { onlineManager, QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { act, renderHook, waitFor } from '@testing-library/react'
import React from 'react'
import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import axios from '../../../../../query-client/axios'
import useSatiPdfExport, { fetchSatiPdf } from '../use-sati-pdf-export.tsx'

vi.mock('../../../../../query-client/axios', () => ({
  default: {
    get: vi.fn()
  }
}))

describe('useSatiPdfExport', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>
  const pdf = new Blob(['%PDF'], { type: 'application/pdf' })
  const createObjectURL = vi.fn().mockReturnValue('blob:fake-url')
  const revokeObjectURL = vi.fn()
  let downloaded: { href: string; download: string }[]

  beforeEach(() => {
    Object.assign(window.URL, { createObjectURL, revokeObjectURL })
    downloaded = []
    vi.spyOn(HTMLAnchorElement.prototype, 'click').mockImplementation(function (this: HTMLAnchorElement) {
      downloaded.push({ href: this.href, download: this.download })
    })
    queryClient = new QueryClient({
      defaultOptions: { queries: { retry: false }, mutations: { retry: false } }
    })
    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
    vi.restoreAllMocks()
    onlineManager.setOnline(true)
  })

  it('fetches the pdf as a blob, accepting problem details on error', async () => {
    vi.mocked(axios.get).mockResolvedValue({ data: pdf })

    const result = await fetchSatiPdf({ ownerId: '761', actionId: '12345' })

    expect(result).toBe(pdf)
    expect(axios.get).toHaveBeenCalledWith('owners/761/actions/12345/sati/pdf', {
      responseType: 'blob',
      headers: { Accept: 'application/pdf, application/problem+json' }
    })
  })

  it('downloads the fetched pdf as sati-<actionId>.pdf and releases the object url', async () => {
    vi.mocked(axios.get).mockResolvedValue({ data: pdf })

    const { result } = renderHook(() => useSatiPdfExport(), { wrapper })
    await act(async () => {
      await result.current.mutateAsync({ ownerId: '761', actionId: '12345' })
    })

    expect(createObjectURL).toHaveBeenCalledWith(pdf)
    expect(downloaded).toEqual([{ href: 'blob:fake-url', download: 'sati-12345.pdf' }])
    expect(document.querySelector('a[download]')).toBeNull()
    // revoked after a delay, so that the browser does not cancel the download
    expect(revokeObjectURL).not.toHaveBeenCalled()
    await waitFor(() => expect(revokeObjectURL).toHaveBeenCalledWith('blob:fake-url'), { timeout: 2000 })
  })

  it('exposes the ProblemDetail message of an error returned as a blob', async () => {
    const problemDetail = {
      type: 'urn:rapportnav:error:usage:COULD_NOT_FIND_EXCEPTION',
      status: 400,
      detail: 'The requested resource could not be found'
    }
    const error = Object.assign(new Error('Request failed with status code 400'), {
      response: {
        status: 400,
        data: new Blob([JSON.stringify(problemDetail)], { type: 'application/problem+json' })
      }
    })
    vi.mocked(axios.get).mockRejectedValue(error)

    const { result } = renderHook(() => useSatiPdfExport(), { wrapper })
    let caught: (Error & { problemDetail?: unknown }) | undefined
    await act(async () => {
      await result.current.mutateAsync({ ownerId: '761', actionId: '12345' }).catch((e: Error) => (caught = e))
    })

    expect(caught?.message).toBe('The requested resource could not be found')
    expect(caught?.problemDetail).toEqual(problemDetail)
    expect(downloaded).toEqual([])
  })

  it('runs the download right away while offline instead of queuing it for later', async () => {
    onlineManager.setOnline(false)
    vi.mocked(axios.get).mockResolvedValue({ data: pdf })

    const { result } = renderHook(() => useSatiPdfExport(), { wrapper })
    act(() => result.current.mutate({ ownerId: '761', actionId: '12345' }))

    // a paused mutation (networkMode 'online') would never call the api here
    await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(1))
    expect(result.current.isPaused).toBe(false)
  })
})
