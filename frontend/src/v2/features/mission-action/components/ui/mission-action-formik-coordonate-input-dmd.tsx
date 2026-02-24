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

    const isCoordsEqual = (value: Coords) => {
      const isLatEqual = isEqual(value.coords[0]?.toFixed(3), initValue?.coords[0]?.toFixed(3))
      const isLngEqual = isEqual(value.coords[1]?.toFixed(3), initValue?.coords[1]?.toFixed(3))
      return isLatEqual && isLngEqual
    }

    const handleSubmit = async (value: Coords) => {
      if (isCoordsEqual(value)) return
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
