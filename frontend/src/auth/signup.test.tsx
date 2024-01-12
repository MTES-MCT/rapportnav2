import { render, screen } from '../test-utils.tsx'
import SignUp from "./signup.tsx";


describe('SignUp', () => {
    test('should render', () => {
        render(<SignUp/>);
        expect(screen.getByText('S\'inscrire')).toBeInTheDocument();
    });
});
