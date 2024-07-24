import { FormikEffect, FormikNumberInput } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import React, { useEffect, useState } from 'react'
import { MissionGeneralInfo } from '../../../types/mission-types'
import useAddOrUpdateGeneralInfo from './use-add-update-distance-consumption'

type RecognizedVessel = {
  nbrOfRecognizedVessel?: number
}

interface MissionRecognizedVesselProps {
  missionId: number
  generalInfo?: MissionGeneralInfo
}

const MissionRecognizedVessel: React.FC<MissionRecognizedVesselProps> = ({ missionId, generalInfo }) => {
  const [updateGeneralInfo] = useAddOrUpdateGeneralInfo()
  const [initValue, setInitValue] = useState<RecognizedVessel>()

  useEffect(() => {
    setInitValue({ nbrOfRecognizedVessel: generalInfo?.nbrOfRecognizedVessel })
  }, [generalInfo])

  const handleSubmit = async ({ nbrOfRecognizedVessel }: RecognizedVessel) => {
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
              required={true}
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
