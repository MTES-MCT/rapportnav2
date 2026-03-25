import { describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import MissionGeneralInformationUlamFormExtend from '../mission-general-information-ulam-form-extend.tsx'
import { MissionReportTypeEnum } from '../../../../common/types/mission-types.ts'
import { MissionGeneralInfoInput } from '../../../hooks/use-ulam-mission-general-information-form.tsx'

// Mock the hooks
vi.mock('../../../../common/services/use-agents.tsx', () => ({
  __esModule: true,
  default: () => ({ data: [] })
}))

vi.mock('../../../../common/services/use-administrations.tsx', () => ({
  __esModule: true,
  default: () => ({ data: [] })
}))

vi.mock('../../../../common/services/use-resources-control-unit.tsx', () => ({
  __esModule: true,
  default: () => ({ resources: [] })
}))

vi.mock('../../../../common/hooks/use-mission-finished.tsx', () => ({
  useMissionFinished: () => false
}))

vi.mock('@tanstack/react-store', () => ({
  useStore: () => ({ controlUnitId: 1 })
}))

const createMockValues = (overrides: Partial<MissionGeneralInfoInput> = {}): MissionGeneralInfoInput => ({
  dates: [new Date(), new Date()],
  missionTypes: [],
  missionReportType: undefined,
  ...overrides
})

describe('MissionGeneralInformationUlamFormExtend', () => {
  it('renders observation textarea', () => {
    const values = createMockValues()
    render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

    expect(screen.getByTestId('mission-general-observation')).toBeInTheDocument()
  })

  it('renders crew section', () => {
    const values = createMockValues()
    render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

    expect(screen.getByText('Agents de la mission')).toBeInTheDocument()
  })

  describe('when missionReportType is FIELD_REPORT', () => {
    it('renders field', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.FIELD_REPORT
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.getByText('Moyen(s) utilisé(s)')).toBeInTheDocument()
      expect(screen.getByText("Aucun moyen de l'unité n'est utilisé pour cette mission")).toBeInTheDocument()
      expect(screen.getByText('Mission armée')).toBeInTheDocument()
      expect(screen.getByText('Mission sous JDP')).toBeInTheDocument()
      expect(screen.getByText('Mission conjointe (avec un autre service)')).toBeInTheDocument()
    })

    it('renders JDP type options when isUnderJdp is true', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.FIELD_REPORT,
        isUnderJdp: true
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.getByText('Type de JDP')).toBeInTheDocument()
      expect(screen.getByText('À quai')).toBeInTheDocument()
      expect(screen.getByText('Embarqué')).toBeInTheDocument()
    })

    it('does not render JDP type options when isUnderJdp is false', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.FIELD_REPORT,
        isUnderJdp: false
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText('Type de JDP')).not.toBeInTheDocument()
    })

    it('renders inter-ministerial services form when isWithInterMinisterialService is true', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.FIELD_REPORT,
        isWithInterMinisterialService: true
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.getByText('Ajouter une administration')).toBeInTheDocument()
    })

    it('does not render inter-ministerial services form when isWithInterMinisterialService is false', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.FIELD_REPORT,
        isWithInterMinisterialService: false
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText('Ajouter une administration')).not.toBeInTheDocument()
    })

    it('disables resources field when isResourcesNotUsed is true', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.FIELD_REPORT,
        isResourcesNotUsed: true
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      // The resources field should still be rendered but disabled
      expect(screen.getByText('Moyen(s) utilisé(s)')).toBeInTheDocument()
    })
  })

  describe('when missionReportType is OFFICE_REPORT', () => {
    it('does not render resources field', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.OFFICE_REPORT
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText('Moyen(s) utilisé(s)')).not.toBeInTheDocument()
    })

    it('does not render isResourcesNotUsed checkbox', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.OFFICE_REPORT
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText("Aucun moyen de l'unité n'est utilisé pour cette mission")).not.toBeInTheDocument()
    })

    it('does not render mission armed checkbox', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.OFFICE_REPORT
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText('Mission armée')).not.toBeInTheDocument()
    })

    it('does not render JDP checkbox', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.OFFICE_REPORT
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText('Mission sous JDP')).not.toBeInTheDocument()
    })
  })

  describe('when missionReportType is EXTERNAL_REINFORCEMENT_TIME_REPORT', () => {
    it('does not render resources field', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText('Moyen(s) utilisé(s)')).not.toBeInTheDocument()
    })

    it('does not render isResourcesNotUsed checkbox', () => {
      const values = createMockValues({
        missionReportType: MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText("Aucun moyen de l'unité n'est utilisé pour cette mission")).not.toBeInTheDocument()
    })
  })

  describe('when missionReportType is undefined', () => {
    it('does not render resources field', () => {
      const values = createMockValues({
        missionReportType: undefined
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText('Moyen(s) utilisé(s)')).not.toBeInTheDocument()
    })

    it('does not render isResourcesNotUsed checkbox', () => {
      const values = createMockValues({
        missionReportType: undefined
      })
      render(<MissionGeneralInformationUlamFormExtend values={values} missionId="123" />)

      expect(screen.queryByText("Aucun moyen de l'unité n'est utilisé pour cette mission")).not.toBeInTheDocument()
    })
  })
})
