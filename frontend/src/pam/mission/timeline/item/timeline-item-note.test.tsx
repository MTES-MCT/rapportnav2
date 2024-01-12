import { render, screen } from '../../../../test-utils.tsx'
import ActionNote from "./timeline-item-note.tsx";


describe('ActionNote', () => {
    test('should render', () => {
        const {container} = render(<ActionNote/>);
        expect(container.firstElementChild).toBeNull();
    });
});
