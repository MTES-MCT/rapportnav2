import { ControlType } from '@common/types/control-types.ts'

import { Infraction } from '@common/types/infraction-types'
import { THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import React, { useState } from 'react'
import MissionInfractionSummary from '../ui/mission-infraction-summary.tsx'
import MissionInfractionForm from './mission-infraction-form.tsx'

interface MissionInfractionItemProps {
  index: number
  name: string
  infraction: Infraction
  handleRemove: (index: number) => void
}

const MissionInfractionItem: React.FC<MissionInfractionItemProps> = ({ name, index, infraction, handleRemove }) => {
  const [showForm, setShowForm] = useState(false)

  return (
    <div key={`infraction-item-${index}`} style={{ marginBottom: '2em' }}>
      {showForm ? (
        <>
          <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
            <Field name={`${name}.${index}`}>
              {(field: FieldProps<Infraction>) => (
                <MissionInfractionForm
                  fieldFormik={field}
                  name={`${name}.${index}`}
                  onClose={() => setShowForm(false)}
                />
              )}
            </Field>
          </div>
        </>
      ) : (
        <MissionInfractionSummary
          isNavAction={true}
          infractions={[infraction]}
          onEdit={() => setShowForm(true)}
          onDelete={() => handleRemove(index)}
          controlType={ControlType.NAVIGATION}
        />
      )}
    </div>
  )
}

export default MissionInfractionItem
