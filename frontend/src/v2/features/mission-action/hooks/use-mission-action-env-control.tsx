import { ControlType } from '@common/types/control-types'
import { FormikErrors } from 'formik'
import { uniq } from 'lodash'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionEnvActionData } from '../../common/types/mission-action'
import { ActionEnvControlInput } from '../types/action-type'

export function useMissionActionEnvControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionEnvControlInput> & {
  getAvailableControlTypes: (value: ActionEnvControlInput) => ControlType[]
  getAvailableControlTypes2: (value: ActionEnvControlInput, actionNumberOfControls?: number) => ControlType[]
} {
  const value = action?.data as MissionEnvActionData
  const { extractLatLngFromMultiPoint } = useCoordinate()
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionEnvActionData): ActionEnvControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: extractLatLngFromMultiPoint(data.geom)
    }
  }

  const fromInputToFieldValue = (value: ActionEnvControlInput): MissionEnvActionData => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, endDateTimeUtc, startDateTimeUtc }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionEnvActionData, ActionEnvControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionEnvActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionEnvControlInput, errors?: FormikErrors<ActionEnvControlInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  const getAvailableControlTypes = (value?: ActionEnvControlInput) => {
    const controls: ControlType[] = []
    if (value?.controlSecurity?.amountOfControls) controls.push(ControlType.SECURITY)
    if (value?.controlGensDeMer?.amountOfControls) controls.push(ControlType.GENS_DE_MER)
    if (value?.controlNavigation?.amountOfControls) controls.push(ControlType.NAVIGATION)
    if (value?.controlAdministrative?.amountOfControls) controls.push(ControlType.ADMINISTRATIVE)
    return value?.availableControlTypesForInfraction?.filter(c => controls.includes(c)) ?? []
  }

  const getAvailableControlTypes2 = (value?: ActionEnvControlInput, actionNumberOfControls?: number): ControlType[] => {
    const controlTypes = uniq(
      value?.targets
        ?.flatMap(target => target.controls)
        ?.filter(control => control?.amountOfControls)
        ?.map(control => control?.controlType)
    )

    return value?.availableControlTypesForInfraction?.filter(c => controlTypes.includes(c)) ?? []
  }

  return {
    errors,
    initValue,
    getAvailableControlTypes,
    getAvailableControlTypes2,
    handleSubmit: handleSubmitOverride
  }
}
