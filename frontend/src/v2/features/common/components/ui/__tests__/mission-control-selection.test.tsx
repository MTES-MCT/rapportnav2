import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { ModuleType } from '../../../types/module-type'
import MissionControlSelection from '../mission-control-selection'

const handleSelect = vi.fn()
describe('IconVesselCommerce', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<MissionControlSelection moduleType={ModuleType.PAM} onSelect={handleSelect} />)
    expect(wrapper).toMatchSnapshot()
  })

  it('should render 5 controls selection for PAM', () => {
    render(<MissionControlSelection moduleType={ModuleType.PAM} onSelect={handleSelect} />)
    const elements = screen.getAllByText('Contrôles de')
    expect(elements.length).toEqual(5)
  })

  it('should render 7 controls selection for ULMA', () => {
    render(<MissionControlSelection moduleType={ModuleType.ULAM} onSelect={handleSelect} />)
    const elements = screen.getAllByText('Contrôles de')
    expect(elements.length).toEqual(7)
  })
})
