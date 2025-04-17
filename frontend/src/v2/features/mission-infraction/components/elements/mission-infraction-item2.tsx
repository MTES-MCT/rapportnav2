import { ControlType } from '@common/types/control-types.ts'

import { THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import React, { useState } from 'react'
import { Infraction } from '../../../common/types/target-types.ts'
import MissionInfractionSummary2 from '../ui/mission-infraction-summary2.tsx'
import MissionInfractionForm2 from './mission-infraction-form2.tsx'

interface MissionInfractionItem2Props {
  index: number
  name: string
  infraction: Infraction
  handleRemove: (index: number) => void
}

const MissionInfractionItem2: React.FC<MissionInfractionItem2Props> = ({ name, index, infraction, handleRemove }) => {
  const [showForm, setShowForm] = useState(false)

  return (
    <div key={`infraction-item-${index}`} style={{ marginBottom: '2em' }}>
      {showForm ? (
        <>
          <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
            <Field name={`${name}.${index}`}>
              {(field: FieldProps<Infraction>) => (
                <MissionInfractionForm2
                  fieldFormik={field}
                  name={`${name}.${index}`}
                  onClose={() => setShowForm(false)}
                />
              )}
            </Field>
          </div>
        </>
      ) : (
        <MissionInfractionSummary2
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

export default MissionInfractionItem2
