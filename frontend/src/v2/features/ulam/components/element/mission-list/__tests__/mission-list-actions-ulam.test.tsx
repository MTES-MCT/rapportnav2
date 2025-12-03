import { render, screen, fireEvent } from '../../../../../../../test-utils.tsx'
import { describe, it, vi, expect } from 'vitest'
import MissionListActionsUlam from '../mission-list-actions-ulam.tsx'

const mockOnClickExport = vi.fn()

describe('MissionListHeader component', () => {
  it('renders the ExportFileButton with the correct text', () => {
    render(<MissionListActionsUlam onClickExport={mockOnClickExport} exportIsLoading={false} />)

    expect(screen.getByText('Exporter le tableau AEM du mois')).toBeInTheDocument()
  })

  it('calls onClickExport when the ExportFileButton is clicked', () => {
    render(<MissionListActionsUlam onClickExport={mockOnClickExport} exportIsLoading={false} />)

    const exportButton = screen.getByText('Exporter le tableau AEM du mois')
    fireEvent.click(exportButton)

    expect(mockOnClickExport).toHaveBeenCalled()
  })

  it('displays a loading state when exportIsLoading is true', () => {
    render(<MissionListActionsUlam onClickExport={mockOnClickExport} exportIsLoading={true} />)
    expect(screen.getByTestId('loading-icon')).toBeInTheDocument()
  })
})
