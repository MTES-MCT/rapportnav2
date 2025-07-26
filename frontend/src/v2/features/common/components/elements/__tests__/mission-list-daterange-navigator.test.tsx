import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import ItemListDateRangeNavigator from '../item-list-daterange-navigator.tsx'

describe('useDateNavigator', () => {
  test('should initialize with the correct year date', () => {
    const initialDate = '2022-01-01T00:00:00Z'
    render(<ItemListDateRangeNavigator startDateTimeUtc={initialDate} timeframe={'year'} />)
    const result = screen.queryByTestId('date-display')
    expect(result).toHaveTextContent('2022')
  })
  test('should initialize with the correct month date', () => {
    const initialDate = '2022-01-01T00:00:00Z'
    render(<ItemListDateRangeNavigator startDateTimeUtc={initialDate} timeframe={'month'} />)
    const result = screen.queryByTestId('date-display')
    expect(result).toHaveTextContent('Janvier 2022')
  })

  test('should navigate to the previous month correctly', async () => {
    const initialDate = '2022-01-01T00:00:00Z'
    render(<ItemListDateRangeNavigator startDateTimeUtc={initialDate} timeframe={'month'} />)

    const previousButton = screen.getByTestId('previous-button')
    fireEvent.click(previousButton)
    const dateDisplay = await screen.findByTestId('date-display')
    expect(dateDisplay).toHaveTextContent('Décembre 2021')
  })

  test('should navigate to the next month correctly', async () => {
    const initialDate = '2022-01-01T00:00:00Z'
    render(<ItemListDateRangeNavigator startDateTimeUtc={initialDate} timeframe={'month'} />)
    const nextButton = screen.getByTestId('next-button')
    fireEvent.click(nextButton)
    const dateDisplay = await screen.findByTestId('date-display')

    expect(dateDisplay).toHaveTextContent('Février 2022')
  })
})
