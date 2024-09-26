import DateRangePicker from '@common/components/elements/dates/daterange-picker.tsx'
import { render, screen } from '../../../../../test-utils.tsx'

describe('DateRangePicker tests  ', () => {
  describe('DateRangePicker', () => {
    const onChangeMock = vi.fn()

    beforeEach(() => {
      vi.clearAllMocks()
    })
    it('should render the MonitorUIDateRangePicker with preprocessed date', () => {
      render(
        <DateRangePicker
          selectedRange={[new Date('2023-09-01T12:15:30Z'), new Date('2023-09-02T12:15:30Z')]}
          onChange={onChangeMock}
        />
      )
      const inputs = screen.getAllByPlaceholderText('yyyy-MM-dd') as HTMLInputElement
      expect(inputs[0].value).toBe('2023-09-01')
      expect(inputs[1].value).toBe('2023-09-02')
    })
  })
})
