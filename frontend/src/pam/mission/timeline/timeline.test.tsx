import { render, screen } from '../../../test-utils.tsx'
import Timeline from "./timeline";
import { ActionControl, ActionStatusType } from "../../../types/action-types.ts";
import { vi } from "vitest";
import useGetMissionTimeline from "./use-mission-timeline.tsx";
import { Mission, VesselTypeEnum } from "../../../types/mission-types.ts";
import { GraphQLError } from "graphql/error";
import { ActionTypeEnum, MissionSourceEnum } from "../../../types/env-mission-types.ts";
import { ControlMethod } from "../../../types/control-types.ts";
import { fireEvent } from "@testing-library/react";

vi.mock("./use-mission-timeline.tsx", async (importOriginal) => {
    const actual = await importOriginal()
    return {
        ...actual,
        default: vi.fn()
    }
})


const actionMock1 = {
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

// set a different date
const actionMock2 = {...actionMock1, startDateTimeUtc: '2022-01-02T00:00:00Z'}

const actionStatusMock = {...actionMock2, type: ActionTypeEnum.STATUS,}


const mockedQueryResult = (mission?: Mission, loading: boolean = false, error: any = undefined) => ({
    data: mission,
    loading,
    error,
})


const props = () => ({
    missionId: '1',
    onSelectAction: vi.fn()
})
describe('Timeline', () => {
    describe('testing rendering', () => {
        test('should render loading', () => {
            ;(useGetMissionTimeline as any).mockReturnValue(mockedQueryResult(undefined, true))
            render(<Timeline {...props()}/>);
            expect(screen.getByTestId('timeline-loading')).toBeInTheDocument();
        });
        test('should render error', () => {
            ;(useGetMissionTimeline as any).mockReturnValue(mockedQueryResult(undefined, false, new GraphQLError("Error!")))
            render(<Timeline {...props()}/>);
            expect(screen.getByTestId('timeline-error')).toBeInTheDocument();
        });
        test('should render empty action message', () => {
            const actions = {actions: []}
            ;(useGetMissionTimeline as any).mockReturnValue(mockedQueryResult(actions))
            render(<Timeline {...props()}/>);
            expect(screen.getByText('Aucune action n\'est ajoutée pour le moment')).toBeInTheDocument();
        });
        test('should render content', () => {
            const actions = {actions: [actionMock1, actionMock2]}
            ;(useGetMissionTimeline as any).mockReturnValue(mockedQueryResult(actions))
            render(<Timeline {...props()}/>);
            expect(screen.getByTestId('timeline-content')).toBeInTheDocument();
        });
        test('should render 1 less divider as there are actions', () => {
            const actions = {actions: [actionMock1, actionMock2]}
            ;(useGetMissionTimeline as any).mockReturnValue(mockedQueryResult(actions))
            render(<Timeline {...props()}/>);
            expect(screen.queryAllByTestId('timeline-day-divider')).toHaveLength(actions.actions.length - 1);
        });
        test('should not render status tag for status actions', () => {
            const actions = {actions: [actionMock1, actionStatusMock]}
            ;(useGetMissionTimeline as any).mockReturnValue(mockedQueryResult(actions))
            render(<Timeline {...props()}/>);
            expect(screen.queryAllByTestId('timeline-item-status')).toHaveLength(actions.actions.length - 1);
        });
        test('should render the date in french', () => {
            const actions = {actions: [actionMock1, actionMock2]}
            ;(useGetMissionTimeline as any).mockReturnValue(mockedQueryResult(actions))
            render(<Timeline {...props()}/>);
            expect(screen.getByText('01 janv.')).toBeInTheDocument()
            expect(screen.getByText('02 janv.')).toBeInTheDocument()
        });
    });
    describe('testing the action', () => {
        test('should call onSelect when clicking an item', () => {
            const actions = {actions: [actionMock1, actionStatusMock]}
            ;(useGetMissionTimeline as any).mockReturnValue(mockedQueryResult(actions))
            const spy = vi.fn()
            render(<Timeline {...{...props(), onSelectAction: spy}}/>);

            const item = screen.getByText('en Mer - Navire de pêche professionnelle')
            fireEvent.click(item)
            expect(spy).toHaveBeenCalled()
        });
    });
});
