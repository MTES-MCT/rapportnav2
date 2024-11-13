import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import MissionListItem from '../mission-list-item.tsx'
import { ControlUnit } from '@mtes-mct/monitor-ui'
import ControlUnitResourceType = ControlUnit.ControlUnitResourceType
import { Mission } from '@common/types/mission-types.ts'
import MissionListing from '../mission-listing.tsx'

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
  const missions: Mission[] = [
    { id: 1, missionSource: 'source1', startDateTimeUtc: new Date().toISOString(), observationsByUnit: 'Observation 1' },
    { id: 2, missionSource: 'source2', startDateTimeUtc: new Date().toISOString(), observationsByUnit: 'Observation 2' },
  ];

  const mockSetOpenIndex = vi.fn()

  test('should render "N/A" if no ControlUnitResource provided', () => {
    render(<MissionListItem mission={mission1} isUlam={true} index={0} openIndex={true} setOpenIndex={mockSetOpenIndex} />)
    const noResource = screen.queryByText("N/A")
    expect(noResource).toBeInTheDocument()
  })

  test('should render "Voiture" if ControlUnitResource contains CAR', () => {
    render(<MissionListItem mission={mission2} isUlam={true} index={0} openIndex={true} setOpenIndex={mockSetOpenIndex} />)
    const car = screen.queryByTestId("mission-list-item-control_unit_resources")
    expect(car).toHaveTextContent('Voiture')
  })

  test('should render all properties for ulam', () => {
    render(<MissionListItem isUlam={true} index={0} openIndex={true} setOpenIndex={mockSetOpenIndex} />)
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
    render(<MissionListItem isUlam={false} index={0} openIndex={true} setOpenIndex={mockSetOpenIndex} />)
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
    render(<MissionListItem isUlam={true} mission={mission1} index={0} openIndex={true} setOpenIndex={mockSetOpenIndex} /> )
    const missionCrew = screen.queryByTestId("mission-list-item-crew__text")
    expect(missionCrew).toHaveStyle('text-overflow: ellipsis')
    expect(missionCrew).toHaveStyle(`overflow: hidden`)
    expect(missionCrew).toHaveStyle(`white-space: nowrap`)
  })

  it('should open an item on click and close others', () => {
    render(<MissionListing missions={missions} isUlam={true} />);

    const missionItems = screen.getAllByTestId('mission-list-item-with-hover');

    fireEvent.click(missionItems[0]);
    expect(screen.getByText('Observation 1')).toBeVisible();

    expect(screen.queryByText('Observation 2')).toBeNull();

    fireEvent.click(missionItems[1]);
    expect(screen.getByText('Observation 2')).toBeVisible();
    expect(screen.queryByText('Observation 1')).toBeNull();
  });

  it('should close the item when clicking outside', () => {
    render(<MissionListing missions={missions} isUlam={true} />);

    const missionItems = screen.getAllByTestId('mission-list-item-with-hover');

    fireEvent.click(missionItems[0]);
    expect(screen.getByText('Observation 1')).toBeVisible();

    fireEvent.mouseDown(document);
    expect(screen.queryByText('Observation 1')).toBeNull();
  });

})

