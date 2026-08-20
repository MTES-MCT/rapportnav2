import { Accent, Button } from '@mtes-mct/monitor-ui'

type BasicDialogActionProps = {
  isValid: boolean
  accent?: Accent
  validateButtonLabel?: string
  onSubmit: (response: boolean) => void
}

const BasicDialogAction: React.FC<BasicDialogActionProps> = ({ isValid, accent, validateButtonLabel, onSubmit }) => {
  return (
    <>
      <Button data-testid="dialog-form-cancel-button" accent={Accent.SECONDARY} onClick={() => onSubmit(false)}>
        Annuler
      </Button>
      <Button
        disabled={!isValid}
        onClick={() => onSubmit(true)}
        accent={accent ?? Accent.PRIMARY}
        data-testid="dialog-form-confirm-button"
        className={[Accent.ERROR, Accent.WARNING].includes(accent ?? Accent.PRIMARY) ? '_active' : ''}
      >
        {validateButtonLabel ?? 'Confirmer'}
      </Button>
    </>
  )
}

export default BasicDialogAction
