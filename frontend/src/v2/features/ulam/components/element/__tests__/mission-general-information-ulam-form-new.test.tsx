
import MissionGeneralInformationUlamFormNew from '../mission-general-information-ulam-form-new.tsx'
import { render, screen } from '../../../../../../test-utils.tsx'
import { vi } from 'vitest'
import useCreateMissionMutation from '../../../services/use-create-mission.tsx'
import { fireEvent } from '@testing-library/react'


vi.mock('../../../services/use-create-mission', () => ({
  __esModule: true,
  default: vi.fn(),
}));

describe('MissionGeneralInformationUlamFormNew', () => {
  it('renders the form with initial values', () => {
    render(
      <MissionGeneralInformationUlamFormNew
        onClose={vi.fn()}
      />
    )

    expect(screen.getByText('Type de mission')).toBeInTheDocument()
  })

  /*it('check if form submitted', () => {
    const consoleSpy = vi.spyOn(console, 'log').mockImplementation(() => {});

    render(
      <MissionGeneralInformationUlamFormNew
        onClose={vi.fn()}
      />
    );

    fireEvent.click(screen.getByText('Créer le rapport'));

    // TODO: a remplacer par le check d'un appel api
    expect(consoleSpy).toHaveBeenCalledWith(expect.stringContaining('Form Submitted'), expect.any(Object));

  });*/
})
