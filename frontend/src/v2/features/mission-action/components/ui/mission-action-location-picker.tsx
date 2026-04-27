import { Field, FieldProps, useFormikContext } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikSearchPort } from '../../../common/components/ui/formik-search-port'
import { FormikSelectLocationType } from '../../../common/components/ui/formik-select-location-type'
import { SearchCity } from '../../../common/components/ui/search-city'
import { LocationType } from '../../../common/types/location-type'
import { MissionNavActionData } from '../../../common/types/mission-action'
import { MissionActionFormikCoordinateInputDMD } from './mission-action-formik-coordonate-input-dmd'

const MissionActionLocationPicker: FC = () => {
  const { values, setFieldValue } = useFormikContext<MissionNavActionData>()

  return (
    <Stack>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction={'row'} spacing={'1rem'}>
          <Stack.Item style={{ width: '25%' }}>
            <FormikSelectLocationType
              name="locationType"
              isLight={true}
              label="Lieu de l'operation"
              controlMethod={values.controlMethod}
              onChangeEffect={() => {
                setFieldValue('geoCoords', [])
                setFieldValue('zipCode', undefined)
                setFieldValue('city', undefined)
                setFieldValue('portLocode', undefined)
              }}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '75%' }}>
            {values.locationType === LocationType.GPS && (
              <Field name="geoCoords">
                {(field: FieldProps<number[]>) => (
                  <MissionActionFormikCoordinateInputDMD name="geoCoords" fieldFormik={field} />
                )}
              </Field>
            )}
            {values.locationType === LocationType.PORT && (
              <Field name="portLocode">
                {(field: FieldProps<string>) => (
                  <FormikSearchPort name="portLocode" isLight={true} label="Nom du port" fieldFormik={field} />
                )}
              </Field>
            )}
            {values.locationType === LocationType.COMMUNE && (
              <Field name="zipCode">
                {(field: FieldProps<string>) => (
                  <SearchCity
                    name="zipCode"
                    label="Nom de la commune"
                    isLight={true}
                    value={{ zipCode: values.zipCode, city: values.city }}
                    onChange={val => {
                      setFieldValue('zipCode', val?.zipCode)
                      setFieldValue('city', val?.city)
                    }}
                    fieldFormik={field}
                  />
                )}
              </Field>
            )}
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionLocationPicker
