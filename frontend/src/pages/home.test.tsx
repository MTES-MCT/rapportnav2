import { render } from '../test-utils.tsx'
import Home from './home.tsx'

describe('Home', () => {
  test('should render', () => {
    const { container } = render(<Home />)
    expect(container.firstElementChild).not.toBeNull()
  })
})
