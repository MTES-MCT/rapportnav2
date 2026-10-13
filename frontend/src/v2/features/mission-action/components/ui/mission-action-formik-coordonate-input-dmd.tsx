import { useEffect, useState } from 'react'
import { FormikCoordinatesInputProps, FormikEffect } from '@mtes-mct/monitor-ui'
import { FieldProps, Formik } from 'formik'
import styled from 'styled-components'
import { FormikCoordinateInputDMD } from '../../../common/components/ui/formik-coordonates-input-dmd'
import { useCoordinate } from '../../../common/hooks/use-coordinate.tsx'

type Coords = {
  coords: (number | undefined)[]
}

type MissionActionFormikCoordinateInputDMDProps = {
  name: string
  fieldFormik: FieldProps<number[]>
} & Omit<FormikCoordinatesInputProps, 'label' | 'coordinatesFormat'>

export const MissionActionFormikCoordinateInputDMD = styled(
  ({ name, fieldFormik, ...props }: MissionActionFormikCoordinateInputDMDProps) => {
    const { isCoordsEqual, roundCoord } = useCoordinate()
    const [initValue, setInitValue] = useState<Coords>()

    useEffect(() => {
      if (!fieldFormik?.field?.value) return
      setInitValue({ coords: fieldFormik.field.value })
    }, [fieldFormik])

    const handleSubmit = async (value: Coords) => {
      if (isCoordsEqual(value.coords, initValue?.coords)) return
      await fieldFormik.form.setFieldValue(name, value.coords.map(roundCoord))
    }

    return (
      <>
        {initValue && (
          <Formik
            initialValues={initValue}
            initialErrors={fieldFormik.meta.error ? { coords: fieldFormik.meta.error } : undefined}
            onSubmit={handleSubmit}
            enableReinitialize
            validateOnChange
          >
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
