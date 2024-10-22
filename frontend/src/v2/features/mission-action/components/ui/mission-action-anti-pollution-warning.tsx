import Text from '@common/components/ui/text'

const MissionActionAntiPollutionWarning: React.FC = () => {
  return (
    <Text as="h4" weight="normal" fontStyle="italic">
      Ces actions conernent des opérations menées en coordination avec le CROSS lors de la suspicion / détection /
      signalement de pollution en mer (rejets illicites...)
      <br />
      Les contrôles effectués de manière autonomes sont à rapporter au CACEM
      <br />
      En cas de doute, appelez le CACEM
    </Text>
  )
}

export default MissionActionAntiPollutionWarning
