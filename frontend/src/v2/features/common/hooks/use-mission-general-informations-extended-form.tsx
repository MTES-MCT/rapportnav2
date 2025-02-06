import { FieldProps } from 'formik'
import { MissionGeneralInfoExtended } from '../types/mission-types.ts'
import { useAbstractFormikSubForm } from './use-abstract-formik-sub-form.tsx'

export function useMissionGeneralInformationsExtendedForm(
  name: string,
  fieldFormik: FieldProps<MissionGeneralInfoExtended>
) {

  const fromFieldValueToInput = (data: MissionGeneralInfoExtended) => {

    return {
      ...data,
      envData: {
        controlUnits: [{resources: data.resources}],
        observationsByUnit: data.observations
      }
    }
  }

  const fromInputToFieldValue = (value: MissionGeneralInfoExtended): MissionGeneralInfoExtended => {
    return value
  }

  const { initValue, handleSubmit } = useAbstractFormikSubForm<
    MissionGeneralInfoExtended,
    MissionGeneralInfoExtended
  >(name, fieldFormik, fromFieldValueToInput, fromInputToFieldValue)

  return {
    initValue,
    handleSubmit
  }
}
