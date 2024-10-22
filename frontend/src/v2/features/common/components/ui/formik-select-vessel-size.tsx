import { FormikSelect, FormikSelectProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'
import { useVessel } from '../../hooks/use-vessel'

export const FormikSelectVesselSize = styled((props: Omit<FormikSelectProps, 'options'>) => {
  const { vesselSizeOptions } = useVessel()
  return (
    <FormikSelect isLight={true} isRequired={true} isErrorMessageHidden={true} {...props} options={vesselSizeOptions} />
  )
})({})
