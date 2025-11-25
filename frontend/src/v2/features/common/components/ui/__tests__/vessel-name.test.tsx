import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import VesselName from '../vessel-name'

describe('VesselName', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<VesselName name="ultima navire" />)
    expect(wrapper).toMatchSnapshot()
  })
  it('should render vessel name with optional fields', () => {
    render(<VesselName name="Poseidon" flagState="FR" externalReferenceNumber="REF-42" />)

    expect(screen.getByTestId('vessel-name')).toHaveTextContent('Poseidon - FR - REF-42')
  })

  it('should not include missing fields', () => {
    render(<VesselName name="Poseidon" flagState="FR" />)

    expect(screen.getByTestId('vessel-name')).toHaveTextContent('Poseidon - FR')
  })

  it('should render fallback when all props are undefined', () => {
    render(<VesselName />)

    expect(screen.getByTestId('vessel-name')).toHaveTextContent('')
  })
})
