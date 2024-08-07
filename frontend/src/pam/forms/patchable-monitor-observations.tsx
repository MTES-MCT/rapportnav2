import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import React, { useEffect, useState } from 'react'

export type ObservationsByUnit = {
  observations?: string
}

interface PatchableMonitorObservationsProps {
  observationsByUnit?: string
  label: string
  onSubmit: any
  isLight: boolean
}

const PatchableMonitorObservations: React.FC<PatchableMonitorObservationsProps> = ({
  observationsByUnit,
  onSubmit,
  label,
  isLight
}) => {
  const [initValue, setInitValue] = useState<ObservationsByUnit>()

  useEffect(() => {
    setInitValue({ observations: observationsByUnit })
  }, [observationsByUnit])

  const handleSubmit = async ({ observations }: ObservationsByUnit) => {
    if (!observations || observations.length < 4 || observations === observationsByUnit) return

    await onSubmit(observations)
  }

  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true}>
          <>
            <FormikEffect onChange={handleSubmit} />
            <FormikTextarea isLight={isLight} name="observations" data-testid="observations-by-unit" label={label} />
          </>
        </Formik>
      )}
    </>
  )
}

export default PatchableMonitorObservations
