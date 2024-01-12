import { render, screen } from '../../../test-utils'
import NatinfsFullNameList from "./natinfs-fullname-list.tsx";
import useNatinfs from "./use-natinfs.tsx";
import { GraphQLError } from "graphql/error";
import { Natinf } from "../../../types/infraction-types.ts";

vi.mock("././use-natinfs.tsx", async (importOriginal) => {
    const actual = await importOriginal()
    return {
        ...actual,
        default: vi.fn()
    }
})

const mock = [{
    infraction: 'text1',
    natinfCode: '1'
}]

const mockedQueryResult = (action: Natinf[] = mock as any, loading: boolean = false, error: any = undefined) => ({
    data: action,
    loading,
    error,
})


describe('NatinfsFullNameList', () => {

    describe('Testing rendering according to Query result', () => {
        test('renders null when no input natinf', async () => {
            ;(useNatinfs as any).mockReturnValue(mockedQueryResult(mock as any, true))
            const {container} = render(<NatinfsFullNameList natinfs={undefined}/>);
            expect(container.firstChild).toBeNull();
        });
        test('renders loading state', async () => {
            ;(useNatinfs as any).mockReturnValue(mockedQueryResult(mock as any, true))
            const {container} = render(<NatinfsFullNameList natinfs={[]}/>);
            expect(container.firstChild).toBeNull();
        });
        test('renders error state', async () => {
            ;(useNatinfs as any).mockReturnValue(mockedQueryResult(mock as any, false, new GraphQLError("Error!")))
            const {container} = render(<NatinfsFullNameList natinfs={[]}/>);
            expect(container.firstChild).toBeNull();
        });
        test('renders empty text when empty natinfs', async () => {
            ;(useNatinfs as any).mockReturnValue(mockedQueryResult(mock as any, false))
            render(<NatinfsFullNameList natinfs={[]}/>);
            expect(screen.getByText('--')).toBeInTheDocument()
        });
        test('renders empty text when the input natinf does not match', async () => {
            ;(useNatinfs as any).mockReturnValue(mockedQueryResult(mock as any, false))
            render(<NatinfsFullNameList natinfs={['2']}/>);
            expect(screen.getByText('--')).toBeInTheDocument()
        });
        test('renders the full natinf name when the input natinf matches', async () => {
            ;(useNatinfs as any).mockReturnValue(mockedQueryResult(mock as any, false))
            render(<NatinfsFullNameList natinfs={['1']}/>);
            expect(screen.getByText('1 - text1')).toBeInTheDocument()
        });
    })

});
