import { FC, useEffect, useState } from 'react'
import { Select, SelectProps } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { Service } from '../../types/crew-types'
import useUpdateMissionService from './use-update-mission-service.tsx'

const StyledSelect = styled((props: SelectProps) => <Select placeholder={' '} {...props} />)({
  width: 100
})

interface MissionServiceProps {
  missionId: number
  serviceId?: number
  services?: Service[]
}

const MissionService: FC<MissionServiceProps> = ({ services, missionId, serviceId }) => {
  const [id, setId] = useState<string>()
  const [updateMissionService] = useUpdateMissionService()

  useEffect(() => {
    if (!serviceId) return
    setId(serviceId.toString())
  }, [serviceId])

  const getOptions = (values?: Service[]): { value: string; label: string }[] => {
    return (
      values?.map((service, index) => ({
        value: service.id,
        label: String.fromCharCode(64 + index + 1)
      })) || []
    )
  }
  const onChange = async (nextValue?: string) => {
    setId(nextValue)
    if (!nextValue) return
    const service = { missionId, serviceId: Number(nextValue) }
    await updateMissionService({
      variables: {
        service
      }
    })
  }

  return (
    <Stack>
      <Stack.Item>
        <StyledSelect
          name="id"
          value={id}
          label="Bordée"
          onChange={onChange}
          isCleanable={false}
          options={getOptions(services)}
          data-testid="mission-service-select"
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionService
