import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import MissionListItem from '../mission-list-item.tsx'
import { ControlUnit } from '@mtes-mct/monitor-ui'
import ControlUnitResourceType = ControlUnit.ControlUnitResourceType

const mission1 = {
  id: 1,
  controlUnits: [],
  observationsByUnit: 'Je suis une observation'
}
const mission2 = {
  id: 2,
  controlUnits: [{
    controlUnitResources: [
      {
      name: ControlUnitResourceType.CAR
      }
    ]
  }]
}

const queryAllMissionItemProps = () => ({
  missionIcon: screen.queryByTestId("mission-list-item-icon"),
  missionNumber: screen.queryByTestId('mission-list-item-mission_number'),
  openBy: screen.queryByTestId('mission-list-item-open_by'),
  openDate: screen.queryByTestId('mission-list-item-start_date'),
  ressourcesUsed: screen.queryByTestId('mission-list-item-control_unit_resources'),
  crew: screen.queryByTestId('mission-list-item-crew'),
  missionStatus: screen.queryByTestId('mission-list-item-mission_status'),
  reportStatus: screen.queryByTestId('mission-list-item-completeness')
})

describe('MissionListItem component', () => {
  test('should render "N/A" when no ControlUnitResource provided', () => {
    render(<MissionListItem mission={mission1} isUlam={true} />)
    const noResource = screen.queryByText("N/A")
    expect(noResource).toBeInTheDocument()
  })

  test('should render "Voiture" when no ControlUnitResource contains CAR', () => {
    render(<MissionListItem mission={mission2} isUlam={true} />)
    const car = screen.queryByTestId("mission-list-item-control_unit_resources")
    expect(car).toHaveTextContent('Voiture')
  })

  test('should render all properties for ulam', () => {
    render(<MissionListItem isUlam={true} />)
    const {
      missionIcon, missionNumber, openBy, openDate,
      ressourcesUsed, crew, missionStatus, reportStatus
    } = queryAllMissionItemProps()

    expect(missionIcon).toBeInTheDocument()
    expect(missionNumber).toBeInTheDocument()
    expect(openBy).toBeInTheDocument()
    expect(openDate).toBeInTheDocument()
    expect(ressourcesUsed).toBeInTheDocument()
    expect(crew).toBeInTheDocument()
    expect(missionStatus).toBeInTheDocument()
    expect(reportStatus).toBeInTheDocument()
  })

  test('should not render ulam props when isUlam set to false', () => {
    render(<MissionListItem isUlam={false} />)
    const {
      missionIcon, missionNumber, openBy, openDate,
      ressourcesUsed, crew, missionStatus, reportStatus
    } = queryAllMissionItemProps()

    expect(missionIcon).toBeInTheDocument()
    expect(missionNumber).toBeInTheDocument()
    expect(openBy).toBeInTheDocument()
    expect(openDate).toBeInTheDocument()
    expect(ressourcesUsed).not.toBeInTheDocument()
    expect(crew).not.toBeInTheDocument()
    expect(missionStatus).toBeInTheDocument()
    expect(reportStatus).toBeInTheDocument()
  })

  test('should render crew list with an ellipsis', () => {
    render(<MissionListItem isUlam={true} mission={mission1}/> )
    const missionCrew = screen.queryByTestId("mission-list-item-crew__text")
    expect(missionCrew).toHaveStyle('text-overflow: ellipsis')
    expect(missionCrew).toHaveStyle(`overflow: hidden`)
    expect(missionCrew).toHaveStyle(`white-space: nowrap`)
  })

  test('should displays details on mouse over and hides on mouse leave', () => {
    render(<MissionListItem mission={mission1} isUlam={true} />);

    const listItem = screen.getByTestId('mission-list-item-with-hover');

    expect(screen.queryByTestId('mission-list-item-more')).toBeNull();

    fireEvent.mouseOver(listItem);

    expect(screen.getByTestId('mission-list-item-more')).toBeInTheDocument();

    fireEvent.mouseLeave(listItem);

    expect(screen.queryByTestId('mission-list-item-more')).toBeNull();
  });
})

