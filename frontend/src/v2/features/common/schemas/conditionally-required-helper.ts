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
  return (isRequired: boolean): T => {
    const baseSchema = baseSchemaFactory()

    if (!isRequired) {
      return baseSchema
    }

    // If no form field dependencies, directly apply required validation
    const deps = Array.isArray(dependsOn) ? dependsOn : [dependsOn]
    const hasFormFieldDependency = deps.length > 0 && deps[0] !== ''

    if (!hasFormFieldDependency) {
      const updated = baseSchema.required(errorMessage)
      return transformThen ? transformThen(updated) : updated
    }

    // Use .when() to make validation conditional on form field values
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
