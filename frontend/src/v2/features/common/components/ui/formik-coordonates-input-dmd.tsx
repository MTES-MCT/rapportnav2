import { CoordinatesFormat, FormikCoordinatesInput, FormikCoordinatesInputProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

export const FormikCoordinateInputDMD = styled(
  ({ label, ...props }: Omit<FormikCoordinatesInputProps, 'coordinatesFormat'>) => (
    <FormikCoordinatesInput
      label={`${label} (01° 12.600′ S 044° 58.800′ E)`}
      coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
      {...props}
    />
  )
)({})
