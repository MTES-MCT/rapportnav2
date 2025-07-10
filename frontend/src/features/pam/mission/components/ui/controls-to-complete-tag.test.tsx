import { render, screen } from '../../../../../test-utils.tsx'
import ControlsToCompleteTag from './controls-to-complete-tag.tsx'

describe('ControlsToCompleteTag', () => {
  test('renders null when amountOfControlsToComplete is not provided or zero', () => {
    render(<ControlsToCompleteTag />)
    expect(screen.queryByTestId('controls-to-complete-tag')).not.toBeInTheDocument()

    render(<ControlsToCompleteTag amountOfControlsToComplete={0} />)
    expect(screen.queryByTestId('controls-to-complete-tag')).not.toBeInTheDocument()
  })

  test('renders the tag with correct content (singular) when amountOfControlsToComplete is provided', () => {
    const amountOfControlsToComplete = 1
    render(<ControlsToCompleteTag amountOfControlsToComplete={amountOfControlsToComplete} />)

    const tagElement = screen.getByTestId('controls-to-complete-tag')
    expect(tagElement).toBeInTheDocument()
    expect(tagElement).toHaveTextContent(`${amountOfControlsToComplete} type de contrôles à compléter`)
  })
  test('renders the tag with correct content (plural) when amountOfControlsToComplete is provided', () => {
    const amountOfControlsToComplete = 3
    render(<ControlsToCompleteTag amountOfControlsToComplete={amountOfControlsToComplete} />)

    const tagElement = screen.getByTestId('controls-to-complete-tag')
    expect(tagElement).toBeInTheDocument()
    expect(tagElement).toHaveTextContent(`${amountOfControlsToComplete} types de contrôles à compléter`)
  })
})
