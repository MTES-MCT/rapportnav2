import MissionService, { MissionServiceProps } from '../mission-service.tsx'
import { render, screen } from '../../../../../../../test-utils.tsx'
import { Formik } from 'formik'
import { vi } from 'vitest'
import { Service } from '@common/types/service-types.ts'

const component = (props: MissionServiceProps) => (
  <Formik
    initialValues={{
      serviceId: undefined
    }}
    onSubmit={vi.fn()}
  >
    <MissionService {...props} />
  </Formik>
)

describe('MissionService Component', () => {
  it('renders', () => {
    const mockServices: Service[] = [
      { id: '1', name: 'Service A' },
      { id: '2', name: 'Service B' },
      { id: '3', name: 'Service C' }
    ]
    render(component({ services: mockServices }))
    const select = screen.getByTestId('mission-service-select')
    expect(select).toBeInTheDocument()
  })
})
