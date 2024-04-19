import { render } from '../../../../test-utils.tsx'
import ActionOther from './timeline-item-other.tsx'

describe('ActionOther', () => {
  test('should render', () => {
    const { container } = render(<ActionOther />)
    expect(container.firstElementChild).toBeNull()
  })
})
