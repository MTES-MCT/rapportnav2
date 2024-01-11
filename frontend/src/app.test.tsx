import { render, screen } from './test-utils.tsx'
import App from "./app.tsx";


describe('App', () => {
    test('should render', () => {
        const {container} = render(<App/>);
        expect(container.firstElementChild).not.toBeNull();
    });
});
