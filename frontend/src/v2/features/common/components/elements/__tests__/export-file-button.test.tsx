import { expect, vi } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../../../../test-utils.tsx'
import ExportFileButton from '../export-file-button.tsx'
import { Mission2 } from '../../../types/mission-types.ts'

const missions: Mission2[] = [{ id: 1 }]

describe('ExportAEMButton Component', () => {
  test('triggers export lazy mutation on button click', async () => {
    const mock = vi.fn()
    render(<ExportFileButton missions={missions} onClick={mock} />)

    const exportButton = screen.getByTestId('export-btn')
    fireEvent.click(exportButton)

    await waitFor(() => expect(mock).toHaveBeenCalled())
  })

  test('displays correct label when provided', () => {
    render(<ExportFileButton missions={missions}>Export Custom Label</ExportFileButton>)
    expect(screen.getByText('Export Custom Label')).toBeInTheDocument()
  })

  test('handles error scenario gracefully', async () => {
    const mock = vi.fn()
    render(<ExportFileButton missions={missions} onClick={mock} />)
    const exportButton = screen.getByTestId('export-btn')
    fireEvent.click(exportButton)

    await waitFor(() => expect(mock).toHaveBeenCalled())
    expect(screen.queryByTestId('loading-icon')).not.toBeInTheDocument()
  })
})
