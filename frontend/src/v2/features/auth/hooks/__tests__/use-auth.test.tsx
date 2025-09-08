import { describe, it, expect, vi, beforeEach } from 'vitest'
import { renderHook, act } from '@testing-library/react'
import useAuth, { setGlobalLogout, triggerGlobalLogout } from '../use-auth'
import { useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { jwtDecode } from 'jwt-decode'

// Mocks
vi.mock('@features/auth/utils/token')
vi.mock('@tanstack/react-query')
vi.mock('react-router-dom', () => ({ useNavigate: vi.fn() }))
vi.mock('jwt-decode')

describe('useAuth hook', () => {
  let mockClear: vi.Mock
  let mockInvalidateQueries: vi.Mock
  let mockNavigate: vi.Mock

  beforeEach(() => {
    mockClear = vi.fn()
    mockInvalidateQueries = vi.fn()
    ;(useQueryClient as vi.Mock).mockReturnValue({
      clear: mockClear,
      invalidateQueries: mockInvalidateQueries
    })

    // navigation
    mockNavigate = vi.fn()
    ;(useNavigate as vi.Mock).mockReturnValue(mockNavigate)

    // jwtDecode
    ;(jwtDecode as vi.Mock).mockImplementation(() => ({
      userId: 1,
      roles: ['USER']
    }))
  })

  it('should set isAuthenticated true if token exists', () => {
    const fakeAuth = {
      get: vi.fn(() => 'fake-token'),
      remove: vi.fn()
    }
    const { result } = renderHook(() => useAuth(fakeAuth as any))
    expect(result.current.isAuthenticated).toBe(true)
  })

  it('should set isAuthenticated false if no token', () => {
    const fakeAuth = {
      get: vi.fn(() => undefined),
      remove: vi.fn()
    }
    const { result } = renderHook(() => useAuth(fakeAuth as any))
    expect(result.current.isAuthenticated).toBe(false)
  })

  it('logout should remove token, clear cache, and navigate', async () => {
    const fakeAuth = {
      get: vi.fn(() => 'fake-token'),
      remove: vi.fn()
    }
    const { result } = renderHook(() => useAuth(fakeAuth as any))

    await act(async () => {
      await result.current.logout()
    })

    expect(mockClear).toHaveBeenCalled()
    expect(mockNavigate).toHaveBeenCalledWith('/login', { replace: true })
  })

  it('navigateAndResetCache should invalidate queries and navigate', async () => {
    const fakeAuth = {
      get: vi.fn(() => 'fake-token'),
      remove: vi.fn()
    }
    const { result } = renderHook(() => useAuth(fakeAuth as any))

    await act(async () => {
      await result.current.navigateAndResetCache('/dashboard', ['key1', 'key2'])
    })

    expect(mockInvalidateQueries).toHaveBeenCalledWith({ queryKey: ['key1'] })
    expect(mockInvalidateQueries).toHaveBeenCalledWith({ queryKey: ['key2'] })
    expect(mockNavigate).toHaveBeenCalledWith('/dashboard')
  })

  it('isLoggedIn should decode token', () => {
    const fakeAuth = {
      get: vi.fn(() => 'fake-token'),
      remove: vi.fn()
    }
    const { result } = renderHook(() => useAuth(fakeAuth as any))
    const token = result.current.isLoggedIn()
    expect(token).toEqual({ userId: 1, roles: ['USER'] })
  })

  it('isLoggedIn should return undefined if no token', () => {
    const fakeAuth = {
      get: vi.fn(() => undefined),
      remove: vi.fn()
    }
    const { result } = renderHook(() => useAuth(fakeAuth as any))
    const token = result.current.isLoggedIn()
    expect(token).toBeUndefined()
  })

  it('should register and trigger global logout', async () => {
    const fakeAuth = {
      get: vi.fn(() => 'fake-token'),
      remove: vi.fn()
    }
    const { result } = renderHook(() => useAuth(fakeAuth as any))

    let called = false
    setGlobalLogout(() => {
      called = true
    })

    await act(async () => {
      triggerGlobalLogout()
    })

    expect(called).toBe(true)
  })
})
