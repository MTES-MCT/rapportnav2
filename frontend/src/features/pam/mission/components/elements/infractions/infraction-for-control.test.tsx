import { render, screen } from '../../../../../../test-utils.tsx'
import { InfractionTypeEnum } from '../../../../../common/types/env-mission-types.ts'
import ControlInfraction from './infraction-for-control.tsx'
import { ControlType } from '../../../../../common/types/control-types.ts'
import { vi } from 'vitest'
import { fireEvent } from '../../../../../../test-utils.tsx'
import { Infraction } from '../../../../../common/types/infraction-types.ts'

const mutateMock = vi.fn()
const deleteMock = vi.fn()

vi.mock('./use-add-update-infraction.tsx', () => ({
  default: () => [mutateMock]
}))

vi.mock('./use-delete-infraction.tsx', () => ({
  default: () => [deleteMock]
}))

const infraction = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: undefined
}
const props = (infractions?: Infraction[]) => ({
  controlId: '1234',
  controlType: ControlType.ADMINISTRATIVE,
  infractions
})

describe('ControlInfraction', () => {
  describe('The add infraction button', () => {
    it('should be shown when no infractions (undefined) and form not open', async () => {
      render(<ControlInfraction {...props(undefined)} />)
      const button = screen.getByRole('add-infraction-button')
      expect(button).toBeInTheDocument()
    })
    it('should be shown when no infractions ([] and form not open', async () => {
      render(<ControlInfraction {...props([])} />)
      const button = screen.getByRole('add-infraction-button')
      expect(button).toBeInTheDocument()
    })
  })

  describe('The Infraction Summary', () => {
    it('should be shown when infractions exist and form not open', async () => {
      render(<ControlInfraction {...props([infraction])} />)
      const text = screen.getByText('Infraction administrative')
      expect(text).toBeInTheDocument()
    })
    it('should show the Form when clicking the edit button', async () => {
      render(<ControlInfraction {...props([infraction])} />)
      const button = screen.getByRole('edit-infraction')
      fireEvent.click(button)
      expect(screen.getByText("Ajout d'infraction")).toBeInTheDocument()
    })
    it('should call the delete mutation when clicking the delete button', async () => {
      render(<ControlInfraction {...props([infraction])} />)
      const button = screen.getByRole('delete-infraction')
      fireEvent.click(button)
      expect(deleteMock).toHaveBeenCalled()
    })
  })

  describe('The Infraction Form', () => {
    it('should handle form changes', async () => {
      render(<ControlInfraction {...props([infraction])} />)
      const button = screen.getByRole('edit-infraction')
      fireEvent.click(button)
      expect(screen.getByText("Ajout d'infraction")).toBeInTheDocument()

      const text = 'some text'
      const textarea = screen.getByRole('observations')
      fireEvent.change(textarea, { target: { value: text } })
      fireEvent.blur(textarea)
      expect(screen.getByText(text)).toBeInTheDocument()
    })
    it('should show the infraction summary when clicking the cancel button', async () => {
      render(<ControlInfraction {...props([infraction])} />)
      const button = screen.getByRole('edit-infraction')
      fireEvent.click(button)
      expect(screen.getByText("Ajout d'infraction")).toBeInTheDocument()

      const cancelButton = screen.getByRole('cancel-infraction')
      fireEvent.click(cancelButton)
      const text = screen.getByText('Infraction administrative')
      expect(text).toBeInTheDocument()
    })
    it('should call the update mutation on clicking on the validate button', async () => {
      render(<ControlInfraction {...props([infraction])} />)
      const button = screen.getByRole('edit-infraction')
      fireEvent.click(button)
      expect(screen.getByText("Ajout d'infraction")).toBeInTheDocument()

      const submitButton = screen.getByRole('validate-infraction')
      fireEvent.click(submitButton)
      expect(mutateMock).toHaveBeenCalled()
    })
  })

  // describe('The delete mutation', () => {
  //     it('should be called when changing clicking on the delete button', async () => {
  //         render(<ControlInfraction {...props}/>);
  //         const button = screen.getByRole('delete-infraction')
  //         fireEvent.click(button)
  //         expect(deleteMock).toHaveBeenCalled()
  //     });
  // });
})
