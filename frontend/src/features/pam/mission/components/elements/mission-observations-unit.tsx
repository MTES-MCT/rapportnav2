import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import React, { useEffect, useRef, useState } from 'react'
import usePatchMissionEnv from '../../hooks/use-patch-mission-env.tsx'

const DEBOUNCE_TIME_TRIGGER = 5000

type ObservationsByUnit = {
  observations?: string
}

interface MissionObservationsByUnitProps {
  missionId: number
  observationsByUnit?: string
}

const MissionObservationsUnit: React.FC<MissionObservationsByUnitProps> = ({ missionId, observationsByUnit }) => {
  const [patchMissionObservation] = usePatchMissionEnv()
  const timerRef = useRef<ReturnType<typeof setTimeout>>()
  const [initValue, setInitValue] = useState<ObservationsByUnit>()

  useEffect(() => {
    setInitValue({ observations: observationsByUnit })
  }, [observationsByUnit])

  const handleSubmit = async ({ observations }: ObservationsByUnit): Promise<void> => {
    clearTimeout(timerRef.current)
    timerRef.current = setTimeout(() => patchObservationUnit(observations), DEBOUNCE_TIME_TRIGGER)
  }

  const patchObservationUnit = async (observations?: string) => {
    if (observations === observationsByUnit) return
    await patchMissionObservation({
      variables: {
        mission: {
          missionId,
          observationsByUnit: observations ?? ''
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
              isRequired={true}
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
