import { useCallback, useMemo } from 'react'
import { object, string } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionEnvActionData } from '../../common/types/mission-action'
import { ActionEnvControlInput } from '../types/action-type'

const BOOLEAN_FIELDS = ['incidentDuringOperation', 'hasDivingDuringOperation']

export function useMissionActionEnvControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionEnvControlInput> {
  const value = action?.data as MissionEnvActionData
  const { extractLatLngFromMultiPointRounded } = useCoordinate()
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId)
  const missionDates = useMissionDates(action.ownerId)

  const fromFieldValueToInput = useCallback(
    (data: MissionEnvActionData): ActionEnvControlInput => {
      const dates = getDateRangeForInput(data)
      return {
        ...data,
        dates,
        geoCoords: extractLatLngFromMultiPointRounded(data.geom)
      }
    },
    [getDateRangeForInput, extractLatLngFromMultiPointRounded]
  )

  const fromInputToFieldValue = useCallback(
    (value: ActionEnvControlInput): MissionEnvActionData => {
      const { dates, geoCoords, ...newData } = value
      return { ...newData, ...getDateRangeFromInput(dates) }
    },
    [getDateRangeFromInput]
  )

  const { initValue, handleSubmit } = useAbstractFormik<MissionEnvActionData, ActionEnvControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    BOOLEAN_FIELDS
  )

  const onSubmit = useCallback(
    async (valueToSubmit?: MissionEnvActionData) => {
      if (!valueToSubmit) return
      await onChange({ ...action, data: valueToSubmit })
    },
    [action, onChange]
  )

  const handleSubmitOverride = useCallback(
    async (value?: ActionEnvControlInput) => {
      handleSubmit(value, onSubmit)
    },
    [handleSubmit, onSubmit]
  )

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
