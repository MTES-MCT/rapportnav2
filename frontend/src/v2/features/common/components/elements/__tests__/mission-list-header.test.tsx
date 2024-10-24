import { render, screen } from '../../../../../../test-utils.tsx'
import MissionListHeader from '../mission-list-header.tsx'

describe('MissionListHeader component', () => {
  test('should render all header items for ulam', () => {
    render(<MissionListHeader isUlam={true}/>)
    const openDate = screen.getByText("Date d'ouverture")
    const ressourcesUsed = screen.getByText('Moyen(s) utilisé(s)')
    const agents = screen.getByText('Agent(s)')
    const missionStatus = screen.getByText('Statut mission')
    const reportStatus = screen.getByText('Statut du rapport')

    expect(openDate).toBeInTheDocument()
    expect(ressourcesUsed).toBeInTheDocument()
    expect(agents).toBeInTheDocument()
    expect(missionStatus).toBeInTheDocument()
    expect(reportStatus).toBeInTheDocument()
  })

  test('should not render ulam items when isUlam set to false', () => {
    render(<MissionListHeader isUlam={false}/>)

    const openDate = screen.queryByText("Date d'ouverture")
    const ressourcesUsed = screen.queryByText('Moyen(s) utilisé(s)')
    const agents = screen.queryByText('Agent(s)')
    const missionStatus = screen.queryByText('Statut mission')
    const reportStatus = screen.queryByText('Statut du rapport')

    expect(openDate).toBeInTheDocument()
    expect(ressourcesUsed).not.toBeInTheDocument()
    expect(agents).not.toBeInTheDocument()
    expect(missionStatus).toBeInTheDocument()
    expect(reportStatus).toBeInTheDocument()
  })
})
