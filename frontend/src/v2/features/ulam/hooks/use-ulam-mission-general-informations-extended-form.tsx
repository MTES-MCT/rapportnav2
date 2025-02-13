import { FieldProps } from 'formik'
import { useAbstractFormikSubForm } from '../../common/hooks/use-abstract-formik-sub-form.tsx'
import { MissionGeneralInfoExtended } from '../../common/types/mission-types.ts'

export function useUlamMissionGeneralInformationsExtendedForm(
  name: string,
  fieldFormik: FieldProps<MissionGeneralInfoExtended>
) {
  const fromFieldValueToInput = (data: MissionGeneralInfoExtended) => data
  const fromInputToFieldValue = (value: MissionGeneralInfoExtended): MissionGeneralInfoExtended => value
  const { initValue, handleSubmit } = useAbstractFormikSubForm<MissionGeneralInfoExtended, MissionGeneralInfoExtended>(
    name,
    fieldFormik,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  return {
    initValue,
    handleSubmit
  }
}
