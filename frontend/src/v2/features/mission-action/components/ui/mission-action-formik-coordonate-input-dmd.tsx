import { FormikCoordinatesInputProps, FormikEffect } from '@mtes-mct/monitor-ui'
import { FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import { useEffect, useState } from 'react'
import styled from 'styled-components'
import { FormikCoordinateInputDMD } from '../../../common/components/ui/formik-coordonates-input-dmd'

type Coords = {
  coords: (number | undefined)[]
}

type MissionActionFormikCoordinateInputDMDProps = {
  name: string
  fieldFormik: FieldProps<number[]>
} & Omit<FormikCoordinatesInputProps, 'label' | 'coordinatesFormat'>

export const MissionActionFormikCoordinateInputDMD = styled(
  ({ name, fieldFormik, ...props }: MissionActionFormikCoordinateInputDMDProps) => {
    const [initValue, setInitValue] = useState<Coords>()

    useEffect(() => {
      if (!fieldFormik?.field?.value) return
      setInitValue({ coords: fieldFormik.field.value })
    }, [fieldFormik])

    const handleSubmit = async (value: Coords) => {
      if (isEqual(value.coords, initValue?.coords)) return
      await fieldFormik.form.setFieldValue(name, value.coords)
    }

    return (
      <>
        {initValue && (
          <Formik initialValues={initValue} onSubmit={handleSubmit} enableReinitialize validateOnChange>
            {() => (
              <>
                <FormikEffect onChange={nextValue => handleSubmit(nextValue as Coords)} />
                <FormikCoordinateInputDMD
                  name="coords"
                  isLight={true}
                  isRequired={true}
                  disabled={false}
                  label="Lieu de l'opération"
                  isErrorMessageHidden={true}
                  {...props}
                />
              </>
            )}
          </Formik>
        )}
      </>
    )
  }
)({})
