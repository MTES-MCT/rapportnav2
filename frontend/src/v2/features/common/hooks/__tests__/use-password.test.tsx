import { renderHook } from '@testing-library/react'
import { usePassword } from '../use-password'

describe('usePassword', () => {
  it('should return a generatePassword function', () => {
    const { result } = renderHook(() => usePassword())
    expect(result.current.generatePassword).toBeDefined()
    expect(typeof result.current.generatePassword).toBe('function')
  })

  it('should generate a password of the requested length', () => {
    const { result } = renderHook(() => usePassword())
    const password = result.current.generatePassword(16)
    expect(password).toHaveLength(16)
  })

  it('should generate different lengths correctly', () => {
    const { result } = renderHook(() => usePassword())
    expect(result.current.generatePassword(8)).toHaveLength(8)
    expect(result.current.generatePassword(20)).toHaveLength(20)
    expect(result.current.generatePassword(32)).toHaveLength(32)
  })

  it('should contain at least one number', () => {
    const { result } = renderHook(() => usePassword())
    const password = result.current.generatePassword(16)
    expect(password).toMatch(/[0-9]/)
  })

  it('should contain at least one symbol', () => {
    const { result } = renderHook(() => usePassword())
    const password = result.current.generatePassword(16)
    expect(password).toMatch(/[!@#$%^&*()\-_=+\[\]{}|;:,.<>?]/)
  })

  it('should contain at least one lowercase letter', () => {
    const { result } = renderHook(() => usePassword())
    const password = result.current.generatePassword(16)
    expect(password).toMatch(/[a-z]/)
  })

  it('should contain at least one uppercase letter', () => {
    const { result } = renderHook(() => usePassword())
    const password = result.current.generatePassword(16)
    expect(password).toMatch(/[A-Z]/)
  })

  it('should generate unique passwords on successive calls', () => {
    const { result } = renderHook(() => usePassword())
    const passwords = new Set(Array.from({ length: 10 }, () => result.current.generatePassword(16)))
    expect(passwords.size).toBeGreaterThan(1)
  })

  it('should only contain valid characters', () => {
    const { result } = renderHook(() => usePassword())
    const validChars = /^[0-9a-zA-Z!@#$%^&*()\-_=+\[\]{}|;:,.<>?]+$/
    const password = result.current.generatePassword(32)
    expect(password).toMatch(validChars)
  })

  it('should handle minimum length of 4 (one per character type)', () => {
    const { result } = renderHook(() => usePassword())
    const password = result.current.generatePassword(4)
    expect(password).toHaveLength(4)
    expect(password).toMatch(/[0-9]/)
    expect(password).toMatch(/[a-z]/)
    expect(password).toMatch(/[A-Z]/)
    expect(password).toMatch(/[!@#$%^&*()\-_=+\[\]{}|;:,.<>?]/)
  })
})