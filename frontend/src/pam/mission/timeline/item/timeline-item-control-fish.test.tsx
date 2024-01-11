import { render, screen } from '../../../../test-utils.tsx'
import { Action } from "@sentry/react/types/types";
import {
    ActionTargetTypeEnum,
    ActionTypeEnum,
    FishAction,
    MissionSourceEnum
} from "../../../../types/env-mission-types.ts";
import { ActionStatusType } from "../../../../types/action-types.ts";
import ActionFishControl from "./timeline-item-control-fish.tsx";

const actionMock = {
    id: '1',
    missionId: 1,
    type: ActionTypeEnum.CONTROL,
    source: MissionSourceEnum.MONITORENV,
    status: ActionStatusType.DOCKED,
    startDateTimeUtc: '2022-01-01T00:00:00Z',
    endDateTimeUtc: null,
    summaryTags: ["Avec PV", "Sans infraction"],
    data: {
        actionDatetimeUtc: '2022-01-01T00:00:00Z'
    } as any as FishAction
}


const props = (action: Action = actionMock, onClick = vi.fn()) => ({
    action,
    onClick
})
describe('ActionFishControl', () => {
    test('should render', () => {
        render(<ActionFishControl {...props()} />);
        expect(screen.getByText('ajouté par CNSP')).toBeInTheDocument();
    });


    describe('the tags', () => {
        test('should render the tags when no controls to complete', () => {
            render(<ActionFishControl {...props()} />);
            expect(screen.getByText('Sans infraction')).toBeInTheDocument();
            expect(screen.getByText('Avec PV')).toBeInTheDocument();
        });
        test('should render the tags when controlsToComplete is 0', () => {
            const mock = {...actionMock, data: {...actionMock.data, controlsToComplete: []}}
            render(<ActionFishControl {...props(mock)} />);
            expect(screen.getByText('Sans infraction')).toBeInTheDocument();
            expect(screen.getByText('Avec PV')).toBeInTheDocument();
        });
        test('should render the amount of controls to complete when specified', () => {
            const mock = {...actionMock, data: {...actionMock.data, controlsToComplete: ['x', 'y', 'z']}}
            render(<ActionFishControl {...props(mock)} />);
            expect(screen.getByText('3 types de contrôles à compléter')).toBeInTheDocument();
        });
    });

});
