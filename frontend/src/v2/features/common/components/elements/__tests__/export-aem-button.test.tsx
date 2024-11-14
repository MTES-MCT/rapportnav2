import { expect, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '../../../../../../test-utils.tsx';
import ExportAEMButton from '../export-aem-button.tsx';
import { Mission } from '@common/types/mission-types.ts';
import * as MissionAEMExportModule from '../../../services/use-lazy-mission-aem-export.tsx';

const exportLazyAEMMock = vi.fn();
const missions: Mission[] = [{ id: 1 }];

describe('ExportAEMButton Component', () => {
  beforeEach(() => {
    vi.spyOn(MissionAEMExportModule, 'useLazyMissionAEMExportMutation').mockReturnValue([
      exportLazyAEMMock,
      { error: undefined },
    ]);

    exportLazyAEMMock.mockResolvedValue({
      data: {
        missionAEMExportV2: {
          fileName: 'test.ods',
          fileContent: 'base64EncodedContent',
        },
      },
      loading: false,
      error: null,
    });
  });

  afterEach(() => {
    vi.clearAllMocks();
    vi.resetAllMocks();
  });

  test('triggers export lazy mutation on button click', async () => {
    render(<ExportAEMButton missions={missions} />);

    const exportButton = screen.getByTestId('aem-export-btn');
    fireEvent.click(exportButton);

    await waitFor(() => expect(exportLazyAEMMock).toHaveBeenCalled());
  });

  test('displays correct label when provided', () => {
    render(<ExportAEMButton missions={missions} label="Export Custom Label" />);
    expect(screen.getByText('Export Custom Label')).toBeInTheDocument();
  });

  test('handles error scenario gracefully', async () => {
    exportLazyAEMMock.mockResolvedValueOnce({
      data: null,
      loading: false,
      error: { message: 'An error occurred' },
    });

    render(<ExportAEMButton missions={missions} />);
    const exportButton = screen.getByTestId('aem-export-btn');
    fireEvent.click(exportButton);

    await waitFor(() => expect(exportLazyAEMMock).toHaveBeenCalled());
    expect(screen.queryByTestId('loading-icon')).not.toBeInTheDocument();
  });
});
