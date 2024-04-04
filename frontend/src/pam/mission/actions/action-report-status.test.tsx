import { render, screen } from '../../../test-utils'
import ActionReportStatus from './action-report-status.tsx'
import { MissionStatusEnum } from '../../../types/mission-types.ts'
import { MissionSourceEnum } from '../../../types/env-mission-types.ts'
import { THEME } from '@mtes-mct/monitor-ui'
import { hexToRgb } from '../../../utils/colors.ts'

describe('ActionReportStatus', () => {
  test('renders that everything is complete when mission has ended and data is complete', () => {
    render(
      <ActionReportStatus
        missionStatus={MissionStatusEnum.ENDED}
        actionSource={MissionSourceEnum.RAPPORTNAV}
        dataIsComplete={true}
      />
    )
    const element = screen.getByText('Les champs indispensables aux statistiques sont remplis.')
    expect(element).toBeInTheDocument()
    expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.mediumSeaGreen))
  })
  test('renders data missing text in red when mission is ended', () => {
    render(
      <ActionReportStatus
        missionStatus={MissionStatusEnum.ENDED}
        actionSource={MissionSourceEnum.RAPPORTNAV}
        dataIsComplete={false}
      />
    )
    const element = screen.getByText("Des champs indispensables sont à remplir par l'unité.")
    expect(element).toBeInTheDocument()
    expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.maximumRed))
  })
  test('renders data missing text in charcoal when mission is ongoing', () => {
    render(
      <ActionReportStatus
        missionStatus={MissionStatusEnum.IN_PROGRESS}
        actionSource={MissionSourceEnum.RAPPORTNAV}
        dataIsComplete={false}
      />
    )
    const element = screen.getByText("Des champs indispensables sont à remplir par l'unité.")
    expect(element).toBeInTheDocument()
    expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.charcoal))
  })
  test('renders data missing text from CACEM', () => {
    render(
      <ActionReportStatus
        missionStatus={MissionStatusEnum.IN_PROGRESS}
        actionSource={MissionSourceEnum.MONITORENV}
        dataIsComplete={false}
      />
    )
    const element = screen.getByText('Des champs indispensables sont à remplir par le CACEM.')
    expect(element).toBeInTheDocument()
  })
  test('renders data missing text from CNSP', () => {
    render(
      <ActionReportStatus
        missionStatus={MissionStatusEnum.IN_PROGRESS}
        actionSource={MissionSourceEnum.MONITORFISH}
        dataIsComplete={false}
      />
    )
    const element = screen.getByText('Des champs indispensables sont à remplir par le CNSP.')
    expect(element).toBeInTheDocument()
  })
  test('renders the incomplete data icon', () => {
    render(
      <ActionReportStatus
        missionStatus={MissionStatusEnum.IN_PROGRESS}
        actionSource={MissionSourceEnum.MONITORFISH}
        dataIsComplete={false}
      />
    )
    expect(screen.getByTestId('report-incomplete')).toBeInTheDocument()
  })
  test('renders the complete data icon', () => {
    render(
      <ActionReportStatus
        missionStatus={MissionStatusEnum.IN_PROGRESS}
        actionSource={MissionSourceEnum.MONITORFISH}
        dataIsComplete={true}
      />
    )
    expect(screen.getByTestId('report-complete')).toBeInTheDocument()
  })
})
