import { render, screen } from '../../../../../../../test-utils.tsx'
import MissionGeneralInformationFormPam from '../mission-general-information-form-pam.tsx'
import { useOnlineManager } from '../../../../../common/hooks/use-online-manager.tsx'
import { vi } from 'vitest'

// Mock the useOnlineManager hook
vi.mock('../../../../../common/hooks/use-online-manager.tsx', () => ({
  useOnlineManager: vi.fn()
}))

describe('MissionGeneralInformationFormPam Component', () => {
  it('renders', () => {
    vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false } as any)
    render(<MissionGeneralInformationFormPam generalInfo2={{}} onChange={vi.fn()} />)
  })

  describe('The datepicker', () => {
    it('should be disabled when offline', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false } as any)
      render(<MissionGeneralInformationFormPam generalInfo2={{}} onChange={vi.fn()} />)
      expect(
        screen.queryAllByTitle(
          "Non disponible hors ligne, il est nécessaire d'être synchronisé avec les centres pour saisir/modifier cette donnée."
        )
      ).not.toBeNull()
    })
    it('should be enabled when online', () => {
      vi.mocked(useOnlineManager).mockReturnValue({ isOnline: true } as any)
      render(<MissionGeneralInformationFormPam generalInfo2={{}} onChange={vi.fn()} />)
      expect(screen.queryByText('Non disponible hors ligne,', { exact: false })).toBeNull()
    })
  })

  describe('The datepicker', () => {
    it('should be disabled when offline', () => {
      vi.mocked(useOnlineManager).mockReturnValue({
        isOnline: false
      } as any)
      render(<MissionGeneralInformationFormPam generalInfo2={{}} onChange={vi.fn()} />)
      expect(
        screen.queryAllByTitle(
          "Non disponible hors ligne, il est nécessaire d'être synchronisé avec les centres pour saisir/modifier cette donnée."
        )
      ).not.toBeNull()
    })
    it('should be enabled when online', () => {
      vi.mocked(useOnlineManager).mockReturnValue({
        isOnline: true
      } as any)

      render(<MissionGeneralInformationFormPam generalInfo2={{}} onChange={vi.fn()} />)
      expect(screen.queryByText('Non disponible hors ligne,', { exact: false })).toBeNull()
    })
  })
})
