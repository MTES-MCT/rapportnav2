import { FC } from 'react'
import { FieldProps } from 'formik'
import { MissionGeneralInfoFinal } from '../../../common/types/mission-types.ts'
import { FormikCheckbox, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import MissionCrewUlam from './mission-crew-ulam.tsx'

export interface MissionGeneralInformationFinalFormProps {
  name: string,
  fieldFormik: FieldProps<MissionGeneralInfoFinal>
}

const MissionGeneralInformationFinalForm: FC<MissionGeneralInformationFinalFormProps> = ({name, fieldFormik}) => {
  return (
    <>
      <Stack.Item style={{ width: '100%', marginBottom: '1.5rem' }}>
        <MissionCrewUlam />
      </Stack.Item>

      <Stack.Item style={{ width: '100%', marginBottom: '1rem' }}>
        <FormikCheckbox
          name={"isMissionArmed"}
          label={"Mission armée"}
        />
      </Stack.Item>

      <Stack.Item style={{ width: '100%', marginBottom: '1.5rem' }}>
        <FormikCheckbox
          name={"isJointMission"}
          label={"Mission conjointe (avec un autre service)"}
        />
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <FormikTextarea
          name={"observations"}
          label={"Observation générale à l'échelle de la mission (remarques, résumé)"}
        />

      </Stack.Item>
    </>
  )
}

export default MissionGeneralInformationFinalForm
