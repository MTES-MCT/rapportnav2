import { Label } from '@mtes-mct/monitor-ui'
import { useSelector } from '@tanstack/react-store'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { store } from '../../../../store/index.ts'
import { SelectInput } from '../../../common/components/ui/formik-select-input.tsx'
import { StyledTextInput } from '../../../common/components/ui/formik-text-input.tsx'
import useResources from '../../../common/hooks/use-resources.tsx'
import { ControlUnitResource } from '../../../common/types/control-unit-types.ts'
import { ControlResource } from '../../../common/types/sati.ts'

interface ResourceFormProps {
  resource?: ControlResource
  onChange: (resourceId?: ControlUnitResource) => void
}

const ResourceForm: FC<ResourceFormProps> = ({ onChange, resource }) => {
  const user = useSelector(store, state => state.user)
  const [value, setValue] = useState<ControlUnitResource>()
  const { getResourceById, getResourcesOptionsByControlUnitId } = useResources()

  const handleChange = (id?: string) => {
    if (!id) return
    const newValue = getResourceById(Number(id))
    setValue(newValue)
    onChange(newValue)
  }

  useEffect(() => {
    if (resource?.id === value?.id) return
    setValue(getResourceById(resource?.id))
  }, [resource])

  return (
    <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Label>{`Informations sur le navire d’inspection`}</Label>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <div style={{ width: '100%', padding: '16px', backgroundColor: 'white' }}>
          <Stack
            direction="column"
            spacing=".5rem"
            justifyContent={'flex-start'}
            alignItems={'flex-start'}
            style={{ width: '100%' }}
          >
            <Stack.Item style={{ flex: 1, width: '100%' }}>
              <SelectInput
                isRequired
                isLight={false}
                name="id"
                isErrorMessageHidden
                onChange={handleChange}
                value={value?.id?.toString()}
                label="Moyen utilisé pour l’inspection"
                options={getResourcesOptionsByControlUnitId(user?.controlUnitId)}
              />
            </Stack.Item>
            <Stack.Item style={{ flex: 1, width: '100%' }}>
              <Stack
                direction="row"
                spacing=".5rem"
                justifyContent={'flex-start'}
                alignItems={'flex-start'}
                style={{ width: '100%' }}
              >
                <Stack.Item style={{ flex: 1, width: '100%' }}>
                  <StyledTextInput
                    isRequired
                    readOnly={true}
                    name="registrationId"
                    label="Marquage externe"
                    value={value?.registrationId}
                  />
                </Stack.Item>
                <Stack.Item style={{ flex: 1, width: '100%' }}>
                  <StyledTextInput
                    isRequired
                    readOnly={true}
                    name="radioFrequency"
                    value={value?.radioFrequency}
                    label="Indicatif international d’appel radio"
                  />
                </Stack.Item>
              </Stack>
            </Stack.Item>
          </Stack>
        </div>
      </Stack.Item>
    </Stack>
  )
}

export default ResourceForm
