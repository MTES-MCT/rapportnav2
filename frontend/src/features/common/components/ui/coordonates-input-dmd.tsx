import { CoordinatesFormat, CoordinatesInput, CoordinatesInputProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

export const CoordinateInputDMD = styled(({ label, ...props }: Omit<CoordinatesInputProps, 'coordinatesFormat'>) => (
  <CoordinatesInput
    label={`${label} (12° 12.12′ N 121° 21.21′ E)`}
    coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
    {...props}
  />
))({})
