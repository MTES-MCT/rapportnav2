import React from 'react'
import { expect, afterEach, vi } from 'vitest'
import { cleanup } from '@testing-library/react'

// Make React globally available for packages that reference it without importing
globalThis.React = React

// Cleanup after each test
afterEach(() => {
  cleanup()
})

// Suppress specific warnings that don't affect functionality
const originalWarn = console.warn
const originalError = console.error

globalThis.console.warn = (...args: any[]) => {
  const message = args[0]?.toString() || ''
  // Suppress source map warnings
  if (message.includes('Failed to load source map') || message.includes('.map')) {
    return
  }
  originalWarn(...args)
}

globalThis.console.error = (...args: any[]) => {
  const message = args[0]?.toString() || ''
  // Suppress source map related errors
  if (message.includes('ENOENT') && message.includes('.map')) {
    return
  }
  originalError(...args)
}
