import { ToggleLabel } from '@common/components/ui/toogle-label'
import { FormikToggle, ToggleProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

export const StyledFormikToggle = styled(({ label, ...props }: ToggleProps) => (
  <div style={{ width: '100%', display: 'flex', flexDirection: 'row', alignItems: 'end' }}>
    <FormikToggle {...props} label="" isLight isLabelHidden readOnly={false} />
    <ToggleLabel>{label}</ToggleLabel>
  </div>
))(({ theme }) => ({
  marginRight: 8
}))
