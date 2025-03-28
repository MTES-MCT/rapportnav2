import {
  ActionTargetTypeEnum,
  InfractionTypeEnum,
  VehicleTypeEnum,
  VesselSizeEnum,
  VesselTypeEnum
} from '@common/types/env-mission-types'
import { InfractionByTarget } from '@common/types/infraction-types'
import { FieldProps } from 'formik'
import { array, boolean, mixed, object, string } from 'yup'
import { useAbstractFormikSubForm } from '../../common/hooks/use-abstract-formik-sub-form'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'

export type EnvInfractionByTargetInput = {
  isVessel?: boolean
  withReport: boolean
  isTargetVehicule?: boolean
} & InfractionByTarget

export function useInfractionEnvForm(
  name: string,
  fieldFormik: FieldProps<InfractionByTarget>,
  actionTargetType?: ActionTargetTypeEnum
): AbstractFormikSubFormHook<EnvInfractionByTargetInput> {
  const fromFieldValueToInput = (byTarget: InfractionByTarget) => {
    const withReport = byTarget?.infractions[0].infractionType === InfractionTypeEnum.WITH_REPORT
    const isVessel = byTarget.infractions[0].target?.vehicleType === VehicleTypeEnum.VESSEL
    const isTargetVehicule = actionTargetType === ActionTargetTypeEnum.VEHICLE
    return { ...byTarget, isVessel, withReport, isTargetVehicule }
  }

  const fromInputToFieldValue = (value: EnvInfractionByTargetInput) => {
    const { isVessel, withReport, isTargetVehicule, ...newValue } = value
    const infractionType = withReport ? InfractionTypeEnum.WITH_REPORT : InfractionTypeEnum.WITHOUT_REPORT
    newValue.infractions[0].infractionType = infractionType
    return newValue
  }

  const { initValue, handleSubmit } = useAbstractFormikSubForm<InfractionByTarget, EnvInfractionByTargetInput>(
    name,
    fieldFormik,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const validationSchema = object().shape({
    isVessel: boolean(),
    isTargetVehicule: boolean(),
    infractions: array(
      object({
        controlType: string().required(),
        natinfs: array().min(1).required(),
        target: object()
          .shape({
            vesselSize: mixed<VesselSizeEnum>()
              .nullable()
              .oneOf(Object.values(VesselSizeEnum))
              .when('isVessel', {
                is: true,
                then: schema => schema.nonNullable().required()
              }),
            vesselType: mixed<VesselTypeEnum>()
              .nullable()
              .oneOf(Object.values(VesselTypeEnum))
              .test('isVessel', 'Please enter vessel type', function (val) {
                return this.from && this.from[2].value?.isVessel ? string().required().isValidSync(val) : true
              })
              .when('isVessel', {
                is: true,
                then: schema => schema.nonNullable().required()
              }),
            vesselIdentifier: string()
              .nullable()
              .test('isTargetVehicule', 'Please enter vessel identifier', function (val) {
                return this.from && this.from[2].value?.isTargetVehicule ? string().required().isValidSync(val) : true
              }),
            identityControlledPerson: string().nonNullable().required()
          })
          .when('isVessel', {
            is: true,
            then: schema => schema.nonNullable().required()
          })
      })
    )
  })

  return {
    initValue,
    handleSubmit,
    validationSchema
  }
}
