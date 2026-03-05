import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionEnvActionData } from '../../common/types/mission-action'
import { ActionEnvControlInput } from '../types/action-type'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { object, string } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { useMemo } from 'react'

export function useMissionActionEnvControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionEnvControlInput> {
  const value = action?.data as MissionEnvActionData
  const { extractLatLngFromMultiPoint } = useCoordinate()
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const missionDates = useMissionDates(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionEnvActionData): ActionEnvControlInput => {
    const dates = getDateRangeForInput(data)
    return {
      ...data,
      dates,
      geoCoords: extractLatLngFromMultiPoint(data.geom)
    }
  }

  const fromInputToFieldValue = (value: ActionEnvControlInput): MissionEnvActionData => {
    const { dates, geoCoords, ...newData } = value
    return { ...newData, ...getDateRangeFromInput(dates) }
  }

  const { initValue, handleSubmit } = useAbstractFormik<MissionEnvActionData, ActionEnvControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionEnvActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionEnvControlInput) => {
    handleSubmit(value, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      ...getDateRangeSchema({ isMissionFinished, missionStartDate, missionEndDate }),
      observationsByUnit: string().nullable()
    })
  }

  const validationSchema = useMemo(
    () => createValidationSchema(isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc),
    [isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc]
  )

  return {
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
