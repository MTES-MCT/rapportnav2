import { render, screen } from '../../../test-utils.tsx'
import SignupPage from '../signup-page.tsx'

describe('SignupPage', () => {
  test('should render', () => {
    render(<SignupPage />)
    expect(screen.getByText("S'inscrire")).toBeInTheDocument()
  })
})
