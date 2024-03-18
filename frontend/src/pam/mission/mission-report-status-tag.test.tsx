import { render, screen } from '../../test-utils.tsx'

import { MissionReportStatusEnum, MissionStatusEnum } from "../../types/mission-types.ts";
import MissionReportStatusTag from "./mission-report-status-tag.tsx";


describe('MissionReportStatusTag component', () => {
  test('renders "indisponible" when missionStatus is undefined', () => {
    render(<MissionReportStatusTag reportStatus={MissionReportStatusEnum.INCOMPLETE} missionStatus={undefined}/>)
    const tagElement = screen.getByText("Indisponible")
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "indisponible" when reportStatus is undefined', () => {
    render(<MissionReportStatusTag reportStatus={undefined} missionStatus={MissionStatusEnum.UPCOMING}/>)
    const tagElement = screen.getByText("Indisponible")
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "Données à jour" when missionStatus is IN_PROGRESS and reportStatus is COMPLETE', () => {
    render(<MissionReportStatusTag reportStatus={MissionReportStatusEnum.COMPLETE}
                                   missionStatus={MissionStatusEnum.IN_PROGRESS}/>)
    const tagElement = screen.getByText("Données à jour")
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "À compléter" when missionStatus is IN_PROGRESS and reportStatus is COMPLETE', () => {
    render(<MissionReportStatusTag reportStatus={MissionReportStatusEnum.INCOMPLETE}
                                   missionStatus={MissionStatusEnum.IN_PROGRESS}/>)
    const tagElement = screen.getByText("À compléter")
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "Complété" when missionStatus is ENDED and reportStatus is COMPLETE', () => {
    render(<MissionReportStatusTag reportStatus={MissionReportStatusEnum.COMPLETE}
                                   missionStatus={MissionStatusEnum.ENDED}/>)
    const tagElement = screen.getByText("Complété")
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "À compléter" when missionStatus is ENDED and reportStatus is COMPLETE', () => {
    render(<MissionReportStatusTag reportStatus={MissionReportStatusEnum.INCOMPLETE}
                                   missionStatus={MissionStatusEnum.ENDED}/>)
    const tagElement = screen.getByText("À compléter")
    expect(tagElement).toBeInTheDocument()
  })

})
