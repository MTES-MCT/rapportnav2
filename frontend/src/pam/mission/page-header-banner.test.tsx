import { render, screen } from '../../test-utils.tsx'
import MissionPageHeaderBanner from './page-header-banner.tsx'
import { MissionReportStatusEnum } from '../../types/mission-types.ts'
import { MissionSourceEnum } from '../../types/env-mission-types.ts'
import { hexToRgb } from '../../utils/colors.ts'
import { THEME } from '@mtes-mct/monitor-ui'

describe('MissionPageHeaderBanner', () => {
  describe('testing the text', () => {
    test('should render the green text saying the report is complete', () => {
      const reportStatus = { status: MissionReportStatusEnum.COMPLETE }
      render(<MissionPageHeaderBanner reportStatus={reportStatus} />)
      const element = screen.getByText('La mission est terminée et ses données sont complètes', { exact: false })
      expect(element).toBeInTheDocument()
      expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.mediumSeaGreen))
    })
    test('should render the text incomplete when only missing data from unit', () => {
      const reportStatus = { status: MissionReportStatusEnum.INCOMPLETE, source: [MissionSourceEnum.RAPPORTNAV] }
      render(<MissionPageHeaderBanner reportStatus={reportStatus} />)
      const element = screen.getByText(
        "La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir par votre unité."
      )
      expect(element).toBeInTheDocument()
      expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.maximumRed))
    })
    test('should render the text incomplete when missing data from several sources including the unit', () => {
      const reportStatus = {
        status: MissionReportStatusEnum.INCOMPLETE,
        source: [MissionSourceEnum.MONITORFISH, MissionSourceEnum.RAPPORTNAV]
      }
      render(<MissionPageHeaderBanner reportStatus={reportStatus} />)
      const element = screen.getByText(
        "La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir par votre unité."
      )
      expect(element).toBeInTheDocument()
      expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.maximumRed))
    })

    test('should render the text incomplete when missing data from CNSP & CACEM', () => {
      const reportStatus = {
        status: MissionReportStatusEnum.INCOMPLETE,
        sources: [MissionSourceEnum.MONITORFISH, MissionSourceEnum.MONITORENV]
      }
      render(<MissionPageHeaderBanner reportStatus={reportStatus} />)
      const element = screen.getByText(
        "La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir par le CNSP et le CACEM."
      )
      expect(element).toBeInTheDocument()
      expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.charcoal))
    })
    test('should render the text incomplete when missing data from CACEM', () => {
      const reportStatus = { status: MissionReportStatusEnum.INCOMPLETE, sources: [MissionSourceEnum.MONITORENV] }
      render(<MissionPageHeaderBanner reportStatus={reportStatus} />)
      const element = screen.getByText(
        "La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir par le CACEM."
      )
      expect(element).toBeInTheDocument()
      expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.charcoal))
    })
    test('should render the text incomplete when missing data from CNSP', () => {
      const reportStatus = { status: MissionReportStatusEnum.INCOMPLETE, sources: [MissionSourceEnum.MONITORFISH] }
      render(<MissionPageHeaderBanner reportStatus={reportStatus} />)
      const element = screen.getByText(
        "La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir par le CNSP."
      )
      expect(element).toBeInTheDocument()
      expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.charcoal))
    })
  })
  describe('testing the action', () => {
    test('should render the hide button as the banner is collapsible when not success', () => {
      const reportStatus = { status: MissionReportStatusEnum.INCOMPLETE, sources: [MissionSourceEnum.MONITORFISH] }
      render(<MissionPageHeaderBanner reportStatus={reportStatus} />)
      const element = screen.getByText('Masquer')
      expect(element).toBeInTheDocument()
    })
    test('should render the close button as the banner is closable when success', () => {
      const reportStatus = { status: MissionReportStatusEnum.COMPLETE, sources: [MissionSourceEnum.MONITORFISH] }
      render(<MissionPageHeaderBanner reportStatus={reportStatus} />)
      const element = screen.getByTitle('fermer')
      expect(element).toBeInTheDocument()
    })
  })
})
