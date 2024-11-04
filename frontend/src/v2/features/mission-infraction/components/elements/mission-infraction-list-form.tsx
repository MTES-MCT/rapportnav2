import { ControlType } from '@common/types/control-types'
import { Infraction } from '@common/types/infraction-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldArrayRenderProps, FieldProps } from 'formik'
import { useEffect, useState } from 'react'
import MissionInfractionSummary from '../ui/mission-infraction-summary'
import MissionInfractionForm from './mission-infraction-form'
import MissionInfractionFormNew from './mission-infraction-form-new'

const DEFAULT_MAX = 1
export interface MissionInfractionListProps {
  name: string
  max?: number
  controlType: ControlType
  fieldArray: FieldArrayRenderProps
}

const MissionInfractionList: React.FC<MissionInfractionListProps> = ({ max, name, controlType, fieldArray }) => {
  const [showForms, setShowForms] = useState<boolean[]>([])

  const handleShowForm = (index: number, value: boolean) => {
    const booleans = [...showForms]
    booleans[index] = value
    setShowForms(booleans)
  }

  const handleRemove = (index: number) => {
    const booleans = [...showForms]
    booleans.splice(index)
    setShowForms(booleans)
    fieldArray.remove(index)
  }

  const handleAdd = (infraction: Infraction) => {
    fieldArray.push(infraction)
    setShowForms([...showForms, false])
  }

  useEffect(() => {
    if (!fieldArray) return
    if (showForms.length !== 0) return
    setShowForms(fieldArray.form.values.infractions?.map(() => false))
  }, [fieldArray])

  return (
    <div>
      {fieldArray.form.values.infractions?.map((infraction: Infraction, index: number) => (
        <div key={index} style={{ marginBottom: '2em' }}>
          {showForms[index] ? (
            <>
              <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
                <Field name={`${name}.${index}`}>
                  {(field: FieldProps<Infraction>) => (
                    <MissionInfractionForm
                      fieldFormik={field}
                      name={`${name}.${index}`}
                      onClose={() => handleShowForm(index, false)}
                    />
                  )}
                </Field>
              </div>
            </>
          ) : (
            <MissionInfractionSummary
              infractions={[infraction]}
              controlType={ControlType.NAVIGATION}
              onDelete={() => handleRemove(index)}
              onEdit={() => handleShowForm(index, true)}
            />
          )}
        </div>
      ))}
      <MissionInfractionFormNew
        onSubmit={handleAdd}
        controlType={controlType}
        isDisabled={fieldArray.form.values.infractions.length >= (max ?? DEFAULT_MAX)}
      />
    </div>
  )
}

export default MissionInfractionList
