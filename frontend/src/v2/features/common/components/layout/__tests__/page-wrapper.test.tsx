import { render, screen } from '../../../../../../test-utils'
import PageWrapper from '../page-wrapper'

describe('MissionPageWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <PageWrapper
        footer={<>Footer</>}
        header={<>Header</>}
        action={<>Action</>}
        timeline={<>Timeline</>}
        generalInformations={<>Général informations</>}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })

  it('should show loading message', () => {
    render(<PageWrapper isLoading={true} footer={<>Footer</>} header={<>Header</>} />)
    expect(screen.getByText('Chargement...')).toBeInTheDocument()
  })

  it('should show error message', () => {
    render(<PageWrapper footer={<>Footer</>} header={<>Header</>} error={{ message: 'My beautiful error' }} />)
    expect(screen.getByText('My beautiful error')).toBeInTheDocument()
  })
})
