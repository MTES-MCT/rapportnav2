import { render, screen } from '../../../../../test-utils.tsx'
import InfractionTypeTag from './infraction-type-tag.tsx'
import { InfractionTypeEnum } from '../../../../common/types/env-mission-types.ts'
import { InfractionType } from '../../../../common/types/fish-mission-types.ts'

describe('InfractionTypeTag', () => {
  test('renders Sans PV when type is undefined', async () => {
    render(<InfractionTypeTag type={undefined} />)
    expect(screen.getByText('Sans PV')).toBeInTheDocument()
  })
  test('renders Sans PV when type is incorrect', async () => {
    render(<InfractionTypeTag type={'invalid value' as any} />)
    expect(screen.getByText('Sans PV')).toBeInTheDocument()
  })
  test('renders Sans PV when type is WITHOUT_REPORT', async () => {
    render(<InfractionTypeTag type={InfractionTypeEnum.WITHOUT_REPORT} />)
    expect(screen.getByText('Sans PV')).toBeInTheDocument()
  })
  test('renders Sans PV when type is WITHOUT_RECORD', async () => {
    render(<InfractionTypeTag type={InfractionType.WITHOUT_RECORD} />)
    expect(screen.getByText('Sans PV')).toBeInTheDocument()
  })
  test('renders En attente when type is WAITING', async () => {
    render(<InfractionTypeTag type={InfractionTypeEnum.WAITING} />)
    expect(screen.getByText('En attente')).toBeInTheDocument()
  })
  test('renders En attente when type is PENDING', async () => {
    render(<InfractionTypeTag type={InfractionType.PENDING} />)
    expect(screen.getByText('En attente')).toBeInTheDocument()
  })
  test('renders Avec PV when type is WITH_REPORT', async () => {
    render(<InfractionTypeTag type={InfractionTypeEnum.WITH_REPORT} />)
    expect(screen.getByText('Avec PV')).toBeInTheDocument()
  })
  test('renders Avec PV when type is WITH_RECORD', async () => {
    render(<InfractionTypeTag type={InfractionType.WITH_RECORD} />)
    expect(screen.getByText('Avec PV')).toBeInTheDocument()
  })
})
