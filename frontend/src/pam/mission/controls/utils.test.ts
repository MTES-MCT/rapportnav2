import { ControlAdministrative, ControlSecurity } from '../../mission-types' // Import your types as needed
import { controlIsEnabled } from './utils'

describe('controlIsEnabled', () => {
  it('should return false when controlData is undefined', () => {
    // Arrange: Set controlData to null
    const controlData = undefined

    // Act: Call the function
    const result = controlIsEnabled(controlData)

    // Assert: Expect the result to be false
    expect(result).toBe(false)
  })
  it('should return false when controlData is null', () => {
    // Arrange: Set controlData to null
    const controlData: any | null = null

    // Act: Call the function
    const result = controlIsEnabled(controlData)

    // Assert: Expect the result to be false
    expect(result).toBe(false)
  })
  it('should return false when controlData is not null but is empty object', () => {
    // Arrange: Create an empty controlData object
    const controlData = {} as any

    // Act: Call the function
    const result = controlIsEnabled(controlData)

    // Assert: Expect the result to be true
    expect(result).toBe(false)
  })
  it('should return true when controlData is not null and has properties', () => {
    // Arrange: Create controlData with properties
    const controlData: ControlAdministrative = {
      id: '123'
    }

    // Act: Call the function
    const result = controlIsEnabled(controlData)

    // Assert: Expect the result to be true
    expect(result).toBe(true)
  })
  it('should return true when controlData is not null and has properties with deletedAt explicitely undefined', () => {
    // Arrange: Create controlData with properties
    const controlData: ControlAdministrative = {
      id: '123',
      deletedAt: undefined
    }

    // Act: Call the function
    const result = controlIsEnabled(controlData)

    // Assert: Expect the result to be true
    expect(result).toBe(true)
  })

  it('should return false when controlData is not null and has deletedAt property', () => {
    // Arrange: Create controlData with deletedAt property
    const controlData: ControlSecurity = {
      id: '123',
      deletedAt: new Date().toISOString()
    }

    // Act: Call the function
    const result = controlIsEnabled(controlData)

    // Assert: Expect the result to be false
    expect(result).toBe(false)
  })
})
