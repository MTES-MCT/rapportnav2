import { JSX } from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'

const StyledFooter = styled.div`
  height: 60px;
  background: var(--white-ffffff-) 0% 0% no-repeat padding-box;
  background: #ffffff 0% 0% no-repeat padding-box;
  border-top: 1px solid var(--lightGray-cccfd6-);
  border-top: 1px solid #cccfd6;
  padding: 0 2rem;
`

interface PageFooterWrapperProps {
  action?: JSX.Element
  message?: JSX.Element
  online?: JSX.Element
  exitMission: () => void
}

const PageFooterWrapper: React.FC<PageFooterWrapperProps> = ({ online, message, action }) => {
  return (
    <StyledFooter>
      <Stack direction="row" justifyContent="space-between" alignItems="center" style={{ height: '100%' }}>
        <Stack.Item style={{ paddingLeft: '1rem' }}>{action}</Stack.Item>
        <Stack.Item>
          <Stack direction="row">{message}</Stack>
        </Stack.Item>
        <Stack.Item style={{ paddingLeft: '1rem' }}>{online}</Stack.Item>
      </Stack>
    </StyledFooter>
  )
}

export default PageFooterWrapper
