import { Accent, Icon, IconButton, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { FC, useEffect, useState } from 'react'
import { Divider, Stack } from 'rsuite'
import BannerYesNo from '../../../common/components/ui/banner-yes-no.tsx'
import { Address, Contact, SatiVessel } from '../../../common/types/sati.ts'
import ContactFormItem from '../ui/contact-form-item.tsx'

const emptyContact: Contact = { address: {} as Address } as Contact

interface FishControlInfosMasterProps {
  vessel?: SatiVessel
  onchange: (value?: SatiVessel) => void
}

const FishControlInfosMaster: FC<FishControlInfosMasterProps> = ({ vessel, onchange }) => {
  const [edit, setEdit] = useState<boolean>(true)

  const getContact = (checked: boolean) => {
    const contactId = vessel?.master?.contact?.id
    const addressId = vessel?.master?.contact?.address?.id
    const baseContact = checked ? vessel?.owner?.contact : emptyContact
    return { ...baseContact, id: contactId, address: { ...baseContact?.address, id: addressId } } as Contact
  }

  const handleToggle = (checked: boolean) => {
    const master = { ...vessel?.master, contact: getContact(checked) }
    onchange({ ...vessel, isMasterOwner: checked, master })
    setEdit(true)
  }

  const handleSubmit = (value: Contact) => {
    const master = { ...vessel?.master, contact: value }
    onchange({ ...vessel, master })
    setEdit(false)
  }

  useEffect(() => {
    setEdit(!vessel?.master?.contact?.email)
  }, [vessel?.master?.contact])

  return (
    <Stack direction="column" spacing="0.2rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Label>Informations sur le capitaine du navire contrôlé</Label>
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
              <BannerYesNo
                onSubmit={handleToggle}
                value={vessel?.isMasterOwner}
                title={`Saisie des informations`}
                message={`Les informations du capitaine sont-ellesidentiques à celle du propriétaire `}
              />
            </Stack.Item>
            {!edit && (
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" spacing="0.75rem" justifyContent="flex-end">
                  <Stack.Item>
                    <IconButton
                      title="Editer"
                      size={Size.NORMAL}
                      accent={Accent.SECONDARY}
                      Icon={Icon.EditUnbordered}
                      color={THEME.color.charcoal}
                      onClick={() => setEdit(!edit)}
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            )}
            {!edit && (
              <Stack.Item style={{ width: '100%' }}>
                <Divider style={{ margin: 8 }} />
              </Stack.Item>
            )}
            <Stack.Item style={{ width: '100%' }}>
              <ContactFormItem
                readOnly={!edit}
                onSubmit={handleSubmit}
                onClose={() => setEdit(false)}
                contact={vessel?.master?.contact}
                disabled={vessel?.isMasterOwner === undefined}
              />
            </Stack.Item>
          </Stack>
        </div>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlInfosMaster
