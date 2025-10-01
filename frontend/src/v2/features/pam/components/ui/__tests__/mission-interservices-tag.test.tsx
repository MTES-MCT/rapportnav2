import { render, screen } from '../../../../../../test-utils.tsx'
import { describe, it, expect } from 'vitest'
import MissionInterServicesTag from '../mission-interservices-tag'

describe('MissionInterServicesTag', () => {
  it('renders the tag text', () => {
    render(<MissionInterServicesTag />)
    expect(screen.getByText('Inter-services')).toBeInTheDocument()
  })

  it('matches snapshot', () => {
    const { container } = render(<MissionInterServicesTag />)
    expect(container).toMatchSnapshot()
  })
})
