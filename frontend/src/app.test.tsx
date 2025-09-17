import { render, waitFor } from './test-utils.tsx'
import App from './app.tsx'

describe('App', () => {
  test('should render', async () => {
    const { container } = render(<App />)
    await waitFor(() => {
      expect(container.firstElementChild).not.toBeNull()
    })
  })
})
