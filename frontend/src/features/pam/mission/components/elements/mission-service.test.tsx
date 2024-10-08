import { Service } from '@common/types/crew-types.ts'
import { render, screen } from '../../../../../test-utils.tsx'
import MissionService from './mission-service.tsx'

const services: Service[] = [
  { id: '1', name: 'Themis_A' },
  { id: '2', name: 'Themis_B' }
]

describe('MissionService', () => {
  it('should render service', async () => {
    render(<MissionService missionId={1} serviceId={1} services={services} />)
    expect(screen.getByText('Bordée')).toBeInTheDocument()
    expect(screen.getByText('A')).toBeInTheDocument()
  })

  it('should render A for index 0', async () => {
    render(<MissionService missionId={1} serviceId={Number(services[0].id)} services={services} />)
    expect(screen.getByText('A')).toBeInTheDocument()
  })

  it('should render B for index 1', async () => {
    render(<MissionService missionId={1} serviceId={Number(services[1].id)} services={services} />)
    expect(screen.getByText('B')).toBeInTheDocument()
  })
})
