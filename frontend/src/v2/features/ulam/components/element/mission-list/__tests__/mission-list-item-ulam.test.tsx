import { vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../../../test-utils.tsx'
import {
  MissionListItem,
  MissionReportTypeEnum,
  MissionSourceEnum
} from '../../../../../common/types/mission-types.ts'
import MissionListItemUlam from '../mission-list-item-ulam.tsx'

const mission1 = {
  id: 1,
  controlUnits: [],
  crew: [],
  observationsByUnit: 'Je suis une observation',
  missionSource: MissionSourceEnum.MONITORENV,
  missionReportType: MissionReportTypeEnum.OFFICE_REPORT,
  startDateTimeUtc: new Date().toISOString(),
}
const mission2 = {
  id: 2,
  controlUnits: [
    {
      id: 1,
      name: "ControlUnit1",
      isArchived: false,
      administration: "fake-administration",
      resources: [
        {
          id: 1,
          controlUnitId: 1,
          name: "Renault Mégane",
        }
      ]
    }
  ],
  crew: [],
  missionSource: MissionSourceEnum.MONITORFISH,
  startDateTimeUtc: new Date().toISOString(),

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

describe.skip('MissionListItem component', () => {
  const missions: MissionListItem[] = [
    {
      id: 1,
      crew: [],
      missionSource: MissionSourceEnum.MONITORENV,
      startDateTimeUtc: new Date().toISOString(),
      observationsByUnit: 'Observation 1',
    },
    {
      id: 2,
      crew: [],
      missionSource: MissionSourceEnum.MONITORFISH,
      startDateTimeUtc: new Date().toISOString(),
      observationsByUnit: 'Observation 2',
    }
  ]

  const mockSetOpenIndex = vi.fn()

  test('should render "N/A" if no ControlUnitResource provided and report type is OFFICE_REPORT', () => {

    render(<MissionListItemUlam mission={mission1} index={0} openIndex={1} setOpenIndex={mockSetOpenIndex} missionsLength={2} />)
    const noResource = screen.queryByText('N/A')
    expect(noResource).toBeInTheDocument()
  })

  test('should render "Renault Mégane" if ControlUnitResource contains CAR', () => {
    render(<MissionListItemUlam mission={mission2} index={0} openIndex={1} setOpenIndex={mockSetOpenIndex} missionsLength={2} />)
    const car = screen.queryByTestId('mission-list-item-control_unit_resources')
    expect(car).toHaveTextContent('Renault Mégane')
  })

  test.skip('should render all properties for ulam', () => {
    render(<MissionListItemUlam  index={0} openIndex={1} setOpenIndex={mockSetOpenIndex} />)
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
        mission={mission1}
        index={0}
        openIndex={1}
        setOpenIndex={mockSetOpenIndex}
        missionsLength={2}
      />
    )
    const missionCrew = screen.queryByTestId('mission-list-item-crew__text')
    expect(missionCrew).toHaveStyle('text-overflow: ellipsis')
    expect(missionCrew).toHaveStyle(`overflow: hidden`)
    expect(missionCrew).toHaveStyle(`white-space: nowrap`)
  })

  it('toggles open state when clicking on the list item', async () => {
    const setOpenIndexMock = vi.fn()

    render(<MissionListItemUlam mission={mission1} index={0} setOpenIndex={setOpenIndexMock} missionsLength={2} openIndex={0} />)

    fireEvent.click(screen.getByTestId('mission-list-item-with-hover'))
    await waitFor(() => expect(setOpenIndexMock).toHaveBeenCalledWith(0))
  })
})
