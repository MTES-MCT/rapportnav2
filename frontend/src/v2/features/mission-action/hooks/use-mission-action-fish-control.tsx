import { StyledTabItem } from '../../common/components/ui/styled-tab.tsx'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionFishActionData } from '../../common/types/mission-action'
import FishControlConclusion from '../components/elements/fish-control-conclusion.tsx'
import FishControlOthers from '../components/elements/fish-control-others.tsx'
import FishControlPolpeche from '../components/elements/fish-control-polpeche.tsx'
import { ActionFishControlInput } from '../types/action-type'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { object, string } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { useMemo } from 'react'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'

const ITEMS: StyledTabItem[] = [
  {
    key: 'polpech',
    title: 'Police des pêches',
    component: FishControlPolpeche
  },
  {
    key: 'others',
    title: 'Autres polices',
    component: FishControlOthers
  },
  {
    key: 'conclusion',
    title: 'Conclusions',
    component: FishControlConclusion
  }
]


export function useMissionActionFishControl(
  action: MissionAction,
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
): AbstractFormikSubFormHook<ActionFishControlInput> & { items: StyledTabItem[] } {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionFishActionData
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const missionDates = useMissionDates(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionFishActionData): ActionFishControlInput => {
    const dates = getDateRangeForInput(data)
    return {
      ...data,
      dates,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionFishControlInput): MissionFishActionData => {
    const { dates, geoCoords, ...newData } = value
    return { ...newData, ...getDateRangeFromInput(dates) }
  }

  const { initValue, handleSubmit } = useAbstractFormik<MissionFishActionData, ActionFishControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionFishActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionFishControlInput) => {
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
