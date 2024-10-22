import { FormikCoordinatesInputProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'
import { FormikCoordinateInputDMD } from '../../../common/components/ui/formik-coordonates-input-dmd'

export const MissionActionFormikCoordinateInputDMD = styled(
  (props: Omit<FormikCoordinatesInputProps, 'label' | 'coordinatesFormat'>) => (
    <FormikCoordinateInputDMD
      isLight={true}
      isRequired={true}
      disabled={false}
      label="Lieu de l'opération"
      isErrorMessageHidden={true}
      {...props}
    />
  )
)({})
