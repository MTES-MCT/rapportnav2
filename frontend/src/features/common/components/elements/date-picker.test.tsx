import DatePicker, {
  postprocessDateFromPicker,
  preprocessDateForPicker
} from '@common/components/elements/date-picker.tsx'
import { addMinutes } from 'date-fns'
import { render, screen, fireEvent } from '../../../../test-utils.tsx'

describe('DatePicker tests  ', () => {
  describe('preprocessDateForPicker', () => {
    it('should convert a local date to a UTC date for the picker', () => {
      const localDate = new Date('2023-09-01T12:15:30+02:00')
      const processedDate = preprocessDateForPicker(localDate)

      const expectedDate = new Date('2023-09-01T12:15:30Z')
      expect(processedDate.toISOString()).toBe(expectedDate.toISOString())
    })
  })

  describe('postprocessDateFromPicker', () => {
    it('should correct the date received from picker by adding local timezone offset', () => {
      const dateFromPicker = new Date('2023-09-01T10:15:30Z')
      const localOffset = new Date().getTimezoneOffset()
      const processedDate = postprocessDateFromPicker(dateFromPicker)

      const expectedDate = addMinutes(dateFromPicker, localOffset)
      expect(processedDate).toEqual(expectedDate)
    })
  })
  describe('DatePicker', () => {
    const onChangeMock = vi.fn()

    beforeEach(() => {
      vi.clearAllMocks()
    })
    it('should render the MonitorUIDatePicker with preprocessed date', () => {
      const { getByTestId } = render(<DatePicker defaultValue="2023-09-01T12:15:30Z" onChange={onChangeMock} />)
      const input = screen.getByPlaceholderText('yyyy-MM-dd') as HTMLInputElement
      expect(input.value).toBe('2023-09-01')
    })
  })
})
