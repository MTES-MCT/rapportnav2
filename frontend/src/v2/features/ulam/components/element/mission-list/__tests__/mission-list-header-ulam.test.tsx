import { render, screen } from '../../../../../../../test-utils.tsx'
import MissionListHeaderUlam from '../mission-list-header-ulam.tsx'

describe('MissionListHeader component', () => {
  test('should render all header items for ulam', () => {
    render(<MissionListHeaderUlam />)
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
})
