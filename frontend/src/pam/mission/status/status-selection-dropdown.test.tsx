import { render, screen } from '../../../test-utils'
import StatusSelectionDropdown from './status-selection-dropdown.tsx';

describe('StatusSelectionDropdown', () => {
    test('renders status selection options', () => {
        render(<StatusSelectionDropdown onSelect={vi.fn()}/>);

        expect(screen.getByText('Navigation')).toBeInTheDocument();
        expect(screen.getByText('Mouillage')).toBeInTheDocument();
        expect(screen.getByText('Présence à quai')).toBeInTheDocument();
        expect(screen.getByText('Indisponibilité')).toBeInTheDocument();
    });

});
