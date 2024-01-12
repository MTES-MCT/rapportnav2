import { render, screen, fireEvent } from '../../../test-utils'
import ActionSelectionDropdown from './action-selection-dropdown.tsx';
import { VesselTypeEnum } from '../../../types/mission-types';
import { ActionTypeEnum, missionTypeEnum } from '../../../types/env-mission-types';

describe('ActionSelectionDropdown', () => {
    test('renders control selection options', () => {
        render(<ActionSelectionDropdown onSelect={vi.fn()}/>);

        expect(screen.getByText('Ajouter des contrôles')).toBeInTheDocument();
        expect(screen.getByText('Ajouter une note libre')).toBeInTheDocument();
        expect(screen.getByText('Ajouter une assistance / sauvetage')).toBeInTheDocument();
        expect(screen.getByText('Ajouter une autre activité de mission')).toBeInTheDocument();
    });

});
