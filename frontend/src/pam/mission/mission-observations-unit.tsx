import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import React, { useEffect, useState } from 'react'
import usePatchMissionEnv from './use-patch-mission-env.tsx'

type ObservationsByUnit = {
  observations?: string
}

interface MissionObservationsByUnitProps {
  missionId: number
  observationsByUnit?: string
}

const MissionObservationsUnit: React.FC<MissionObservationsByUnitProps> = ({ missionId, observationsByUnit }) => {
  const [patchMissionObservation] = usePatchMissionEnv()
  const [initValue, setInitValue] = useState<ObservationsByUnit>()

  useEffect(() => {
    setInitValue({ observations: observationsByUnit })
  }, [observationsByUnit])

  const handleSubmit = async ({ observations }: ObservationsByUnit) => {
    if (!observations || observations.length < 4 || observations === observationsByUnit) return

    await patchMissionObservation({
      variables: {
        mission: {
          missionId,
          observationsByUnit: observations
        }
      }
    })
  }

  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true}>
          <>
            <FormikEffect onChange={handleSubmit} />
            <FormikTextarea
              isLight={false}
              name="observations"
              data-testid="mission-general-observation"
              label="Observation générale à l'échelle de la mission (remarques, résumé)"
            />
          </>
        </Formik>
      )}
    </>
  )
}

export default MissionObservationsUnit
