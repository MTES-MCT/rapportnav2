import { render, screen, fireEvent } from '../../../test-utils'
import FishInfractionSummary from "./fish-infraction-summary.tsx";
import { ControlType } from "../../../types/control-types.ts";
import { InfractionTypeEnum } from "../../../types/env-mission-types.ts";
import {
    GearInfraction, InfractionType,
    LogbookInfraction,
    OtherInfraction,
    SpeciesInfraction
} from "../../../types/fish-mission-types.ts";

const infraction = {
    comments: 'some comments',
    infractionType: InfractionType.WITH_RECORD,
    natinf: 123
}

const props = (infractions?: LogbookInfraction[] | GearInfraction[] | SpeciesInfraction[] | OtherInfraction[]) => ({
    title: "title",
    infractions
})
describe('FishInfractionSummary', () => {

    test('renders Aucune infraction when no infraction (undefined)', async () => {
        render(<FishInfractionSummary {...props(undefined)}/>);
        expect(screen.getByText('Aucune infraction')).toBeInTheDocument()
    });
    test('renders Aucune infraction when no infraction ([])', async () => {
        render(<FishInfractionSummary {...props([])}/>);
        expect(screen.getByText('Aucune infraction')).toBeInTheDocument()
    });
    test('renders the summary when there are infractions', async () => {
        render(<FishInfractionSummary {...props([infraction])}/>);
        expect(screen.getByText('some comments')).toBeInTheDocument()
    });
    test('renders Aucune observation when no comments', async () => {
        render(<FishInfractionSummary {...props([{...infraction, comments: undefined}])}/>);
        expect(screen.getByText('Aucune observation')).toBeInTheDocument()
    });
    test('renders natinfs tags', async () => {
        render(<FishInfractionSummary {...props([infraction])}/>);
        expect(screen.getByText('NATINF : 123')).toBeInTheDocument()
    });
    test('renders no tags when none', async () => {
        render(<FishInfractionSummary {...props([{...infraction, natinf: undefined}])}/>);
        expect(screen.getByText('Sans infraction')).toBeInTheDocument()
    });

});
