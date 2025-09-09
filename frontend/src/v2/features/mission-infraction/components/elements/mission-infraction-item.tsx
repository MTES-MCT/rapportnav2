import { ControlType } from '@common/types/control-types.ts'

import { THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import React, { useState } from 'react'
import { Infraction, TargetInfraction } from '../../../common/types/target-types.ts'
import MissionInfractionForm from './mission-infraction-form.tsx'
import MissionInfractionNavSummary from './mission-infraction-nav-summary.tsx'

interface MissionInfractionItemProps {
  index: number
  name: string
  fieldFormik: FieldProps<Infraction>
  handleRemove: (index: number) => void
}

const MissionInfractionItem: React.FC<MissionInfractionItemProps> = ({ name, index, fieldFormik, handleRemove }) => {
  const [showForm, setShowForm] = useState(false)

  const handleSubmit = async (value?: TargetInfraction) => {
    if (!value?.infraction) return
    setShowForm(false)
    await fieldFormik.form.setFieldValue(name, value.infraction)
  }

  return (
    <div key={`infraction-item-${index}`} style={{ marginBottom: '2em' }}>
      {showForm ? (
        <>
          <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
            <MissionInfractionForm
              editTarget={false}
              editControl={false}
              editInfraction={true}
              onSubmit={handleSubmit}
              onClose={() => setShowForm(false)}
              value={{ infraction: fieldFormik.field.value } as TargetInfraction}
            />
          </div>
        </>
      ) : (
        <MissionInfractionNavSummary
          onEdit={() => setShowForm(true)}
          infraction={fieldFormik.field.value}
          onDelete={() => handleRemove(index)}
          controlType={ControlType.NAVIGATION}
        />
      )}
    </div>
  )
}

export default MissionInfractionItem

/**
 * 
 * /* <Field name={`${name}.${index}`}>
              {(field: FieldProps<Infraction>) => (
                <MissionInfractionForm
                  fieldFormik={field}
                  name={`${name}.${index}`}
                  onClose={() => setShowForm(false)}
                />
              )}
            </Field>*/
