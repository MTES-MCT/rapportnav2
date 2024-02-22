import { render, screen } from '../../../test-utils'
import ActionSelectionDropdown from './action-selection-dropdown.tsx';

describe('ActionSelectionDropdown', () => {
    test('renders control selection options', () => {
        render(<ActionSelectionDropdown onSelect={vi.fn()}/>);

        expect(screen.getByText('Ajouter des contrôles')).toBeInTheDocument();
        expect(screen.getByText('Ajouter une note libre')).toBeInTheDocument();
        expect(screen.getByText('Ajouter une assistance / sauvetage')).toBeInTheDocument();
        expect(screen.getByText('Ajouter une autre activité de mission')).toBeInTheDocument();
    });

});
