import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import React, { useEffect, useRef, useState } from 'react'
import useIsMissionFinished from '@features/pam/mission/hooks/use-is-mission-finished.tsx'
import usePatchMissionEnv from '@features/pam/mission/hooks/use-patch-mission-env.tsx'

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
  const isMissionFinished = useIsMissionFinished(missionId?.toString())

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

  const validateError = (isMissionFinished?: boolean, observations?: string) =>
    isMissionFinished && !observations
      ? { observations: "L'observation générale de la mission est requise" }
      : undefined

  return (
    <>
      {initValue && (
        <Formik
          initialValues={initValue}
          initialErrors={validateError(isMissionFinished, initValue.observations)}
          onSubmit={handleSubmit}
          validateOnChange={true}
          validate={values => validateError(isMissionFinished, values.observations)}
        >
          <>
            <FormikEffect onChange={handleSubmit} />
            <FormikTextarea
              isRequired={true}
              isLight={false}
              name="observations"
              data-testid="mission-general-observation"
              label="Observation générale à l'échelle de la mission (remarques, résumé)"
              isErrorMessageHidden={true}
            />
          </>
        </Formik>
      )}
    </>
  )
}

export default MissionObservationsUnit
