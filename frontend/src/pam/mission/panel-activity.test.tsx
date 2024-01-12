import { render, screen } from '../../test-utils.tsx'
import MissionActivityPanel from "./panel-activity.tsx";


describe('MissionActivityPanel', () => {
    test('should render', () => {
        render(<MissionActivityPanel/>);
        expect(screen.getByText('Activité du navire')).toBeInTheDocument();
    });
});
