import { render, screen } from '../../test-utils.tsx'
import MissionOperationalSummaryPanel from "./panel-operational-summary.tsx";


describe('MissionOperationalSummaryPanel', () => {
    test('should render', () => {
        render(<MissionOperationalSummaryPanel/>);
        expect(screen.getByText('Bilan opérationnel')).toBeInTheDocument();
    });
});
