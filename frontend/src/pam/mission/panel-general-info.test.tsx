import { render, screen } from '../../test-utils.tsx'
import MissionGeneralInfoPanel from "./panel-general-info.tsx";

const mock = {
    id: 1,
    startDateTimeUtc: '2024-01-01T00:00:00Z',
    endDateTimeUtc: '2024-01-12T01:00:00Z',
    actions: []
};
describe('MissionGeneralInfoPanel', () => {
    test('should render', () => {
        render(<MissionGeneralInfoPanel mission={mock}/>);
        expect(screen.getByText('Informations Générales')).toBeInTheDocument();
    });
});
