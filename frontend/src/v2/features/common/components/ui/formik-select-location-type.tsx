import { ControlMethod } from '@common/types/control-types'
import { Select } from '@mtes-mct/monitor-ui'
import { useField } from 'formik'
import { useMemo } from 'react'
import styled from 'styled-components'
import { LAND_LOCATION_TYPES, LOCATION_TYPES, LocationType, SEA_LOCATION_TYPES } from '../../types/location-type'

type FormikSelectLocationTypeProps = {
  name: string
  label: string
  isLight?: boolean
  controlMethod?: ControlMethod
  onChangeEffect?: (nextValue: LocationType | undefined) => void
  className?: string
}

export const FormikSelectLocationType = styled(
  ({ controlMethod, onChangeEffect, ...props }: FormikSelectLocationTypeProps) => {
    const [field, meta, helpers] = useField<LocationType | undefined>(props.name)

    const options = useMemo(() => {
      const locationTypes = controlMethod === ControlMethod.LAND ? LAND_LOCATION_TYPES : SEA_LOCATION_TYPES
      return locationTypes.map(type => ({
        value: type,
        label: LOCATION_TYPES[type]
      }))
    }, [controlMethod])

    const handleChange = (nextValue: LocationType | undefined) => {
      helpers.setValue(nextValue)
      onChangeEffect?.(nextValue)
    }

    return (
      <Select<LocationType>
        isLight={true}
        isRequired={true}
        isErrorMessageHidden={true}
        {...props}
        value={field.value}
        error={meta.touched ? meta.error : undefined}
        onChange={handleChange}
        options={options}
      />
    )
  }
)({})
