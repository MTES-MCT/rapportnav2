import { render, screen, fireEvent, waitFor } from '../../../../../../../test-utils.tsx'
import { vi } from 'vitest'
import MissionListItemUlam from '../mission-list-item-ulam.tsx'
import { ControlUnit } from '@mtes-mct/monitor-ui'
import ControlUnitResourceType = ControlUnit.ControlUnitResourceType
import { Mission } from '@common/types/mission-types.ts'

const mission1 = {
  id: 1,
  controlUnits: [],
  observationsByUnit: 'Je suis une observation'
}
const mission2 = {
  id: 2,
  controlUnits: [
    {
      controlUnitResources: [
        {
          name: ControlUnitResourceType.CAR
        }
      ]
    }
  ]
}

const queryAllMissionItemProps = () => ({
  missionIcon: screen.queryByTestId('mission-list-item-icon'),
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
    {
      id: 1,
      missionSource: 'source1',
      startDateTimeUtc: new Date().toISOString(),
      observationsByUnit: 'Observation 1'
    },
    {
      id: 2,
      missionSource: 'source2',
      startDateTimeUtc: new Date().toISOString(),
      observationsByUnit: 'Observation 2'
    }
  ]

  const mockSetOpenIndex = vi.fn()

  test('should render "N/A" if no ControlUnitResource provided', () => {
    render(<MissionListItemUlam mission={mission1} index={0} openIndex={true} setOpenIndex={mockSetOpenIndex} />)
    const noResource = screen.queryByText('N/A')
    expect(noResource).toBeInTheDocument()
  })

  test('should render "Voiture" if ControlUnitResource contains CAR', () => {
    render(<MissionListItemUlam mission={mission2} index={0} openIndex={true} setOpenIndex={mockSetOpenIndex} />)
    const car = screen.queryByTestId('mission-list-item-control_unit_resources')
    expect(car).toHaveTextContent('Voiture')
  })

  test('should render all properties for ulam', () => {
    render(<MissionListItemUlam isUlam={true} index={0} openIndex={true} setOpenIndex={mockSetOpenIndex} />)
    const { missionIcon, missionNumber, openBy, openDate, ressourcesUsed, crew, missionStatus, reportStatus } =
      queryAllMissionItemProps()

    expect(missionIcon).toBeInTheDocument()
    expect(missionNumber).toBeInTheDocument()
    expect(openBy).toBeInTheDocument()
    expect(openDate).toBeInTheDocument()
    expect(ressourcesUsed).toBeInTheDocument()
    expect(crew).toBeInTheDocument()
    expect(missionStatus).toBeInTheDocument()
    expect(reportStatus).toBeInTheDocument()
  })

  test('should render crew list with an ellipsis', () => {
    render(
      <MissionListItemUlam
        isUlam={true}
        mission={mission1}
        index={0}
        openIndex={true}
        setOpenIndex={mockSetOpenIndex}
      />
    )
    const missionCrew = screen.queryByTestId('mission-list-item-crew__text')
    expect(missionCrew).toHaveStyle('text-overflow: ellipsis')
    expect(missionCrew).toHaveStyle(`overflow: hidden`)
    expect(missionCrew).toHaveStyle(`white-space: nowrap`)
  })

  it('toggles open state when clicking on the list item', async () => {
    const setOpenIndexMock = vi.fn()

    render(<MissionListItemUlam mission={missions} index={0} setOpenIndex={setOpenIndexMock} />)

    fireEvent.click(screen.getByTestId('mission-list-item-with-hover'))
    await waitFor(() => expect(setOpenIndexMock).toHaveBeenCalledWith(0))
  })
})
