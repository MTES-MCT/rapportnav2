package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock

object AdministrationStubs {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    fun configureStubs(wireMockServer: WireMockServer) {

        val json = "[\n" +
            "        {\n" +
            "            \"id\": 1001,\n" +
            "            \"controlUnitIds\": [1150, 1232, 1233, 1151],\n" +
            "            \"controlUnits\": [\n" +
            "                {\"id\": 1150, \"areaNote\": null, \"administrationId\": 1001, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Pm 509 - Valbelle (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1232, \"areaNote\": null, \"administrationId\": 1001, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Pm 503 Sparfell Vor (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1233, \"areaNote\": null, \"administrationId\": 1001, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Pm 504 Startijenn (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1151, \"areaNote\": null, \"administrationId\": 1001, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Pm 510 - Augustine (historique)\", \"termsNote\": null}\n" +
            "            ],\n" +
            "            \"isArchived\": true,\n" +
            "            \"name\": \"-\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 1007,\n" +
            "            \"controlUnitIds\": [10328, 1356, 1569, 1652, 1644, 1654, 1645],\n" +
            "            \"controlUnits\": [\n" +
            "                {\"id\": 10328, \"areaNote\": null, \"administrationId\": 1007, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Unité AECP\", \"termsNote\": \"L'Ocean Guardian et Protector vont parfois en Atlantique, parfois en Mer du Nord, ce qui donne 2 adresses génériques EFCA : efca-western@efca.europa.eu pour l'Atlantique et efca-jdp-nor@efca.europa.eu pour la Mer du Nord. \"},\n" +
            "                {\"id\": 1356, \"areaNote\": null, \"administrationId\": 1007, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Lundy Sentinel (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1569, \"areaNote\": null, \"administrationId\": 1007, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Autre Moyen (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1652, \"areaNote\": null, \"administrationId\": 1007, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Ocean Protector (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1644, \"areaNote\": null, \"administrationId\": 1007, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Ocean Guardian (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1654, \"areaNote\": null, \"administrationId\": 1007, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Fs Floreal [2] (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1645, \"areaNote\": null, \"administrationId\": 1007, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Ocean Sentinel (historique)\", \"termsNote\": null}\n" +
            "            ],\n" +
            "            \"isArchived\": false,\n" +
            "            \"name\": \"AECP\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 1004,\n" +
            "            \"controlUnitIds\": [10475, 10476, 10478, 10488, 10507, 10006, 10053, 10021, 10037, 10051, 10089, 10091, 10095, 10114, 10116, 10117, 10129, 10134, 10161, 10175, 10180, 1380, 10216, 10228, 10249, 10271, 10276, 10281, 1389, 10315, 10335, 10340, 10363, 10366, 10370, 10376, 10384, 10385, 10397, 1357, 10406, 1388, 1390, 1391, 1392, 1393, 1394, 10450, 1395, 1396, 1397, 1398, 1399, 1401, 1403, 10621, 1404, 1405, 1406, 1407, 1408, 1409, 1410, 1411, 1412, 1413, 1414, 1416, 1417, 1418, 1419, 1420, 1400, 1402, 1415],\n" +
            "            \"controlUnits\": [\n" +
            "                {\"id\": 10475, \"areaNote\": \"Périmètre de la réserve naturelle de Scandola\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de Scandola\", \"termsNote\": null},\n" +
            "                {\"id\": 10476, \"areaNote\": \"Périmètre de la réserve naturelle de l’Amana\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de L'Amana\", \"termsNote\": null},\n" +
            "                {\"id\": 10478, \"areaNote\": \"Périmètre de la réserve naturelle du Marais de Séné\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle du Marais de Séné\", \"termsNote\": null},\n" +
            "                {\"id\": 10488, \"areaNote\": \"Périmètre de la réserve naturelle d’Iroise\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle d'Iroise\", \"termsNote\": null},\n" +
            "                {\"id\": 10507, \"areaNote\": \"Pérmètre de la réserve naturelle du courant d’Huchet\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle du Courant Huchet\", \"termsNote\": null},\n" +
            "                {\"id\": 10006, \"areaNote\": \"Périmètre de la réserve naturelle de Saint-Martin\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle Nationale de Saint-Martin\", \"termsNote\": null},\n" +
            "                {\"id\": 10053, \"areaNote\": \"Périmètre de la réserve naturelle des l’estuaire de la Seine\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de L'Estuaire de La Seine\", \"termsNote\": null},\n" +
            "                {\"id\": 10021, \"areaNote\": \"Périmètre de la réserve naturelle des Glorieuses\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle des Glorieuses\", \"termsNote\": null},\n" +
            "                {\"id\": 10037, \"areaNote\": \"Périmètre de la réserve naturelle des Iles du Cap Corse\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle des Iles du Cap Corse\", \"termsNote\": null},\n" +
            "                {\"id\": 10051, \"areaNote\": \"Périmètre de la réserve naturelle du platier d’Oye\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle du Platier D'Oye\", \"termsNote\": null},\n" +
            "                {\"id\": 10089, \"areaNote\": \"Périmètre de la réserve naturelle de la baie de Somme\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de la Baie de Somme\", \"termsNote\": null},\n" +
            "                {\"id\": 10091, \"areaNote\": \"Périmètre de la réserve naturelle des TAAF\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle des Terres Australes\", \"termsNote\": null},\n" +
            "                {\"id\": 10095, \"areaNote\": \"Périmètre de la réserve naturelle Marine du Prêcheur\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle Régionale Marine du Prêcheur\", \"termsNote\": null},\n" +
            "                {\"id\": 10114, \"areaNote\": \"Saint-Barthélémy\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle Nationale de Saint-Barthélémy\", \"termsNote\": null},\n" +
            "                {\"id\": 10116, \"areaNote\": \"Périmètre de la réserve naturelle de la baie de l’Aiguillon\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de La Baie de L'Aiguillon (Charente-Maritime)\", \"termsNote\": null},\n" +
            "                {\"id\": 10117, \"areaNote\": \"Périmètre de la réserve de Crozon\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de Crozon\", \"termsNote\": null},\n" +
            "                {\"id\": 10129, \"areaNote\": \"Périmètre de la réserve naturelle de Moeze-Oléron\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de Moëze-Oléron\", \"termsNote\": null},\n" +
            "                {\"id\": 10134, \"areaNote\": \"Archipel des Glénan\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve naturelle Saint-Nicolas des Glénan\", \"termsNote\": null},\n" +
            "                {\"id\": 10161, \"areaNote\": \"Périmètre de la réserve naturelle des îlets de Petite Terre\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de l’île de la Petite-Terre\", \"termsNote\": null},\n" +
            "                {\"id\": 10175, \"areaNote\": \"Périmètre de la réserve naturelle de la baie de Canche\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de la Baie de Canche\", \"termsNote\": null},\n" +
            "                {\"id\": 10180, \"areaNote\": \"Périmètre de la réserve naturelle des prés salés d’Arès et de Lège-Cap Ferret\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle des près salés d'Arès et de Lège-Cap Ferret\", \"termsNote\": null},\n" +
            "                {\"id\": 1380, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle 7 Iles (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 10216, \"areaNote\": \"Périmètre de la réserve de la Désirade\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de La Désirade\", \"termsNote\": null},\n" +
            "                {\"id\": 10228, \"areaNote\": \"Périmètre de la réserve naturelle du domaine de Beauguillot\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle du Domaine de Beauguillot\", \"termsNote\": null},\n" +
            "                {\"id\": 10249, \"areaNote\": \"Périmètre de la réserve naturelle du Casse de La Belle Henriette\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle du Casse de La Belle Henriette\", \"termsNote\": null},\n" +
            "                {\"id\": 10271, \"areaNote\": \"Périmètre de la réserve naturelle marine de la Réunion\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle Marine de La Réunion\", \"termsNote\": null},\n" +
            "                {\"id\": 10276, \"areaNote\": \"Périmètre de la réserve naturelle des sept-îles\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle des 7 Iles\", \"termsNote\": null},\n" +
            "                {\"id\": 10281, \"areaNote\": \"île de Groix\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle François Le Bail\", \"termsNote\": null},\n" +
            "                {\"id\": 1389, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Platier D'Oye (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 10315, \"areaNote\": \"Périmètre de la réserve naturelle du Sillon de Talbert\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle Régionale du Sillon du Talbert\", \"termsNote\": null},\n" +
            "                {\"id\": 10335, \"areaNote\": \"Périmètre de la réserve naturelle des Bouches de Bonifacio\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle des Bouches de Bonifacio\", \"termsNote\": null},\n" +
            "                {\"id\": 10340, \"areaNote\": \"Périmètre de la réserve naturelle des Ilets de Sainte-Anne\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle des Ilets de Sainte-Anne\", \"termsNote\": null},\n" +
            "                {\"id\": 10363, \"areaNote\": \"Périmètre de la réserve naturelle de Cerbère-Banyuls\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve naturelle de Cerbère-Banyuls\", \"termsNote\": null},\n" +
            "                {\"id\": 10366, \"areaNote\": \"Périmètre de la réserve du Cap Romain\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle du Cap Romain\", \"termsNote\": null},\n" +
            "                {\"id\": 10370, \"areaNote\": \"Périmètre de la réserve naturelle de l’îlot Mbouzi\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de L'Ilot M'Bouzi\", \"termsNote\": null},\n" +
            "                {\"id\": 10376, \"areaNote\": \"Périmètre de la réserve naturellede la baie de l’Aiguillon\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de La Baie de L'Aiguillon (Vendée)\", \"termsNote\": null},\n" +
            "                {\"id\": 10384, \"areaNote\": \"Périmètre de la réserve naturelle de Kaw-Roura\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de Kaw-Roura\", \"termsNote\": null},\n" +
            "                {\"id\": 10385, \"areaNote\": \"Périmètre de la réserve naturelle du banc d’Arguin\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle du Banc D'Arguin\", \"termsNote\": null},\n" +
            "                {\"id\": 10397, \"areaNote\": \"Périmètre de la réserve naturelle de l’île du Grand Connetable\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de l'Ile du Grand Connétable\", \"termsNote\": null},\n" +
            "                {\"id\": 1357, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Association Titè Rnn Petite-Terre (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 10406, \"areaNote\": \"Fiers d’Ars\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de Lilleau des Niges\", \"termsNote\": null},\n" +
            "                {\"id\": 1388, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Cerbère Banyuls (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1390, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Baie De Canche (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1391, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Baie De Somme (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1392, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De L'Esturaire De La Seine (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1393, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Du Cap Romain (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1394, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Du Domaine De Beauguillot (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 10450, \"areaNote\": \"Périmètre de la réserve naturelle de Saint-Brieuc\", \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de la Baie de Saint Brieuc\", \"termsNote\": null},\n" +
            "                {\"id\": 1395, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De La Baie De Saint Brieuc (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1396, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle D'Iroise (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1397, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle François Le Bail (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1398, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Du Marais De Séné (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1399, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Du Casse De La Belle Henriette (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1401, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Régionale Du Sillon Du Talbert (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1403, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De Moëze-Oléron (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 10621, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": false, \"name\": \"Réserve Naturelle de la Baie de Saint Brieuc (Ambassadeurs bénévoles VivArmor)\", \"termsNote\": null},\n" +
            "                {\"id\": 1404, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Près Salés D'Ares Lèges Cap Ferr (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1405, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Du Banc D'Arguin (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1406, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Du Courant Huchet (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1407, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Des Iles Du Cap Corse (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1408, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Des Bouches De Bonifacio (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1409, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De Scandola (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1410, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De La Désirade (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1411, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Des Ilets De Sainte-Anne (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1412, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Régionale Marione Du Prêcheur (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1413, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Nationale Saint-Barthélémy (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1414, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Nationale Saint-Martin (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1416, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De Kaw-Roura (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1417, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De L'Amana (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1418, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle  De L'Ilot M'Bouzi (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1419, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Marine De La Réunion (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1420, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle Des Terres Australes (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1400, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De La Baie De L'Aiguillon (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1402, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De Lilleau Des Niges (historique)\", \"termsNote\": null},\n" +
            "                {\"id\": 1415, \"areaNote\": null, \"administrationId\": 1004, \"departmentAreaInseeCode\": null, \"isArchived\": true, \"name\": \"Réserve Naturelle De L'Ile Du Grand Connétable (historique)\", \"termsNote\": null}\n" +
            "            ],\n" +
            "            \"isArchived\": false,\n" +
            "            \"name\": \"Réserves Naturelles\"\n" +
            "        }]"

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/api/v1/administrations"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )
    }
}
