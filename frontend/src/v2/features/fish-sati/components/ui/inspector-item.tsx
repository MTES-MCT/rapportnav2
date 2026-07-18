import { FormikEffect } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Divider, Stack } from 'rsuite'
import { useAgent } from '../../../common/hooks/use-agent'
import { Contact, SatiInspector, SatiParty } from '../../../common/types/sati'
import InspectorItemFooter from './inspector-item-footer'
import InspectorItemForm from './inspector-item-form'
import InspectorItemHeader from './inspector-item-header'

const emptyInspector = { party: { contact: {} as Contact } as SatiParty } as SatiInspector

interface InspectorItemProps {
  readOnly?: boolean
  onClose?: () => void
  onDelete?: () => void
  isPrincipal?: boolean
  inspector?: SatiInspector
  onChange?: (value?: SatiInspector) => void
  onSubmit?: (value?: SatiInspector) => void
  excludedAgentIds?: number[]
}

const InspectorItem: FC<InspectorItemProps> = ({
  readOnly,
  onChange,
  onDelete,
  onClose,
  onSubmit,
  inspector,
  isPrincipal,
  excludedAgentIds
}) => {
  const { getAgent } = useAgent()
  const [edit, setEdit] = useState<boolean>(true)
  const initialValues = { ...emptyInspector, ...(inspector ?? {}) }

  useEffect(() => {
    setEdit(!readOnly)
  }, [readOnly])

  const handleChange = (response?: SatiInspector) => {
    if (!response) return
    const next = isPrincipal ? { ...response, cardId: getAgent(response?.agentId)?.cardId } : response
    if (isEqual(next, inspector)) return
    if (onChange) onChange(next)
  }

  const handleSubmit = (response?: SatiInspector) => {
    if (!response) return

    if (onSubmit)
      onSubmit({
        ...response,
        agentId: response.isOutOfUnit ? undefined : response.agentId
      })
    handleClose()
  }

  const handleDelete = () => {
    if (onDelete) onDelete()
    handleClose()
  }

  const handleClose = () => {
    setEdit(false)
    if (onClose) onClose()
  }

  return (
    <Formik
      enableReinitialize
      initialValues={initialValues}
      onSubmit={response => handleSubmit(response as SatiInspector)}
    >
      {({ values }) => (
        <Stack
          direction="column"
          spacing="0.5rem"
          alignItems="flex-start"
          style={{ width: '100%', backgroundColor: 'white' }}
        >
          <FormikEffect onChange={response => handleChange(response as SatiInspector)} />

          {!isPrincipal && (
            <Stack.Item style={{ width: '100%' }}>
              <InspectorItemHeader edit={edit} onEdit={() => setEdit(true)} onDelete={handleDelete} />
            </Stack.Item>
          )}
          {!isPrincipal && (
            <Stack.Item style={{ width: '100%' }}>
              <Divider style={{ margin: 8 }} />
            </Stack.Item>
          )}
          <Stack.Item style={{ width: '100%' }}>
            <InspectorItemForm
              readOnly={!edit}
              isPrincipal={isPrincipal}
              values={values}
              excludedAgentIds={excludedAgentIds}
            />
          </Stack.Item>
          {!isPrincipal && edit && (
            <Stack.Item style={{ width: '100%' }}>
              <InspectorItemFooter onClose={handleClose} onSubmit={() => handleSubmit(values)} />
            </Stack.Item>
          )}
        </Stack>
      )}
    </Formik>
  )
}
export default InspectorItem
