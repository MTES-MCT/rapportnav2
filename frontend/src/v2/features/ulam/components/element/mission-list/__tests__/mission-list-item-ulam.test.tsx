import { vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../../../test-utils.tsx'
import { MissionListItem, MissionReportTypeEnum, MissionSourceEnum } from '../../../../../common/types/mission-types.ts'
import { User } from '../../../../../common/types/user.ts'
import MissionListItemUlam from '../mission-list-item-ulam.tsx'

const mission1: MissionListItem = {
  id: 1,
  controlUnits: [],
  crew: [
    {
      id: '1',
      agent: {
        id: '1',
        firstName: 'John',
        lastName: 'Doe',
        services: []
      }
    },
    {
      id: '2',
      agent: {
        id: '2',
        firstName: 'Jane',
        lastName: 'Doe',
        services: []
      }
    }
  ],
  observationsByUnit: 'Je suis une observation',
  missionSource: MissionSourceEnum.MONITORENV,
  missionReportType: MissionReportTypeEnum.OFFICE_REPORT,
  startDateTimeUtc: new Date().toISOString()
}
const mission2 = {
  id: 2,
  controlUnits: [
    {
      id: 1,
      name: 'ControlUnit1',
      isArchived: false,
      administration: 'fake-administration',
      resources: [
        {
          id: 1,
          controlUnitId: 1,
          name: 'Renault Mégane'
        }
      ]
    }
  ],
  crew: [],
  missionSource: MissionSourceEnum.MONITORFISH,
  startDateTimeUtc: new Date().toISOString()
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
  const mockUser: User = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    controlUnitId: 1,
    email: 'john@doe.com'
  }

  const mockSetOpenIndex = vi.fn()

  test('should render "N/A" if report type is OFFICE_REPORT', () => {
    render(
      <MissionListItemUlam
        mission={mission1}
        index={0}
        openIndex={1}
        setOpenIndex={mockSetOpenIndex}
        missionsLength={2}
        user={mockUser}
      />
    )
    const noResource = screen.queryByText('N/A')
    expect(noResource).toBeInTheDocument()
  })

  test('should render "Renault Mégane" if ControlUnitResource contains Renaul Mégane', () => {
    render(
      <MissionListItemUlam
        mission={mission2}
        index={0}
        openIndex={1}
        setOpenIndex={mockSetOpenIndex}
        missionsLength={2}
        user={mockUser}
      />
    )
    const car = screen.queryByTestId('mission-list-item-control_unit_resources')
    expect(car).toHaveTextContent('Renault Mégane')
  })

  test('should render all properties for ulam', () => {
    render(
      <MissionListItemUlam
        index={0}
        openIndex={1}
        setOpenIndex={mockSetOpenIndex}
        missionsLength={2}
        mission={mission1}
        user={mockUser}
      />
    )
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

  test('should toggles open state when clicking on the list item', async () => {
    const setOpenIndexMock = vi.fn()

    render(
      <MissionListItemUlam
        mission={mission1}
        index={0}
        setOpenIndex={setOpenIndexMock}
        missionsLength={2}
        openIndex={null}
      />
    )

    fireEvent.click(screen.getByTestId('mission-list-item-with-hover'))
    await waitFor(() => expect(setOpenIndexMock).toHaveBeenCalledWith(0))
  })
})
