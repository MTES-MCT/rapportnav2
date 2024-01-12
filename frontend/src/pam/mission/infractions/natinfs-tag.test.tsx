import { render, screen } from '../../../test-utils'
import NatinfsTag from './natinfs-tag.tsx';

describe('NatinfsTag', () => {
    test('renders render the no infraction text', () => {
        render(<NatinfsTag natinfs={[]}/>);
        expect(screen.getByText('Sans infraction')).toBeInTheDocument();
    });
    test('renders render one natinf', () => {
        render(<NatinfsTag natinfs={['123']}/>);
        expect(screen.getByText('NATINF : 123')).toBeInTheDocument();
    });
    test('renders render two natinfs', () => {
        render(<NatinfsTag natinfs={['123', '456']}/>);
        expect(screen.getByText('2 NATINF : 123, 456')).toBeInTheDocument();
    });
});
