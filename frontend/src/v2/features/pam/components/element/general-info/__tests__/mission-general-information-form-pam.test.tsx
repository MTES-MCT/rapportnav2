import { render, screen } from '../../../../../../../test-utils.tsx'
import MissionGeneralInformationFormPam from '../mission-general-information-form-pam.tsx'
import { vi } from 'vitest'

describe('MissionGeneralInformationFormPam Component', () => {
  it('renders', () => {
    render(<MissionGeneralInformationFormPam generalInfo2={{}} onChange={vi.fn()} />)
  })

  describe('The datepicker', () => {
    it('should be disabled when offline', () => {
      render(<MissionGeneralInformationFormPam generalInfo2={{}} onChange={vi.fn()} />)
      expect(
        screen.queryAllByTitle(
          "Non disponible hors ligne, il est nécessaire d'être synchronisé avec les centres pour saisir/modifier cette donnée."
        )
      ).not.toBeNull()
    })
    it('should be enabled when online', () => {
      render(<MissionGeneralInformationFormPam generalInfo2={{}} onChange={vi.fn()} />)
      expect(screen.queryByText('Non disponible hors ligne,', { exact: false })).toBeNull()
    })
  })
})
