import { Accent, Button, Icon, IconButton, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { isEmpty } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { Contact } from '../../../common/types/sati.ts'
import ContactFormItem from './contact-form-item.tsx'

interface AddContactFormProps {
  title: string
  contact?: Contact
  buttonLabel: string
  onDelete: () => void
  onChange: (newValue: Contact) => void
}

const AddContactForm: FC<AddContactFormProps> = ({ contact, title, onChange, onDelete, buttonLabel }) => {
  const [edit, setEdit] = useState<boolean>(true)
  const [showForm, setShowForm] = useState(false)

  const handleAdd = () => {
    setShowForm(true)
  }

  const handleDelete = () => {
    onDelete()
    setShowForm(false)
  }

  const handleSubmit = (response: Contact) => {
    onChange(response)
    setShowForm(false)
  }

  const handleCancel = () => {
    setEdit(false)
    setShowForm(false)
  }

  useEffect(() => {
    setEdit(!contact)
    setShowForm(!!contact)
  }, [contact])

  if (!showForm && isEmpty(contact)) {
    return (
      <Button
        Icon={Icon.Plus}
        size={Size.NORMAL}
        onClick={handleAdd}
        accent={Accent.SECONDARY}
        style={{ width: '100%' }}
      >
        {buttonLabel}
      </Button>
    )
  }

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
        <div style={{ width: '100%', padding: '16px', backgroundColor: 'white' }}>
          <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%', display: 'flex', justifyContent: 'flex-end' }}>
              {!edit && (
                <Stack direction="row" spacing="0.75rem" justifyContent="flex-start">
                  <Stack.Item>
                    <IconButton
                      title="Editer"
                      Icon={Icon.EditUnbordered}
                      size={Size.NORMAL}
                      accent={Accent.SECONDARY}
                      color={THEME.color.charcoal}
                      onClick={() => setEdit(!edit)}
                    />
                  </Stack.Item>
                  <Stack.Item>
                    <IconButton
                      title="Supprimer"
                      Icon={Icon.Delete}
                      size={Size.NORMAL}
                      onClick={handleDelete}
                      accent={Accent.SECONDARY}
                      color={THEME.color.maximumRed}
                    />
                  </Stack.Item>
                </Stack>
              )}
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <ContactFormItem readOnly={!edit} contact={contact} onSubmit={handleSubmit} onClose={handleCancel} />
            </Stack.Item>
          </Stack>
        </div>
      </Stack.Item>
    </Stack>
  )
}

export default AddContactForm
