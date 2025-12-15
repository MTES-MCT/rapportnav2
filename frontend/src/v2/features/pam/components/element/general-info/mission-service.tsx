import { FC } from 'react'
import { FormikSelect } from '@mtes-mct/monitor-ui'
import { Service } from '@common/types/crew-types.ts'
import { useOnlineManager } from '../../../../common/hooks/use-online-manager.tsx'

export interface MissionServiceProps {
  services?: Service[]
}

const MissionService: FC<MissionServiceProps> = ({ services }) => {
  const { isOffline } = useOnlineManager()
  const getOptions = (values?: Service[]): { value: string; label: string }[] => {
    return (
      values?.map((service, index) => ({
        value: service.id,
        label: String.fromCharCode(64 + index + 1)
      })) || []
    )
  }

  return (
    <FormikSelect
      name="service.id"
      label="Bordée"
      isCleanable={false}
      options={getOptions(services)}
      data-testid="mission-service-select"
      disabled={isOffline}
      placeholder="Sélectionnez votre bordée"
    />
  )
}

export default MissionService
