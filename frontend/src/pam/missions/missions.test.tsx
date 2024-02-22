import { render, screen } from '../../test-utils'
import { vi } from 'vitest';
import useMissions from "./use-missions.tsx";
import { Mission } from "../../types/mission-types.ts";
import Missions from "./missions.tsx";
import { GraphQLError } from "graphql/error";


vi.mock("./use-missions.tsx", async (importOriginal) => {
    const actual = await importOriginal()
    return {
        ...actual,
        default: vi.fn()
    }
})

const mission = {
    id: '123',
    agent: {
        id: 'abc',
        firstName: 'firstName',
        lastName: 'lastName',
        services: []
    },
    comment: undefined,
    role: undefined
}

const mockedQueryResult = (crew?: Mission[], loading: boolean = false, error: any = undefined) => ({
    data: crew,
    loading,
    error,
})


describe('Missions', () => {
    describe('Testing rendering', () => {
        test('should render loading', () => {
            ;(useMissions as any).mockReturnValue(mockedQueryResult(undefined, true))
            render(<Missions/>);
            expect(screen.getByText('Missions en cours de chargement')).toBeInTheDocument();
        });

        test('should render error', () => {
            ;(useMissions as any).mockReturnValue(mockedQueryResult(undefined, false, new GraphQLError("Error!")))
            render(<Missions/>);
            expect(screen.getByText('Erreur: Error!')).toBeInTheDocument();
        });

        test('should render content', () => {
            ;(useMissions as any).mockReturnValue(mockedQueryResult([mission]))
            render(<Missions/>);
            expect(screen.getByText('Mes rapports de mission')).toBeInTheDocument();
        });
    });

});
