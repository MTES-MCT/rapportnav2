import Text from '@common/components/ui/text'
import { Button, Message, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

interface EstablishmentWarningProps {
  onYes: () => void
  onNo: () => void
}

export const EstablishmentWarning: FC<EstablishmentWarningProps> = ({ onYes, onNo }) => (
  <Message level="WARNING">
    <Text as="h3" color={THEME.color.arylideYellow}>
      <strong>
        Je confirme que l’établissement n’a pas été trouvé à partir de son <u>SIRET</u>
      </strong>
    </Text>
    <span>
      Attention, si l’entreprise est <strong>française et déclarée, elle existe forcément</strong> dans l’annuaire
      entreprises
      <br />
      de l’Etat. Le champ libre n’est à utiliser que dans le cas où l’entreprise est étrangère ou non déclarée.
    </span>
    <div style={{ width: '100%', marginTop: '16px', display: 'flex', flexDirection: 'row' }}>
      <div style={{ width: '50%', paddingRight: '4px' }}>
        <Button accent="WARNING" style={{ width: '100%' }} onClick={onYes}>
          Oui, je dois ajouter l’établissement manuellement
        </Button>
      </div>
      <div style={{ width: '50%', paddingLeft: '4px' }}>
        <Button accent="WARNING" style={{ width: '100%' }} onClick={onNo}>
          Non, je recherche à nouveau
        </Button>
      </div>
    </div>
  </Message>
)
