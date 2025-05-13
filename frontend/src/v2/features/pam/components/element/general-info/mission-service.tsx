import { FC } from 'react'
import { FormikSelect } from '@mtes-mct/monitor-ui'
import { Service } from '@common/types/crew-types.ts'

export interface MissionServiceProps {
  services?: Service[]
}

const MissionService: FC<MissionServiceProps> = ({ services }) => {
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
      name="serviceId"
      label="Bordée"
      isCleanable={false}
      options={getOptions(services)}
      data-testid="mission-service-select"
    />
  )
}

export default MissionService
