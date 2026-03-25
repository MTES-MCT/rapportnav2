import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import MissionActionNavControlWarning from '../mission-action-nav-control-warning.tsx'

describe('MissionActionNavControlWarning', () => {
  it('renders', () => {
    render(<MissionActionNavControlWarning />)
    expect(
      screen.getByText('Veuillez contacter respectivement le CNSP et le CACEM', { exact: false })
    ).toBeInTheDocument()
  })
})
