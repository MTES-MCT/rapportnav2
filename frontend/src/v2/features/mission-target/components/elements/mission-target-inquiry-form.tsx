import { ControlType } from '@common/types/control-types'
import { Accent, Button, Icon, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import { useState } from 'react'
import { Stack } from 'rsuite'
import { Target } from '../../../common/types/target-types'
import MissionInfractionEnvForm from '../../../mission-infraction/components/elements/mission-infraction-env-form'
import { TargetInfraction } from '../../../mission-infraction/hooks/use-infraction-env-form'
import { useTarget } from '../../hooks/use-target'

export interface MissionTargetInquiryFormProps {
  name: string
  isDisabled?: boolean
  fieldFormik: FieldProps<Target>
  availableControlTypes?: ControlType[]
}

const MissionTargetInquiryForm: React.FC<MissionTargetInquiryFormProps> = ({
  name,
  isDisabled,
  fieldFormik,
  availableControlTypes
}) => {
  const [showForm, setShowForm] = useState<boolean>(false)
  const { fromInputToFieldValue } = useTarget()

  const handleShow = () => setShowForm(true)
  const handleClose = () => setShowForm(false)

  const handleSubmit = async (value?: TargetInfraction) => {
    if (!value) return
    const valueToSubmit = fromInputToFieldValue(value, fieldFormik.field.value)
    await fieldFormik.form.setFieldValue(name, valueToSubmit)
    handleClose()
  }

  return (
    <Stack style={{ width: '100%' }}>
      {showForm && (
        <Stack.Item
          style={{
            width: '100%',
            padding: '1rem',
            marginBottom: '1rem',
            backgroundColor: THEME.color.white
          }}
        >
          <MissionInfractionEnvForm
            editTarget={false}
            editInfraction={true}
            onClose={handleClose}
            onSubmit={handleSubmit}
            availableControlTypes={availableControlTypes}
            value={{ target: fieldFormik.field.value } as TargetInfraction}
          />
        </Stack.Item>
      )}
      {!showForm && (
        <Stack.Item style={{ width: '100%' }}>
          <Button
            Icon={Icon.Plus}
            size={Size.NORMAL}
            onClick={handleShow}
            disabled={isDisabled}
            accent={Accent.SECONDARY}
            style={{ width: 'inherit' }}
            role={'target-inquiry-add-button'}
          >
            {'Ajouter une infraction'}
          </Button>
        </Stack.Item>
      )}
    </Stack>
  )
}

export default MissionTargetInquiryForm
