import { render, screen } from '../../../../test-utils.tsx'
import {
    ActionTargetTypeEnum,
    ActionTypeEnum,
    EnvAction,
    MissionSourceEnum
} from "../../../../types/env-mission-types.ts";
import { Action, ActionStatusType } from "../../../../types/action-types.ts";
import ActionEnvControl from "./timeline-item-control-env.tsx";


const actionMock = {
    id: '1',
    missionId: 1,
    type: ActionTypeEnum.CONTROL,
    source: MissionSourceEnum.MONITORENV,
    status: ActionStatusType.DOCKED,
    startDateTimeUtc: '2022-01-01T00:00:00Z',
    endDateTimeUtc: '2022-01-01T01:00:00Z',
    summaryTags: ["Avec PV", "Sans infraction"],
    data: {
        actionNumberOfControls: 3,
        actionTargetType: ActionTargetTypeEnum.VEHICLE,
        actionType: ActionTypeEnum.CONTROL,
        infractions: [],
        observations: null,
        geom: "MULTIPOINT ((-8.52318191 48.30305604))",
        themes: [{
            subThemes: ['subtheme1', 'subtheme2'],
            theme: 'rejets illicites'
        }]
    } as any as EnvAction
}


const props = (action: Action = actionMock, onClick = vi.fn()) => ({
    action,
    onClick
})
describe('ActionEnvControl', () => {
    test('should render', () => {
        render(<ActionEnvControl {...props()} />);
        expect(screen.getByText('ajouté par CACEM')).toBeInTheDocument();
    });
    describe('the title', () => {
        test('should render the theme', () => {
            render(<ActionEnvControl {...props()} />);
            expect(screen.getByText('rejets illicites')).toBeInTheDocument();
        });
        test('should render empty text when no theme', () => {
            const mock = {
                ...actionMock, data: {
                    ...actionMock.data, themes: [{
                        theme: undefined
                    }]
                }
            }
            render(<ActionEnvControl {...props(mock)} />);
            expect(screen.getByTestId('theme').textContent).toEqual('');
        });

    });
    describe('the target', () => {
        test('should render the target', () => {
            render(<ActionEnvControl {...props()} />);
            expect(screen.getByText('véhicule')).toBeInTheDocument();
        });
        test('should render the fallback', () => {
            const mock = {...actionMock, data: {...actionMock.data, actionTargetType: undefined}}
            render(<ActionEnvControl {...props(mock)} />);
            expect(screen.getByText('inconnu')).toBeInTheDocument();
        });
    });
    describe('the controls', () => {
        test('should render the amount of controls (singular)', () => {
            const mock = {...actionMock, data: {...actionMock.data, actionNumberOfControls: 1}}
            render(<ActionEnvControl {...props(mock)} />);
            expect(screen.getByText('1 contrôle')).toBeInTheDocument();
        });
        test('should render the amount of controls (plural)', () => {
            const mock = {...actionMock, data: {...actionMock.data, actionNumberOfControls: 3}}
            render(<ActionEnvControl {...props(mock)} />);
            expect(screen.getByText('3 contrôles')).toBeInTheDocument();
        });
        test('should render the fallback message (undefined)', () => {
            const mock = {...actionMock, data: {...actionMock.data, actionNumberOfControls: undefined}}
            render(<ActionEnvControl {...props(mock)} />);
            expect(screen.getByText('Nombre de contrôles inconnu')).toBeInTheDocument();
        });
        test('should render the fallback message (not set)', () => {
            const mock = {
                ...actionMock, data: {
                    actionTargetType: ActionTargetTypeEnum.VEHICLE,
                    actionType: ActionTypeEnum.CONTROL,
                    infractions: [],
                    observations: null,
                    geom: "MULTIPOINT ((-8.52318191 48.30305604))",
                }
            }
            render(<ActionEnvControl {...props(mock)} />);
            expect(screen.getByText('Nombre de contrôles inconnu')).toBeInTheDocument();
        });
        test('should render the fallback message (no action data)', () => {
            const mock = {...actionMock, data: undefined}
            render(<ActionEnvControl {...props(mock)} />);
            expect(screen.getByText('Nombre de contrôles inconnu')).toBeInTheDocument();
        });
    });
    describe('the tags', () => {
        test('should render the tags when no controls to complete', () => {
            render(<ActionEnvControl {...props()} />);
            expect(screen.getByText('Sans infraction')).toBeInTheDocument();
            expect(screen.getByText('Avec PV')).toBeInTheDocument();
        });
        test('should render the tags when controlsToComplete is 0', () => {
            const mock = {...actionMock, data: {...actionMock.data, controlsToComplete: []}}
            render(<ActionEnvControl {...props(mock)} />);
            expect(screen.getByText('Sans infraction')).toBeInTheDocument();
            expect(screen.getByText('Avec PV')).toBeInTheDocument();
        });
        test('should render the amount of controls to complete when specified', () => {
            const mock = {...actionMock, data: {...actionMock.data, controlsToComplete: ['x', 'y', 'z']}}
            render(<ActionEnvControl {...props(mock)} />);
            expect(screen.getByText('3 types de contrôles à compléter')).toBeInTheDocument();
        });
    });

});
