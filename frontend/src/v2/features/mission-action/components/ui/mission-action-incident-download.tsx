import Text from '@common/components/ui/text'
import { FormikCheckbox, Icon } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'

const MissionActioIncidentDonwload: React.FC = () => {
  const downloadFile = () => {
    const pdfUrl = '/src/assets/files/Annexe11_modèle_fiche_incident.odt'
    const link = document.createElement('a')
    link.href = pdfUrl
    link.download = 'Annexe 11 modèle fiche incident.odt'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }
  return (
    <Stack direction="column">
      <Stack.Item style={{ width: '100%' }}>
        <FormikCheckbox
          isLight
          style={{ marginTop: 8 }}
          name="incidentDuringOperation"
          label="L'opération à donné lieu à un incident (utilisation d'arme(s)/menottage)"
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%', marginTop: 4 }}>
        <Stack
          direction="row"
          alignItems="flex-start"
          onClick={downloadFile}
          style={{ cursor: 'pointer', width: 'fit-content' }}
        >
          <Icon.Link style={{ width: 12, marginRight: 4 }} />
          <Text as="h4" decoration="underline">
            Template fiche incident
          </Text>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionActioIncidentDonwload
