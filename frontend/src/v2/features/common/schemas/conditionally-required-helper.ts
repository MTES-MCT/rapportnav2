import { AnySchema } from 'yup'

type SchemaFactory<T extends AnySchema> = () => T

type WhenCondition = any | ((...values: any[]) => boolean)

export const conditionallyRequired = <T extends AnySchema>(
  baseSchemaFactory: SchemaFactory<T>,
  dependsOn: string | string[],
  condition: WhenCondition,
  errorMessage?: string,
  transformThen?: (schema: T) => T,
  transformOtherwise?: (schema: T) => T
) => {
  return (isMissionFinished: boolean): T => {
    const baseSchema = baseSchemaFactory()

    if (!isMissionFinished) {
      return baseSchema
    }

    return baseSchema.when(dependsOn, {
      is: condition,
      then: schema => {
        const updated = schema.required(errorMessage)
        return transformThen ? transformThen(updated) : updated
      },
      otherwise: schema => {
        return transformOtherwise ? transformOtherwise(schema) : schema.notRequired()
      }
    })
  }
}

export default conditionallyRequired
