import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import { CompletenessForStatsStatusEnum, Mission } from '@common/types/mission-types.ts'
import { ExportReportType } from '../../../../common/types/mission-export-types.ts'
import MissionListExportDialog from '../mission-list-export.tsx'
import { MissionSourceEnum } from '@common/types/env-mission-types.ts'

const mockMission1 = {
  id: 1,
  startDateTimeUtc: '2024-01-01T00:00:00Z',
  endDateTimeUtc: '2024-01-12T01:00:00Z',
  missionSource: MissionSourceEnum.MONITORENV,
  completenessForStats: {
    status: CompletenessForStatsStatusEnum.COMPLETE
  },
  actions: []
} as Mission

const mockMission2 = {
  id: 2,
  startDateTimeUtc: '2024-01-02T00:00:00Z',
  endDateTimeUtc: '2024-01-02T01:00:00Z',
  missionSource: MissionSourceEnum.MONITORENV,
  completenessForStats: {
    status: CompletenessForStatsStatusEnum.INCOMPLETE
  },
  actions: []
} as Mission

const renderDialog = (props = {}) => {
  const defaultProps = {
    availableMissions: [],
    toggleDialog: vi.fn(),
    triggerExport: vi.fn(),
    variant: ExportReportType.PATROL,
    exportInProgress: false
  }
  return render(<MissionListExportDialog {...defaultProps} {...props} />)
}

const getElements = () => ({
  exportButton: screen.getByRole('button', { name: /exporter/i }),
  cancelButton: screen.getByRole('button', { name: /annuler/i }),
  dropdownToggle: screen.getByRole('combobox'),
  dropdownInput: screen.getByTestId('picker-toggle-input'),
  singleFileRadio: screen.getByRole('radio', {
    name: /j’exporte un seul fichier/i
  }),
  multipleFilesRadio: screen.getByRole('radio', {
    name: /exporter un fichier pour chaque mission sélectionnée/i
  })
})

describe('MissionListExportDialog', () => {
  describe('Rendering', () => {
    it('should display general elements', () => {
      renderDialog()
      const { singleFileRadio, multipleFilesRadio, exportButton } = getElements()

      expect(screen.getByText('Mission principale de la patrouille')).toBeInTheDocument()
      expect(singleFileRadio).toBeChecked()
      expect(multipleFilesRadio).not.toBeChecked()
      expect(exportButton).toBeDisabled()
    })

    it('should display the correct title based on variant', () => {
      renderDialog({ variant: ExportReportType.AEM })
      expect(screen.getByText('Exporter les tableaux AEM des missions sélectionnées')).toBeInTheDocument()

      renderDialog({ variant: ExportReportType.PATROL })
      expect(screen.getByText('Exporter les rapports de patrouille des missions sélectionnées')).toBeInTheDocument()
    })
  })

  describe('Interactions', () => {
    it('should handle radio button changes and dropdown selection', async () => {
      renderDialog({ availableMissions: [mockMission1, mockMission2] })
      const { dropdownToggle, dropdownInput, exportButton, singleFileRadio, multipleFilesRadio } = getElements()

      // Initial state assertions
      expect(dropdownInput).toHaveValue('')
      expect(singleFileRadio).toBeChecked()
      expect(multipleFilesRadio).not.toBeChecked()
      expect(exportButton).toBeDisabled()

      // Open dropdown and select an option
      fireEvent.click(dropdownToggle)
      const option = await screen.findByText('Mission #2024-01-01 - Ouverte par le CACEM - 0 action(s)')
      fireEvent.click(option)

      // Verify dropdown value and button state
      expect(dropdownInput).toHaveValue('1')
      expect(exportButton).not.toBeDisabled()

      // Toggle between radio buttons
      fireEvent.click(multipleFilesRadio)
      expect(multipleFilesRadio).toBeChecked()
      expect(singleFileRadio).not.toBeChecked()

      fireEvent.click(singleFileRadio)
      expect(singleFileRadio).toBeChecked()
      expect(multipleFilesRadio).not.toBeChecked()
      expect(exportButton).toBeDisabled()
    })
  })

  describe('Conditional Rendering', () => {
    it('should disable export button if all missions incomplete', () => {
      renderDialog({ availableMissions: [{ ...mockMission2, id: 1 }, mockMission2] })
      const { exportButton, multipleFilesRadio } = getElements()

      fireEvent.click(multipleFilesRadio)

      expect(exportButton).toBeDisabled()
    })
    it('should show a warning for incomplete missions for zipped missions', () => {
      renderDialog({ availableMissions: [mockMission1, mockMission2] })

      const { multipleFilesRadio } = getElements()
      fireEvent.click(multipleFilesRadio)

      expect(
        screen.getByText(
          'Attention, les missions suivantes ne seront pas exportées car le statut de la donnée est "à compléter" :'
        )
      ).toBeInTheDocument()
      expect(
        screen.getByText('Mission #2024-01-02 - Ouverte par le CACEM - 0 action(s)', { exact: false })
      ).toBeInTheDocument()
    })

    it('should not show a warning if all missions are complete for zipped missions', () => {
      renderDialog({ availableMissions: [mockMission1] })

      const { multipleFilesRadio } = getElements()
      fireEvent.click(multipleFilesRadio)

      expect(
        screen.queryByText(
          'Attention, les missions suivantes ne seront pas exportées car le statut de la donnée est "à compléter" :'
        )
      ).not.toBeInTheDocument()
    })
  })

  describe('Action Buttons', () => {
    it('should call toggleDialog when clicking cancel', () => {
      const mockToggle = vi.fn()
      renderDialog({ toggleDialog: mockToggle })

      const { cancelButton } = getElements()
      fireEvent.click(cancelButton)

      expect(mockToggle).toHaveBeenCalled()
    })

    it('should call triggerExport when clicking export', () => {
      const mockTrigger = vi.fn()
      renderDialog({ availableMissions: [mockMission1], triggerExport: mockTrigger })

      const { exportButton, multipleFilesRadio } = getElements()
      fireEvent.click(multipleFilesRadio)
      fireEvent.click(exportButton)

      expect(mockTrigger).toHaveBeenCalled()
    })
  })
})
