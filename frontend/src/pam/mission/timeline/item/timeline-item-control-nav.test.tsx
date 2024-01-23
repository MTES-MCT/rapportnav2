import { render, screen } from '../../../../test-utils.tsx'
import { Action } from "@sentry/react/types/types";
import {
    ActionTargetTypeEnum,
    ActionTypeEnum,
    NavAction,
    MissionSourceEnum
} from "../../../../types/env-mission-types.ts";
import { ActionControl, ActionStatusType } from "../../../../types/action-types.ts";
import ActionNavControl from "./timeline-item-control-nav.tsx";
import { ControlMethod } from "../../../../types/control-types.ts";
import { VesselTypeEnum } from "../../../../types/mission-types.ts";


const actionMock = {
    id: '1',
    missionId: 1,
    type: ActionTypeEnum.CONTROL,
    source: MissionSourceEnum.RAPPORTNAV,
    status: ActionStatusType.DOCKED,
    startDateTimeUtc: '2022-01-01T00:00:00Z',
    endDateTimeUtc: '2022-01-01T01:00:00Z',
    summaryTags: undefined,
    controlsToComplete: undefined,
    data: {
        controlMethod: ControlMethod.SEA,
        latitude: 123,
        longitude: 123,
        startDateTimeUtc: '2022-01-01T00:00:00Z',
        endDateTimeUtc: '2022-01-01T01:00:00Z',
        vesselType: VesselTypeEnum.FISHING
    } as any as ActionControl
}


const props = (action: Action = actionMock, onClick = vi.fn()) => ({
    action,
    onClick
})
describe('ActionNavControl', () => {
    test('should render', () => {
        render(<ActionNavControl {...props()} />);
        expect(screen.getByText('en Mer - Navire de pêche professionnelle')).toBeInTheDocument();
    });


    describe('the tags', () => {
        test('should not render the tags when no tags', () => {
            const mock = {...actionMock, summaryTags: undefined}
            render(<ActionNavControl {...props(mock)} />);
            expect(screen.queryByTestId('nav-summary-tags')).toBeNull();
        });
        test('should not render the tags when controlsToComplete is 0', () => {
            const mock = {...actionMock, summaryTags: []}
            render(<ActionNavControl {...props(mock)} />);
            expect(screen.queryByTestId('nav-summary-tags')).toBeNull();
        });
        test('should render the amount of controls to complete when specified', () => {
            const mock = {...actionMock, summaryTags: ['tag1', 'tag2', 'tag3']}
            render(<ActionNavControl {...props(mock)} />);
            expect(screen.getByText('tag1')).toBeInTheDocument();
        });
    });

});
