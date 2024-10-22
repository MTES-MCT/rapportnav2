import { CoordinatesFormat, FormikCoordinatesInput, FormikCoordinatesInputProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

export const FormikCoordinateInputDMD = styled(
  ({ label, ...props }: Omit<FormikCoordinatesInputProps, 'coordinatesFormat'>) => (
    <FormikCoordinatesInput
      label={`${label} (12° 12.12′ N 121° 21.21′ E)`}
      coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
      {...props}
    />
  )
)({})
