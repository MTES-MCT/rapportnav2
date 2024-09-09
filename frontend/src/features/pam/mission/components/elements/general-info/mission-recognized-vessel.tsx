import { MissionGeneralInfo } from '@common/types/mission-types.ts'
import { FormikEffect, FormikNumberInput } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import React, { useEffect, useRef, useState } from 'react'
import useAddOrUpdateGeneralInfo from '../../../hooks/use-add-update-distance-consumption.tsx'

const DEBOUNCE_TIME_TRIGGER = 5000

type RecognizedVessel = {
  nbrOfRecognizedVessel?: number
}

interface MissionRecognizedVesselProps {
  missionId: number
  generalInfo?: MissionGeneralInfo
}

const MissionRecognizedVessel: React.FC<MissionRecognizedVesselProps> = ({ missionId, generalInfo }) => {
  const [updateGeneralInfo] = useAddOrUpdateGeneralInfo()
  const timerRef = useRef<ReturnType<typeof setTimeout>>()
  const [initValue, setInitValue] = useState<RecognizedVessel>()

  useEffect(() => {
    setInitValue({ nbrOfRecognizedVessel: generalInfo?.nbrOfRecognizedVessel })
  }, [generalInfo])

  const handleSubmit = async ({ nbrOfRecognizedVessel }: RecognizedVessel): Promise<void> => {
    clearTimeout(timerRef.current)
    timerRef.current = setTimeout(() => updateRecognizedVessel(nbrOfRecognizedVessel), DEBOUNCE_TIME_TRIGGER)
  }

  const updateRecognizedVessel = async (nbrOfRecognizedVessel?: number) => {
    if (nbrOfRecognizedVessel === undefined || nbrOfRecognizedVessel === generalInfo?.nbrOfRecognizedVessel) return
    const info = { ...generalInfo, missionId, nbrOfRecognizedVessel }
    await updateGeneralInfo({
      variables: { info }
    })
  }

  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true}>
          <>
            <FormikEffect onChange={handleSubmit} />
            <FormikNumberInput
              isLight={false}
              isRequired={true}
              name="nbrOfRecognizedVessel"
              data-testid="mission-information-general-recognized-vessel"
              label="Nombre total de navires reconnus dans les approches maritimes (ZEE)"
            />
          </>
        </Formik>
      )}
    </>
  )
}

export default MissionRecognizedVessel
