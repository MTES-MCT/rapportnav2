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
  const fromFieldValueToInput = (targetInfraction: TargetInfraction) => {
    return {
      target: {
        ...(targetInfraction?.target ?? {}),
        isVessel: vehichleType === VehicleTypeEnum.VESSEL,
        isTargetVehicule: targetType === TargetType.VEHICLE
      },
      control: targetInfraction?.control ?? {},
      infraction: {
        ...(targetInfraction.infraction ?? {}),
        controlType: targetInfraction.control?.controlType,
        withReport: targetInfraction.infraction?.infractionType === InfractionTypeEnum.WITH_REPORT
      }
    }
  }

  const fromInputToFieldValue = (value: TargetInfractionInput) => {
    const { target: targetInput, infraction: infractionInput, control } = value
    const { isVessel, isTargetVehicule, ...target } = targetInput
    const { withReport, controlType, ...infraction } = infractionInput

    const infractionType = withReport ? InfractionTypeEnum.WITH_REPORT : InfractionTypeEnum.WITHOUT_REPORT
    control.controlType = controlType
    return {
      infraction: {
        ...infraction,
        infractionType
      },
      target,
      control
    }
  }

  const { initValue, beforeInitValue, beforeSubmit, handleSubmit } = useAbstractFormik<
    TargetInfraction,
    TargetInfractionInput
  >(targetInfraction, fromFieldValueToInput, fromInputToFieldValue)

  const getValidationSchema = () => {
    const infractionSchema = {
      infraction: object().shape({
        natinfs: array().min(1).required(),
        controlType: editControl ? string().required() : string()
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

    return object().shape({ ...(!editInfraction ? {} : infractionSchema), ...(!editTarget ? {} : targetSchema) })
  }

  return {
    initValue,
    handleSubmit,
    beforeSubmit,
    beforeInitValue,
    validationSchema: getValidationSchema()
  }
}
