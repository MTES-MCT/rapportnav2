import { render, screen } from '../test-utils.tsx'
import { FrontendErrorBoundary } from "./error-boundary.tsx";

const children = <div>child</div>

describe('FrontendErrorBoundary', () => {
    test('should render', () => {
        render(<FrontendErrorBoundary>{children}</FrontendErrorBoundary>);
        expect(screen.getByText('child')).toBeInTheDocument();
    });
});
