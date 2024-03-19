import React from 'react'
import { Col, FlexboxGrid, Loader, Stack } from 'rsuite'
import { Accent, Button, Icon, Size, THEME } from '@mtes-mct/monitor-ui'
import MissionsList from './missions-list'
import { Link } from 'react-router-dom'
import Text from "../../ui/text.tsx";
import useMissions from "./use-missions.tsx";
import { GET_MISSION_BY_ID } from "../mission/use-mission-by-id.tsx";


const Missions: React.FC = () => {
    const {loading, data: missions, error, client} = useMissions()

    const prefetchMission = async (missionId: string) => {
        await client.query({
            query: GET_MISSION_BY_ID,
            variables: {missionId}
        })
    }

    if (loading) {
        return (
            <Loader center={true} size={'md'} vertical={true}
                    content={<Text as={'h3'}>Missions en cours de chargement</Text>}/>
        )
    }

    if (error) {
        return (
            <Stack direction="column" justifyContent="center" style={{width: '100%'}}>
                <Stack.Item alignSelf={"center"} style={{width: '100%'}}>
                    <Stack direction="column" justifyContent="center" spacing={'1rem'}>
                        <Stack.Item alignSelf={"center"} style={{maxWidth: '50%'}}>
                            <Text as={'h3'}>Une erreur s'est produite lors du chargement de vos missions. Si le problème
                                persiste,
                                veuillez contacter l'équipe RapportNav.</Text>
                        </Stack.Item>
                        <Stack.Item alignSelf={"center"} style={{maxWidth: '50%'}}>
                            <Text as={'h3'} color={THEME.color.lightGray}
                                  fontStyle={"italic"}>Erreur: {error?.message}</Text>
                        </Stack.Item>
                    </Stack>
                </Stack.Item>
            </Stack>
        )
    }

    if (missions) {
        return (
            <FlexboxGrid
                // align="middle"
                justify="center"
                style={{padding: '4rem 2rem', display: 'flex', flex: 1}}
            >
                <FlexboxGrid.Item as={Col} colspan={24} xxl={16}>
                    <Stack direction="column" alignItems="flex-start">
                        <Stack.Item>
                            <h3>Missions</h3>
                        </Stack.Item>

                        <Stack.Item style={{paddingTop: '2rem'}}>
                            <h4>Mes rapports de mission</h4>
                        </Stack.Item>

                        <Stack.Item alignSelf="flex-end" style={{paddingTop: '2rem'}}>
                            <Link to={`/pam/missions/0`}>
                                <Button accent={Accent.PRIMARY} Icon={Icon.Plus} size={Size.NORMAL} disabled={true}>
                                    Créer un rapport de mission
                                </Button>
                            </Link>
                        </Stack.Item>

                        <Stack.Item style={{paddingTop: '2rem', width: '100%'}}>
                            {loading ? (
                                <Stack justifyContent="center" style={{marginTop: '5rem'}}>
                                    <Stack.Item>
                                        <Loader/>
                                    </Stack.Item>
                                </Stack>
                            ) : (
                                <MissionsList missions={missions} prefetchMission={prefetchMission}/>
                            )}
                        </Stack.Item>
                    </Stack>
                </FlexboxGrid.Item>
            </FlexboxGrid>
        )
    }

    return null
}

export default Missions
