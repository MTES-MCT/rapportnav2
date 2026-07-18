import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { FC, useState } from 'react'
import { SatiInspector } from '../../../common/types/sati.ts'
import InspectorItemLayout from '../layout/inspector-item-layout.tsx'
import InspectorItem from './inspector-item.tsx'

interface InspectorItemNewProps {
  onSubmit: (inspector?: SatiInspector) => void
  excludedAgentIds?: number[]
}

const InspectorItemNew: FC<InspectorItemNewProps> = ({ onSubmit, excludedAgentIds }) => {
  const [showForm, setShowForm] = useState<boolean>(false)

  const handleSubmit = (inspector?: SatiInspector) => {
    setShowForm(false)
    onSubmit(inspector)
  }

  if (showForm) {
    return (
      <InspectorItemLayout
        title="Ajouter un inspecteur"
        inspectorItem={
          <InspectorItem
            readOnly={false}
            isPrincipal={false}
            onSubmit={handleSubmit}
            onClose={() => setShowForm(false)}
            excludedAgentIds={excludedAgentIds}
          />
        }
      />
    )
  }

  return (
    <Button
      Icon={Icon.Plus}
      isFullWidth={true}
      size={Size.NORMAL}
      title={'inspecteur'}
      role="add-inspector"
      accent={Accent.SECONDARY}
      data-testid="add-inspector"
      onClick={() => setShowForm(true)}
    >
      Ajouter un inspecteur
    </Button>
  )
}

export default InspectorItemNew
