import { ControlType } from '@common/types/control-types'
import { Infraction } from '@common/types/infraction-types'
import { Accent, Button, FormikEffect, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useState } from 'react'
import { useInfraction } from '../../hooks/use-infraction'
import MissionInfractionForm from './mission-infraction-form'

type NewInfraction = { newInfraction: Infraction }

export interface MissionInfractionFormNewProps {
  isDisabled: boolean
  controlType: ControlType
  onSubmit: (infraction: Infraction) => void
}

const MissionInfractionFormNew: FC<MissionInfractionFormNewProps> = ({ isDisabled, onSubmit, controlType }) => {
  const { getInfractionByControlTypeButton } = useInfraction()
  const [shownewForm, setShowNewForm] = useState<boolean>(false)

  const newInfraction = { newInfraction: { controlType } } as NewInfraction
  const handleSubmit = (value: { newInfraction: Infraction }) => {
    if (isEqual(newInfraction, value)) return
    onSubmit(value.newInfraction)
  }
  return (
    <>
      {shownewForm && (
        <>
          <Label>Ajout d'infraction</Label>
          <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
            <Formik initialValues={newInfraction} onSubmit={handleSubmit}>
              <>
                <FormikEffect onChange={newValues => handleSubmit(newValues as NewInfraction)} />
                <Field name={`newInfraction`}>
                  {(field: FieldProps<Infraction>) => (
                    <MissionInfractionForm
                      fieldFormik={field}
                      name={`newInfraction`}
                      onClose={() => setShowNewForm(false)}
                    />
                  )}
                </Field>
              </>
            </Formik>
          </div>
        </>
      )}
      {!shownewForm && (
        <Button
          Icon={Icon.Plus}
          isFullWidth={true}
          size={Size.NORMAL}
          accent={Accent.SECONDARY}
          role="add-infraction-button"
          onClick={() => setShowNewForm(true)}
          disabled={isDisabled}
        >
          {`${getInfractionByControlTypeButton(controlType)}`}
        </Button>
      )}
    </>
  )
}

export default MissionInfractionFormNew
