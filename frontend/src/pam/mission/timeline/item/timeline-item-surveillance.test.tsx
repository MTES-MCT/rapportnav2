import { render } from '../../../../test-utils.tsx'
import ActionSurveillance from "./timeline-item-surveillance.tsx";


describe('ActionSurveillance', () => {
    test('should render', () => {
        const {container} = render(<ActionSurveillance/>);
        // expect(container.firstElementChild).toBeNull();
    });
});
