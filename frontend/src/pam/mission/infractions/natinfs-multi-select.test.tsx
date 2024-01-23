import { render, screen } from '../../../test-utils'
import NatinfsMultiSelect from "./natinfs-multi-select.tsx";
import { vi } from 'vitest';
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


describe('NatinfsMultiSelect', () => {

    describe('Testing rendering according to Query result', () => {
        test('renders loading state', async () => {
            ;(useNatinfs as any).mockReturnValue(mockedQueryResult(mock as any, true))
            const {container} = render(<NatinfsMultiSelect selectedNatinfs={[]} onChange={vi.fn()}/>);
            expect(container.firstChild).toBeNull();
        });

        test('renders error state', async () => {
            ;(useNatinfs as any).mockReturnValue(mockedQueryResult(mock as any, false, new GraphQLError("Error!")))
            const {container} = render(<NatinfsMultiSelect selectedNatinfs={[]} onChange={vi.fn()}/>);
            expect(container.firstChild).toBeNull();
        });

        test('renders data', async () => {
            ;(useNatinfs as any).mockReturnValue(mockedQueryResult(mock as any, false))
            render(<NatinfsMultiSelect selectedNatinfs={[]} onChange={vi.fn()}/>);
            expect(screen.getByText('NATINF')).toBeInTheDocument()
        });
    })

});
