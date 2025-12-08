import { render, screen } from '../../../../../../test-utils'
import { MissionControlTitle } from '../mission-control-title'

describe('MissionControlTitle', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<MissionControlTitle text={'Contrôle administratif'} isToComplete={true} />)
    expect(wrapper).toMatchSnapshot()
  })

  it('should render text properly', () => {
    render(<MissionControlTitle text={'Contrôle administratif'} isToComplete={true} />)
    expect(screen.getByText('Contrôle administratif')).toBeInTheDocument()
  })

  it('should render not render the icon when control is complete', () => {
    render(<MissionControlTitle text={'Contrôle administratif'} isToComplete={false} />)
    expect(screen.queryByTestId('control-incomplete-title')).toBeNull()
  })

  it('should render the icon when control is to complete', () => {
    render(<MissionControlTitle text={'Contrôle administratif'} isToComplete={true} />)
    expect(screen.getByTestId('control-incomplete-title')).toBeInTheDocument()
  })
})
