import { Accent, Button, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { FC, ReactNode, useEffect, useState } from 'react'
import { Stack } from 'rsuite'

export interface PanelEditChildrenParams {
  edit: boolean
  setEdit: (edit: boolean) => void
}

interface PanelEditLayoutProps {
  title?: string
  defaultEdit?: boolean
  children: (params: PanelEditChildrenParams) => ReactNode
}

const PanelEditLayout: FC<PanelEditLayoutProps> = ({ title, defaultEdit = true, children }) => {
  const [edit, setEdit] = useState<boolean>(defaultEdit)

  useEffect(() => {
    setEdit(defaultEdit)
  }, [defaultEdit])

  return (
    <Stack direction="column" spacing="0.2rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Label>{title}</Label>
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
            <Stack.Item style={{ width: '100%' }}>{children({ edit, setEdit })}</Stack.Item>
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

export default PanelEditLayout
