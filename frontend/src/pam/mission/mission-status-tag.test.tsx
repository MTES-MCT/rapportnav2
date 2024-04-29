import { render, screen } from '../../test-utils.tsx'

import { MissionStatusEnum } from '../../types/mission-types.ts'
import MissionStatusTag from './mission-status-tag.tsx'

describe('MissionStatusTag component', () => {
  test('renders "indisponible" when status is undefined', () => {
    render(<MissionStatusTag status={undefined} />)
    const tagElement = screen.getByText('Indisponible')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "À venir" when status is UPCOMING', () => {
    render(<MissionStatusTag status={MissionStatusEnum.UPCOMING} />)
    const tagElement = screen.getByText('À venir')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "À venir" when status is PENDING', () => {
    render(<MissionStatusTag status={MissionStatusEnum.PENDING} />)
    const tagElement = screen.getByText('À venir')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "En cours" when status is IN_PROGRESS', () => {
    render(<MissionStatusTag status={MissionStatusEnum.IN_PROGRESS} />)
    const tagElement = screen.getByText('En cours')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "Terminée" when status is ENDED', () => {
    render(<MissionStatusTag status={MissionStatusEnum.ENDED} />)
    const tagElement = screen.getByText('Terminée')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "À venir" when status is UNAVAILABLE', () => {
    render(<MissionStatusTag status={MissionStatusEnum.UNAVAILABLE} />)
    const tagElement = screen.getByText('Indisponible')
    expect(tagElement).toBeInTheDocument()
  })
})
