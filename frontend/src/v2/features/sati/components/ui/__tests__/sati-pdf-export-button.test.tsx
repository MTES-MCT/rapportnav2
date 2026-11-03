import { beforeEach, describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import useSatiPdfExport from '../../../services/use-sati-pdf-export.tsx'
import SatiPdfExportButton from '../sati-pdf-export-button.tsx'

vi.mock('../../../services/use-sati-pdf-export.tsx', () => ({
  default: vi.fn()
}))

const mockMutation = (overrides: { isPending?: boolean } = {}) => {
  const mutate = vi.fn()
  vi.mocked(useSatiPdfExport).mockReturnValue({ mutate, isPending: false, ...overrides } as any)
  return mutate
}

describe('SatiPdfExportButton', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('exports the pdf of the action on click', () => {
    const mutate = mockMutation()

    render(<SatiPdfExportButton ownerId="761" actionId="12345" />)
    fireEvent.click(screen.getByTestId('sati-pdf-export-button'))

    expect(mutate).toHaveBeenCalledWith({ ownerId: '761', actionId: '12345' })
  })

  it('is disabled while the export is pending', () => {
    mockMutation({ isPending: true })

    render(<SatiPdfExportButton ownerId="761" actionId="12345" />)

    expect(screen.getByTestId('sati-pdf-export-button')).toBeDisabled()
  })

  it('is disabled and explains why when the action is not exportable', () => {
    const mutate = mockMutation()

    render(<SatiPdfExportButton ownerId="761" actionId="12345" disabled={true} />)
    const button = screen.getByTestId('sati-pdf-export-button')
    fireEvent.click(button)

    expect(button).toBeDisabled()
    expect(button).toHaveAttribute(
      'title',
      "Le PDF SATI est disponible une fois l'action complète pour les statistiques"
    )
    expect(mutate).not.toHaveBeenCalled()
  })
})
