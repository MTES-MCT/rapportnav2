// axiosInstance.test.ts
import { describe, it, expect, vi, beforeAll, afterEach, afterAll } from 'vitest'
import { http, HttpResponse } from 'msw'
import { setupServer } from 'msw/node'

// Spy
const logoutSpy = vi.fn()
const getMock = vi.fn()

// Mock before importing axiosInstance
vi.mock('../../v2/features/auth/hooks/use-auth.tsx', async () => {
  return { triggerGlobalLogout: logoutSpy }
})

vi.mock('@features/auth/utils/token', () => {
  return {
    default: vi.fn().mockImplementation(() => ({
      get: getMock
    }))
  }
})

let axiosInstance: typeof import('../axios').default

beforeAll(async () => {
  // ⬅️ import AFTER mocks are set
  axiosInstance = (await import('../axios')).default
})

const server = setupServer(
  http.get('/api/v2/ok', () => HttpResponse.json({ message: 'ok' })),
  http.get('/api/v2/forbidden', () => new HttpResponse(JSON.stringify({ error: 'Forbidden' }), { status: 403 })),
  http.get('/api/v2/not-found', () =>
    new HttpResponse(
      JSON.stringify({
        type: 'urn:rapportnav:error:usage:COULD_NOT_FIND_EXCEPTION',
        title: 'Resource Not Found',
        status: 400,
        detail: 'The requested mission could not be found',
        code: 'COULD_NOT_FIND_EXCEPTION'
      }),
      { status: 400, headers: { 'Content-Type': 'application/problem+json' } }
    )
  )
)

beforeAll(() => server.listen())
afterEach(() => {
  server.resetHandlers()
  logoutSpy.mockClear()
  getMock.mockReset()
})
afterAll(() => server.close())

describe('axiosInstance', () => {
  it('attaches Authorization header if token exists', async () => {
    getMock.mockReturnValue('fake-token')

    const response = await axiosInstance.get('/ok')
    expect(response.data).toEqual({ message: 'ok' })
  })

  it('calls global logout on 403', async () => {
    getMock.mockReturnValue('fake-token')

    await expect(axiosInstance.get('/forbidden')).rejects.toThrow()
    expect(logoutSpy).toHaveBeenCalledTimes(1)
  })

  it('extracts detail from RFC 7807 ProblemDetail response', async () => {
    getMock.mockReturnValue('fake-token')

    try {
      await axiosInstance.get('/not-found')
      expect.fail('Should have thrown')
    } catch (error: any) {
      expect(error.message).toBe('The requested mission could not be found')
      expect(error.response.data.code).toBe('COULD_NOT_FIND_EXCEPTION')
    }
  })
})
