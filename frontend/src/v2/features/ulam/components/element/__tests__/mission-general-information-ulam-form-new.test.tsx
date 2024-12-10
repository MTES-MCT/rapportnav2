
import { MissionTypeEnum } from '@common/types/env-mission-types'
import MissionGeneralInformationUlamFormNew from '../mission-general-information-ulam-form-new.tsx'
import { render, fireEvent, screen } from '../../../../../../test-utils.tsx'



describe('MissionGeneralInformationUlamFormNew', () => {
  it('renders the form with initial values', () => {
    render(
      <MissionGeneralInformationUlamFormNew
        startDateTimeUtc="2024-01-01T00:00:00Z"
        endDateTimeUtc="2024-01-01T00:00:00Z"
        missionTypes={[MissionTypeEnum.AIR]}
        onClose={vi.fn()}
      />
    )

    expect(screen.getByText('Type de mission')).toBeInTheDocument()
  })

  it('check if form submitted', () => {
    const consoleSpy = vi.spyOn(console, 'log').mockImplementation(() => {});

    render(
      <MissionGeneralInformationUlamFormNew
        startDateTimeUtc="2024-01-01T00:00:00Z"
        endDateTimeUtc="2024-01-01T00:00:00Z"
        missionTypes={[MissionTypeEnum.AIR]}
        onClose={vi.fn()}
      />
    );

    fireEvent.click(screen.getByText('Créer le rapport'));

    // TODO: a remplacer par le check d'un appel api
    expect(consoleSpy).toHaveBeenCalledWith(expect.stringContaining('Form Submitted'), expect.any(Object));

    consoleSpy.mockRestore();
  });
})
