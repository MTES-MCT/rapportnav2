import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionEnvActionData } from '../../common/types/mission-action'
import { ActionEnvControlInput } from '../types/action-type'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'

export function useMissionActionEnvControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionEnvControlInput> {
  const value = action?.data as MissionEnvActionData
  const { extractLatLngFromMultiPoint } = useCoordinate()
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionEnvActionData): ActionEnvControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      geoCoords: extractLatLngFromMultiPoint(data.geom)
    }
  }

  const fromInputToFieldValue = (value: ActionEnvControlInput): MissionEnvActionData => {
    const { dates, geoCoords, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, endDateTimeUtc, startDateTimeUtc }
  }

  const { initValue, handleSubmit } = useAbstractFormik<MissionEnvActionData, ActionEnvControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    ['incidentDuringOperation', 'hasDivingDuringOperation']
  )

  const onSubmit = async (valueToSubmit?: MissionEnvActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionEnvControlInput) => {
    handleSubmit(value, onSubmit)
  }

  return {
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
