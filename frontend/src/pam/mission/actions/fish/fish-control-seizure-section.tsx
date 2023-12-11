import React from 'react'
import {Accent, Button, Checkbox, Icon, Label, MultiRadio, Size, THEME} from '@mtes-mct/monitor-ui'
import {FishAction, SpeciesControl} from '../../../../types/fish-mission-types'
import Text from '../../../../ui/text'
import {Stack} from 'rsuite'
import {BOOLEAN_AS_OPTIONS, controlCheckMultiradioOptions} from '../action-control-fish'

interface FishControlSeizureSectionProps {
    action: FishAction
}

const FishControlSeizureSection: React.FC<FishControlSeizureSectionProps> = ({action}) => {
    return (
        <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
            <Stack.Item>
                <Label>Appréhension et déroutement du navire</Label>
            </Stack.Item>

            <Stack.Item style={{backgroundColor: THEME.color.white, width: '100%', padding: '1rem'}}>
                <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
                    <Stack.Item>
                        <Checkbox
                            readOnly={true}
                            name="seizureAndDiversion"
                            label="Appréhension et déroutement du navire"
                            checked={!!action.seizureAndDiversion}
                        />
                    </Stack.Item>
                    <Stack.Item style={{paddingLeft: '1.5rem'}}>
                        <Checkbox
                            readOnly={true}
                            name="hasSomeSpeciesSeized"
                            label="avec appréhension des espèces"
                            checked={!!action.hasSomeSpeciesSeized}
                        />
                    </Stack.Item>
                    <Stack.Item style={{paddingLeft: '1.5rem'}}>
                        <Checkbox
                            readOnly={true}
                            name="hasSomeGearsSeized"
                            label="avec appréhension des engins"
                            checked={!!action.hasSomeGearsSeized}
                        />
                    </Stack.Item>
                </Stack>
            </Stack.Item>

            <Stack.Item style={{backgroundColor: THEME.color.white, width: '100%', padding: '1rem'}}>
                <Label>Observations sur l’appréhension du navire / le déroutement</Label>
                <Text as="h3" weight="medium">
                    {!!action?.seizureAndDiversionComments ? action.seizureAndDiversionComments : 'Aucune observation'}
                </Text>
            </Stack.Item>
        </Stack>
    )
}

export default FishControlSeizureSection
