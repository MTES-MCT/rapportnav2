import { render, screen, fireEvent } from '../../../test-utils'
import { InfractionTypeEnum } from "../../../types/env-mission-types.ts";
import EnvInfractionAddNewTarget from "./env-infraction-add-new-target.tsx";
import { vi } from 'vitest';
import { Infraction } from "../../../types/infraction-types.ts";
import { ControlType } from "../../../types/control-types.ts";

const mutateMock = vi.fn()

vi.mock("./use-add-update-infraction-env.tsx", () => ({
    default: () => [mutateMock, {error: undefined}],
}));

// const infractionMock = {
//     id: '123',
//     controlType: ControlType.ADMINISTRATIVE,
//     infractionType: InfractionTypeEnum.WITHOUT_REPORT,
//     natinfs: ['123'],
//     observations: undefined,
//     target: undefined
// }
// const infractionMockEnv = {
//     id: '456',
//     controlType: null,
//     infractionType: InfractionTypeEnum.WITHOUT_REPORT,
//     natinfs: ['123'],
//     observations: undefined,
//     target: undefined
// }

const props = (infractions?: Infraction[]) => ({
    availableControlTypesForInfraction: [ControlType.ADMINISTRATIVE],
    infractions
})

describe('EnvInfractionAddNewTarget', () => {

    describe('testing rendering', () => {
        it('should show the add button by default', async () => {
            render(<EnvInfractionAddNewTarget {...props(undefined)}/>);
            expect(screen.getByText('Ajouter une nouvelle cible avec infraction')).toBeInTheDocument()
        });
        it('should show the form when clicking the button', async () => {
            render(<EnvInfractionAddNewTarget {...props(undefined)}/>);
            const button = screen.getByRole('add-target')
            fireEvent.click(button)
            expect(screen.getByTestId('new-target-form')).toBeInTheDocument()
        });
        it('should show the summary when clicking on cancel on the form (nav)', async () => {
            render(<EnvInfractionAddNewTarget {...props(undefined)}/>);
            expect(screen.queryByTestId('new-target-form')).not.toBeInTheDocument()
            const button = screen.getByRole('add-target')
            fireEvent.click(button)
            expect(screen.getByTestId('new-target-form')).toBeInTheDocument()
            const cancel = screen.getByRole('cancel-infraction')
            fireEvent.click(cancel)
            expect(screen.queryByTestId('new-target-form')).not.toBeInTheDocument()
        });
    });

    describe('The Infraction Form', () => {
        it('should handle form changes', async () => {
            render(<EnvInfractionAddNewTarget {...props(undefined)}/>);
            const button = screen.getByRole('add-target')
            fireEvent.click(button)

            const field = screen.getByRole('identityControlledPerson')
            const value = 'test'
            fireEvent.change(field, {target: {value}});
            fireEvent.blur(field)

            const obsField = screen.getByRole('observations')
            const observations = 'some observations'
            fireEvent.change(obsField, {target: {value: observations}});
            fireEvent.blur(obsField)
        });
    });

    describe('The update mutation', () => {
        it('should be called when changing clicking on the delete button', async () => {
            render(<EnvInfractionAddNewTarget {...props(undefined)}/>);
            const button = screen.getByRole('add-target')
            fireEvent.click(button)
            const submit = screen.getByRole('validate-infraction')
            fireEvent.click(submit)
            expect(mutateMock).toHaveBeenCalled()
        });
    });


});
