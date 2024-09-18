import DateRangePicker from '@common/components/elements/daterange-picker.tsx'
import { render, screen, fireEvent } from '../../../../test-utils.tsx'

describe('DateRangePicker tests  ', () => {
  describe('DateRangePicker', () => {
    const onChangeMock = vi.fn()

    beforeEach(() => {
      vi.clearAllMocks()
    })
    it('should render the MonitorUIDateRangePicker with preprocessed date', () => {
      const { getByTestId } = render(
        <DateRangePicker
          defaultValue={[new Date('2023-09-01T12:15:30Z'), new Date('2023-09-02T12:15:30Z')]}
          onChange={onChangeMock}
        />
      )
      const input = screen.getByPlaceholderText('yyyy-MM-dd ~ yyyy-MM-dd') as HTMLInputElement
      expect(input.value).toBe('2023-09-01 ~ 2023-09-02')
    })
  })
})
