import { Accent, Button, Icon, Select, Size, THEME, Tag } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import useAdminServiceListQuery from '../../admin/services/use-admin-services-service'
import useImpersonation from '../hooks/use-impersonation'
import { ServiceType } from '../types/impersonation-types'

const ImpersonationContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`

const StyledSelect = styled(Select)`
  min-width: 200px;
`

const ImpersonationDropdown: FC = () => {
  const {
    isImpersonating,
    targetServiceId,
    targetServiceName,
    canImpersonate,
    startImpersonation,
    stopImpersonation
  } = useImpersonation()

  const { data: services, isLoading } = useAdminServiceListQuery({ active: true })

  // Only render for admins
  if (!canImpersonate) {
    return null
  }

  const serviceOptions = services?.map(service => ({
    value: service.id,
    label: service.name
  })) ?? []

  const handleServiceChange = (value: number | undefined) => {
    if (value) {
      const service = services?.find(s => s.id === value)
      if (service) {
        startImpersonation(service.id, service.name, service.serviceType as ServiceType)
      }
    }
  }

  const handleStopImpersonation = () => {
    stopImpersonation()
  }

  if (isImpersonating) {
    return (
      <ImpersonationContainer>
        <Tag backgroundColor={THEME.color.goldenPoppy} color={THEME.color.charcoal}>
          Mode service : {targetServiceName}
        </Tag>
        <Button
          accent={Accent.SECONDARY}
          size={Size.SMALL}
          onClick={handleStopImpersonation}
          Icon={Icon.Close}
        >
          Quitter
        </Button>
      </ImpersonationContainer>
    )
  }

  return (
    <Stack direction="row" alignItems="center" spacing={8}>
      <StyledSelect
        label=""
        placeholder="Voir comme un service..."
        options={serviceOptions}
        onChange={handleServiceChange}
        value={targetServiceId}
        disabled={isLoading}
        searchable
        cleanable={false}
      />
    </Stack>
  )
}

export default ImpersonationDropdown
