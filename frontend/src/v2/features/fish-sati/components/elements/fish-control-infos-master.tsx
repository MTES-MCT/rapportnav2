import { Label, Toggle } from '@mtes-mct/monitor-ui'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { IconBadgeEdit } from '../../../common/components/ui/icon-badge-edit.tsx'
import { Address, Contact, SatiParty } from '../../../common/types/sati.ts'
import ContactFormItem from '../ui/contact-form-item.tsx'

const emptyContact: Contact = { address: {} as Address } as Contact

interface FishControlInfosMasterProps {
  owner?: SatiParty
  master?: SatiParty
  onchange: (value?: Contact) => void
}

const FishControlInfosMaster: FC<FishControlInfosMasterProps> = ({ owner, master, onchange }) => {
  const [edit, setEdit] = useState<boolean>(false)
  const [isMasterOwner, setIsMasterOwner] = useState(false)

  const getContact = (checked: boolean) => {
    const contactId = master?.contact?.id
    const addressId = master?.contact?.address?.id
    const baseContact = checked ? owner?.contact : emptyContact
    return { ...baseContact, id: contactId, address: { ...baseContact?.address, id: addressId } } as Contact
  }

  const handleToggle = (checked: boolean) => {
    setIsMasterOwner(checked)
    onchange(getContact(checked))
    setEdit(false)
  }

  const handleSubmit = (value: Contact) => {
    onchange(value)
    setEdit(false)
  }

  useEffect(() => {
    setIsMasterOwner(owner?.contact?.fullName === master?.contact?.fullName)
  }, [owner?.contact?.fullName, master?.contact?.fullName])

  return (
    <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" justifyContent="space-between" alignItems="center" style={{ width: '100%' }}>
          <Stack.Item>
            <Label>Informations sur le capitaine du navire contrôlé</Label>
          </Stack.Item>
          {!edit && (
            <Stack.Item>
              <div role="button" tabIndex={0} style={{ cursor: 'pointer' }} onClick={() => setEdit(!edit)}>
                <IconBadgeEdit />
              </div>
            </Stack.Item>
          )}
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <div
          style={{
            width: '100%',
            padding: '16px',
            backgroundColor: 'white',
            border: edit && !isMasterOwner ? '1px dashed #5697D2' : '1px solid transparent'
          }}
        >
          <Stack direction="column" spacing="1rem" style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing="0.75rem" justifyContent="flex-start">
                <Stack.Item>
                  <Toggle
                    label=""
                    isLabelHidden
                    checked={isMasterOwner}
                    onChange={handleToggle}
                    name="master-same-as-owner"
                  />
                </Stack.Item>
                <Stack.Item>
                  <span style={{ fontSize: '13px' }}>
                    Informations identiques à celle du propriétaire (Saisie auto)
                  </span>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ContactFormItem
                readOnly={!edit}
                onSubmit={handleSubmit}
                contact={master?.contact}
                onClose={() => setEdit(false)}
              />
            </Stack.Item>
          </Stack>
        </div>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlInfosMaster
