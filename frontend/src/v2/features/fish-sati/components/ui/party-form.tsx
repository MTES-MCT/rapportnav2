import { Label } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { SatiParty } from 'src/v2/features/common/types/sati'
import ContactFormItem from './contact-form-item'

interface PartyFormProps {
  title: string
  party?: SatiParty
}

const PartyForm: FC<PartyFormProps> = ({ title, party }) => {
  return (
    <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" justifyContent="space-between" alignItems="center" style={{ width: '100%' }}>
          <Stack.Item>
            <Label>{title}</Label>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <div
          style={{
            width: '100%',
            padding: '16px',
            backgroundColor: 'white',
            border: '1px solid transparent'
          }}
        >
          <ContactFormItem readOnly={true} contact={party?.contact} />
        </div>
      </Stack.Item>
    </Stack>
  )
}

export default PartyForm
