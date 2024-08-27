import { render, screen } from '../../../../../../../test-utils.tsx'
import ActionNote from './timeline-item-note.tsx'
import { Action } from '@common/types/action-types.ts'

const props = (observations?: string) => ({
  action: {
    data: {
      observations
    }
  } as unknown as Action,
  onClick: vi.fn()
})

describe('ActionNote', () => {
  test('should render', () => {
    const { container } = render(<ActionNote {...props()} />)
    expect(container.firstElementChild).not.toBeNull()
  })
  test('should render placeholder', () => {
    render(<ActionNote {...props(undefined)} />)
    expect(screen.getByText('Note libre')).toBeInTheDocument()
  })
  test('should render observations', () => {
    render(<ActionNote {...props('dummy')} />)
    expect(screen.getByText('Dummy')).toBeInTheDocument()
  })
})
