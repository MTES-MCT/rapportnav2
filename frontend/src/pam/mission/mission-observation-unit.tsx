import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import React, { useEffect, useState } from 'react'
import useUpdateMissionEnv from './use-update-mission-env'

type ObservationByUnit = {
  observation?: string
}

interface MissionObservationByUnitProps {
  missionId: number
  observationsByUnit?: string
}

const MissionObservationUnit: React.FC<MissionObservationByUnitProps> = ({ missionId, observationsByUnit }) => {
  const [updateMissionObservation] = useUpdateMissionEnv()
  const [initValue, setInitValue] = useState<ObservationByUnit>()

  useEffect(() => {
    setInitValue({ observation: observationsByUnit })
  }, [observationsByUnit])

  const handleSubmit = ({ observation }: ObservationByUnit) => {
    if (!observation || observation.length < 4 || observation === observationsByUnit) return
    updateMissionObservation({
      variables: {
        mission: {
          missionId,
          observationsByUnit: observation
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
              name="observation"
              data-testid="mission-general-observation"
              label="Observation générale à l'échelle de la mission (remarques, résumé)"
            />
          </>
        </Formik>
      )}
    </>
  )
}

export default MissionObservationUnit
