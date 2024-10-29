import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { Infraction, InfractionByTarget, InfractionTarget } from '@common/types/infraction-types'
import { FormikEffect, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useState } from 'react'
import MissionInfractionAddButton from '../ui/mission-infraction-add-button'
import MissionInfractionEnvForm from './mission-infraction-env-form'

const natinfs: string[] = []
const infractions = [{ natinfs, target: {} as InfractionTarget }] as Infraction[]
const infractionByTarget = { infractions } as InfractionByTarget
const newInfractionByTarget = { newInfraction: infractionByTarget }

type NewInfraction = { newInfraction: InfractionByTarget }

export interface MissionInfractionEnvFormNewProps {
  isDisabled: boolean
  availableControlTypes?: ControlType[]
  actionTargetType?: ActionTargetTypeEnum
  onSubmit: (infraction: InfractionByTarget) => void
}

const MissionInfractionEnvFormNew: FC<MissionInfractionEnvFormNewProps> = ({
  onSubmit,
  isDisabled,
  actionTargetType,
  availableControlTypes
}) => {
  const [showForm, setShowForm] = useState<boolean>(false)
  const handleSubmit = (value: NewInfraction) => {
    if (isEqual(newInfractionByTarget, value)) return
    onSubmit(value.newInfraction)
  }
  return (
    <>
      {showForm && (
        <div
          style={{
            width: '100%',
            padding: '1rem',
            marginBottom: '1rem',
            backgroundColor: THEME.color.white
          }}
        >
          <Formik initialValues={newInfractionByTarget} onSubmit={handleSubmit}>
            <>
              <FormikEffect onChange={newValues => handleSubmit(newValues as NewInfraction)} />
              <Field name={`newInfraction`}>
                {(field: FieldProps<InfractionByTarget>) => (
                  <MissionInfractionEnvForm
                    fieldFormik={field}
                    name={`newInfraction`}
                    onClose={() => setShowForm(false)}
                    actionTargetType={actionTargetType}
                    availableControlTypes={availableControlTypes}
                  />
                )}
              </Field>
            </>
          </Formik>
        </div>
      )}
      {!showForm && <MissionInfractionAddButton onClick={() => setShowForm(true)} disabled={isDisabled} />}
    </>
  )
}

export default MissionInfractionEnvFormNew
