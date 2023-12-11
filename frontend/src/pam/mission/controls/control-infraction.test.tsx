import {fireEvent, render, screen} from '../../../test-utils'
import {ControlType, Infraction} from '../../../types/control-types'
import ControlInfraction, {ControlInfractionProps} from '../infractions/infraction-for-control'
import {infractionTitleForControlType} from '../infractions/utils'
import {InfractionTypeEnum} from "../../../types/env-mission-types.ts";

const props = (infractions?: Infraction[]) =>
    ({
        controlId: '1',
        controlType: ControlType.ADMINISTRATIVE,
        infractions
    } as ControlInfractionProps)

describe('ControlInfraction', () => {
    it('should display the Add Infraction button when no Infractrion data undefined', () => {
        render(<ControlInfraction {...props(undefined)} />)
        const button = screen.getByRole('add-infraction-button')
        expect(button).toBeInTheDocument()
    })
    it('should display the Add Infraction button when no Infractrion data empty', () => {
        render(<ControlInfraction {...props([])} />)
        const button = screen.getByRole('add-infraction-button')
        expect(button).toBeInTheDocument()
    })
    it('should display the Infraction summary when there is Infractrion data', () => {
        render(<ControlInfraction {...props([{id: '123', infractions: []}])} />)
        const text = screen.getByText(infractionTitleForControlType(ControlType.ADMINISTRATIVE))
        expect(text).not.toBeUndefined()
    })
    it('should display the Infraction form when clicking the Add Infraction button', () => {
        render(<ControlInfraction {...props(undefined)} />)
        const addButton = screen.getByRole('add-infraction-button')
        fireEvent.click(addButton)
        const formSubmitButton = screen.getByRole('validate-infraction')
        expect(formSubmitButton).toBeInTheDocument()
    })
    it('should display the Infraction form with prefilled data when editing an infraction', () => {
        render(
            <ControlInfraction
                {...props([{
                    id: '123',
                    infractions: [],
                    infractionType: InfractionTypeEnum.WITH_REPORT,
                    observations: 'observations dummy'
                }])}
            />
        )
        const editButton = screen.getByRole('edit-infraction')
        fireEvent.click(editButton)

        // check prefilled data
        const observations = screen.getByText('observations dummy')
        expect(observations).toBeInTheDocument()
        // const pvEmis = screen.getByRole('toggle-formal-notice')
        // expect(pvEmis).toBeChecked()

        const formSubmitButton = screen.getByRole('validate-infraction')
        expect(formSubmitButton).toBeInTheDocument()
    })
})
