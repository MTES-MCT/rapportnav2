import { FormikDatePicker, Label } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import BannerYesNo from '../../../common/components/ui/banner-yes-no.tsx'
import { FormikSearchPort } from '../../../common/components/ui/formik-search-port.tsx'
import { SatiJpe } from '../../../common/types/sati.ts'

interface FishControlInfosPortProps {
  name: string
  jpe?: SatiJpe
  onChange: (jpe?: SatiJpe) => void
}

const FishControlInfosPort: FC<FishControlInfosPortProps> = ({ name, jpe, onChange }) => {
  const handleToggle = (lastPortIsNotSame: boolean) => {
    onChange({
      ...jpe,
      lastPortIsNotSame,
      portId: lastPortIsNotSame ? jpe?.portId : undefined,
      lastStopDate: lastPortIsNotSame ? jpe?.lastStopDate : undefined
    })
  }

  return (
    <Stack direction="column" spacing="0.2rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" justifyContent="space-between" alignItems="center" style={{ width: '100%' }}>
          <Stack.Item>
            <Label>Informations sur le dernier port d’escale</Label>
          </Stack.Item>
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <div style={{ width: '100%', padding: '16px', backgroundColor: 'white' }}>
          <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              <BannerYesNo
                onSubmit={handleToggle}
                title={`Port d'escale`}
                value={jpe?.lastPortIsNotSame}
                message={`Le dernier port d'escale est different du port controlé`}
              />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing=".5rem" justifyContent={'flex-start'} style={{ width: '100%' }}>
                <Stack.Item style={{ width: '30%' }}>
                  <FormikDatePicker
                    isLight={false}
                    withTime={false}
                    isRequired={true}
                    isCompact={false}
                    label="Date de l’escale"
                    name={`${name}.lastStopDate`}
                    disabled={!jpe?.lastPortIsNotSame}
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '70%' }}>
                  <FormikSearchPort
                    name={`${name}.portId`}
                    isLight={false}
                    label="Nom du port d’escale"
                    disabled={!jpe?.lastPortIsNotSame}
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

export default FishControlInfosPort
