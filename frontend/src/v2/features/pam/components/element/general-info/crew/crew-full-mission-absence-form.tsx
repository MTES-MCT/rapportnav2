import React from 'react'
import { useFormikContext, getIn } from 'formik'
import { ABSENCE_REASON_OPTIONS } from '../../../../../common/types/crew-type'
import { MissionGeneralInfo2 } from '../../../../../common/types/mission-types'
import { Button, FormikSelect } from '@mtes-mct/monitor-ui'

interface Props {
  crewIndex: number
}

export const FullMissionAbsenceForm: React.FC<Props> = ({ crewIndex }) => {
  const { values, setFieldValue, errors } = useFormikContext<MissionGeneralInfo2>()

  const reasonPath = `crew.${crewIndex}.absences.0.reason`
  const absencesPath = `crew.${crewIndex}.absences`

  const reason = getIn(values, reasonPath)
  const error = getIn(errors, reasonPath)
  debugger

  const handleSubmit = () => {
    setFieldValue(absencesPath, [
      {
        isAbsentFullMission: true,
        reason
      }
    ])
  }

  return (
    <>
      <FormikSelect name={reasonPath} options={ABSENCE_REASON_OPTIONS} label="Motif" isRequired isLight />

      <Button type="button" disabled={!reason || !!error} onClick={handleSubmit} style={{ marginTop: 16 }}>
        Valider l’absence sur toute la mission
      </Button>
    </>
  )
}
