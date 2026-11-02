import { Accent, Button, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { Contact, SatiParty } from '../../../common/types/sati.ts'
import ContactFormItem from '../ui/contact-form-item.tsx'

interface FishingSectorControlInfosOwnerProps {
  owner?: SatiParty
  onChange: (value?: SatiParty) => void
}

const FishingSectorControlInfosOwner: FC<FishingSectorControlInfosOwnerProps> = ({ owner, onChange }) => {
  const [edit, setEdit] = useState<boolean>(true)

  const handleSubmit = (value: Contact) => {
    onChange({ ...(owner ?? {}), contact: value })
    setEdit(false)
  }

  useEffect(() => {
    setEdit(!owner?.contact?.email)
  }, [owner?.contact])

  return (
    <Stack direction="column" spacing="0.2rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Label>Informations sur le propriétaire de l’établissement</Label>
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
          <Stack direction="column" spacing="1rem" style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              <ContactFormItem
                readOnly={!edit}
                onSubmit={handleSubmit}
                contact={owner?.contact}
                onClose={() => setEdit(false)}
                disabled={false}
              />
            </Stack.Item>
            {!edit && (
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" spacing="0.75rem" justifyContent="flex-end">
                  <Stack.Item>
                    <Button
                      title="Editer"
                      size={Size.NORMAL}
                      accent={Accent.PRIMARY}
                      Icon={Icon.EditUnbordered}
                      color={THEME.color.charcoal}
                      onClick={() => setEdit(!edit)}
                    >
                      Editer
                    </Button>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            )}
          </Stack>
        </div>
      </Stack.Item>
    </Stack>
  )
}

export default FishingSectorControlInfosOwner
