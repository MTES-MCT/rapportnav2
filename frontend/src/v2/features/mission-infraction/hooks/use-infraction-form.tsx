import { InfractionTypeEnum, VehicleTypeEnum, VesselSizeEnum, VesselTypeEnum } from '@common/types/env-mission-types'
import { array, boolean, mixed, object, ObjectSchema, string } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { AbstractFormikHook } from '../../common/types/abstract-formik-hook'
import { TargetInfraction, TargetInfractionInput, TargetType } from '../../common/types/target-types'

export function useInfractionForm(
  targetInfraction: TargetInfraction,
  targetType?: TargetType,
  vehichleType?: VehicleTypeEnum,
  editTarget?: boolean,
  editControl?: boolean,
  editInfraction?: boolean
): AbstractFormikHook<TargetInfraction, TargetInfractionInput> & { validationSchema?: ObjectSchema<any> } {
  const fromFieldValueToInput = (targetInfraction: TargetInfraction): TargetInfractionInput => {
    return {
      target: getTargetInput(targetInfraction),
      control: editControl ? targetInfraction?.control : undefined,
      infraction: editInfraction ? getinfractionInput(targetInfraction) : undefined
    }
  }

  const fromInputToFieldValue = (value: TargetInfractionInput): TargetInfraction => {
    return {
      control: value.control,
      target: getTarget(value),
      infraction: getinfraction(value)
    }
  }

  const getinfraction = (value: TargetInfractionInput) => {
    if (!value?.infraction) return
    const { withReport, ...infraction } = value.infraction
    const infractionType = withReport ? InfractionTypeEnum.WITH_REPORT : InfractionTypeEnum.WITHOUT_REPORT
    return {
      ...infraction,
      infractionType
    }
  }

  const getTarget = (value: TargetInfractionInput) => {
    if (!value?.target) return
    const { isVessel, isTargetVehicule, ...target } = value.target
    return target
  }

  const getinfractionInput = (targetInfraction: TargetInfraction) => {
    if (!targetInfraction?.infraction) return
    return {
      ...targetInfraction.infraction,
      ...{ withReport: targetInfraction.infraction?.infractionType === InfractionTypeEnum.WITH_REPORT }
    }
  }

  const getTargetInput = (targetInfraction: TargetInfraction) => {
    if (!targetInfraction?.target) return
    return {
      ...targetInfraction.target,
      isVessel: vehichleType === VehicleTypeEnum.VESSEL,
      isTargetVehicule: targetType === TargetType.VEHICLE
    }
  }

  const { initValue, beforeInitValue, beforeSubmit, handleSubmit } = useAbstractFormik<
    TargetInfraction,
    TargetInfractionInput
  >(targetInfraction, fromFieldValueToInput, fromInputToFieldValue)

  const getValidationSchema = () => {
    const controlSchema = {
      control: object().shape({
        controlType: string().required()
      })
    }
    const infractionSchema = {
      infraction: object().shape({
        natinfs: array().min(1).required()
      })
    }

    const targetSchema = {
      target: object().shape({
        isVessel: boolean(),
        isTargetVehicule: boolean(),
        vesselSize: mixed<VesselSizeEnum>()
          .nullable()
          .oneOf(Object.values(VesselSizeEnum))
          .when(['isVessel'], {
            is: true,
            then: schema => schema.nonNullable().required()
          }),
        vesselType: mixed<VesselTypeEnum>()
          .nullable()
          .oneOf(Object.values(VesselTypeEnum))
          .when('isVessel', {
            is: true,
            then: schema => schema.nonNullable().required()
          }),
        vesselIdentifier: string()
          .nullable()
          .when('isTargetVehicule', {
            is: true,
            then: schema => schema.nonNullable().required()
          }),
        identityControlledPerson: string().nonNullable().required()
      })
    }

    return object().shape({
      ...(!editTarget ? {} : targetSchema),
      ...(!editControl ? {} : controlSchema),
      ...(!editInfraction ? {} : infractionSchema)
    })
  }

  return {
    initValue,
    handleSubmit,
    beforeSubmit,
    beforeInitValue,
    validationSchema: getValidationSchema()
  }
}
