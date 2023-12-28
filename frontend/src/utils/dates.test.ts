import { formatDateForFrenchHumans } from './dates.ts' // Replace 'your-file' with the correct path to your function

describe('formatDateForFrenchHumans', () => {
  it('should format the date correctly for a valid input', () => {
    const inputDate = '2022-02-15T04:50:09Z'
    const formattedDate = formatDateForFrenchHumans(inputDate)
    expect(formattedDate).toBe('15/02/2022')
  })

  it('should return "--/--/----" for an empty input', () => {
    const formattedDate = formatDateForFrenchHumans()
    expect(formattedDate).toBe('--/--/----')
  })

  it('should return "--/--/----" for an invalid input', () => {
    const invalidInputDate = 'invalid-date'
    const formattedDate = formatDateForFrenchHumans(invalidInputDate)
    expect(formattedDate).toBe('--/--/----')
  })
})
