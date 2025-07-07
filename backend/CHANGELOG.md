# Changelog

## [2.12.1](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.12.0...backend@v2.12.1) (2025-07-07)


### Bug Fixes

* **backend:** nullable start date ([0d768be](https://github.com/MTES-MCT/rapportnav2/commit/0d768be2bacd66f1f3ed0c450b70482ffdde6814))
* **backend:** nullable start date ([7e4d7a5](https://github.com/MTES-MCT/rapportnav2/commit/7e4d7a5c117fb7dcf5c50b86dfb345c8db64dfde))

## [2.12.0](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.10...backend@v2.12.0) (2025-07-07)


### Features

* **backend:** missionAction rename mission_id_uuid to owner_id ([7bb3090](https://github.com/MTES-MCT/rapportnav2/commit/7bb309001369c9d82ff12a148d601a85b572202f))
* **backend:** missionAction rename mission_id_uuid to owner_id ([76727be](https://github.com/MTES-MCT/rapportnav2/commit/76727be99b035166c3f82e662ec44094ccba3780))
* **backend:** save general infos in mission creation ([ef78fa0](https://github.com/MTES-MCT/rapportnav2/commit/ef78fa0ed8f5f016f0de92e96466c07f4e1e148c))
* **backend:** separate create and update generalInfos ([257a213](https://github.com/MTES-MCT/rapportnav2/commit/257a2136795e899f009e7437cbd3cb61d7eca620))
* **backend:** update controller signature to match data definition ([b076a53](https://github.com/MTES-MCT/rapportnav2/commit/b076a532191f250cee18565328b191042e8792b0))
* **backend:** update jdp backend ([87fd3aa](https://github.com/MTES-MCT/rapportnav2/commit/87fd3aa9789d1e030dba21b0bd146db892a12cf9))
* **backend:** update missionCrew saving process ([d8e48a3](https://github.com/MTES-MCT/rapportnav2/commit/d8e48a38fd8c5097c5d767442eeae5fcc9a82c73))


### Bug Fixes

* **backend:** build ([cecda8e](https://github.com/MTES-MCT/rapportnav2/commit/cecda8e28eb4d1d9a135039a3a422eadfefa5475))
* **backend:** fix test un updategeneralInfo ([fa6b86b](https://github.com/MTES-MCT/rapportnav2/commit/fa6b86b09bcb8dab0fd92c8a1b60bc0d2bb5e884))
* **backend:** get Owner Id in mission action entity ([61788b9](https://github.com/MTES-MCT/rapportnav2/commit/61788b97c4046c75a1130512f9eb468e6586a613))
* **backend:** mission action controller ([04e1735](https://github.com/MTES-MCT/rapportnav2/commit/04e17350254646290760ba22a2cbd872f156025a))
* **backend:** tests ([aaeb7ba](https://github.com/MTES-MCT/rapportnav2/commit/aaeb7bad76cb8a00dcbc4992bbe4a7aa63a7b374))
* set endDateTimeUtc mandatory for Rescue Action ([3442260](https://github.com/MTES-MCT/rapportnav2/commit/34422607a3d94570a6d9f02483de150fff05ad00))

## [2.11.10](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.9...backend@v2.11.10) (2025-06-24)


### Miscellaneous Chores

* **backend:** Synchronize group-some-associated-services versions

## [2.11.9](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.8...backend@v2.11.9) (2025-06-24)


### Bug Fixes

* **v1:** make startDateTime optional in graphql for Env Actions ([4f4a6f0](https://github.com/MTES-MCT/rapportnav2/commit/4f4a6f048ebca6e1b765d01afebf476b362888c7))

## [2.11.8](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.7...backend@v2.11.8) (2025-06-23)


### Miscellaneous Chores

* **backend:** Synchronize group-some-associated-services versions

## [2.11.7](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.6...backend@v2.11.7) (2025-06-23)


### Bug Fixes

* **v1:** observationsByUnit on FishAction ([586b863](https://github.com/MTES-MCT/rapportnav2/commit/586b8631dbcc4827937c1dacdb269661ce7deb4e))

## [2.11.6](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.5...backend@v2.11.6) (2025-06-23)


### Bug Fixes

* add more logging PAM v1 ([250bdbe](https://github.com/MTES-MCT/rapportnav2/commit/250bdbe33ac637d42d60c5db8831362304a2ab71))

## [2.11.5](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.4...backend@v2.11.5) (2025-06-23)


### Bug Fixes

* add more logging for NullPointer caused by data not fully retrieved ([#735](https://github.com/MTES-MCT/rapportnav2/issues/735)) ([8319177](https://github.com/MTES-MCT/rapportnav2/commit/83191774ba4989a2cf74ea1ada0230dd0ad01a1d))
* bump spring boot ([886b282](https://github.com/MTES-MCT/rapportnav2/commit/886b282671bd1c2083dfb5b7c198cca8c8c66c4f))

## [2.11.4](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.3...backend@v2.11.4) (2025-06-20)


### Bug Fixes

* lower tomcat-embed-core version ([d387e90](https://github.com/MTES-MCT/rapportnav2/commit/d387e90c45410261b480af01e69bb9908c7b15f7))

## [2.11.3](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.2...backend@v2.11.3) (2025-06-20)


### Bug Fixes

* force tomcat-embed-core version ([ac3234e](https://github.com/MTES-MCT/rapportnav2/commit/ac3234e231da416cb32e37f5496f50dd509f7fb5))

## [2.11.2](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.1...backend@v2.11.2) (2025-06-20)


### Bug Fixes

* bump spring-boot ([95f7fac](https://github.com/MTES-MCT/rapportnav2/commit/95f7fac6f1d40c61e983bc2f60e647da831a054c))

## [2.11.1](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.11.0...backend@v2.11.1) (2025-06-19)


### Bug Fixes

* force bump tomcat-embed-core for secu reasons ([b7bcc03](https://github.com/MTES-MCT/rapportnav2/commit/b7bcc03f411827484b6aef67196ee7eeb0c4d6f0))

## [2.11.0](https://github.com/MTES-MCT/rapportnav2/compare/backend@v2.10.1...backend@v2.11.0) (2025-06-19)


### Features

* **backend:** Update cross control computation process ([28134b2](https://github.com/MTES-MCT/rapportnav2/commit/28134b21f19c34e7826034f8ebd2292e86528eee))
* **backend:** update few backend process ([5ce5f97](https://github.com/MTES-MCT/rapportnav2/commit/5ce5f97006eba4068e127b5c3c701564d3a43b96))
* **backend:** using patch instead of update ([e576dd1](https://github.com/MTES-MCT/rapportnav2/commit/e576dd1f9f8345519b635108006d7819289cf165))
* **backend:** using patch instead of update ([7cdab63](https://github.com/MTES-MCT/rapportnav2/commit/7cdab63c16342c9a890d00412600fde0d6b2e999))
* **frontend:** cross control display ([f83d9ae](https://github.com/MTES-MCT/rapportnav2/commit/f83d9ae52083678fd77633a9e6e877efd01138db))


### Bug Fixes

* fix PAM crew updates ([594134e](https://github.com/MTES-MCT/rapportnav2/commit/594134e5e60ddc37b34353644a3ace75672909a0))
* fix setting status correctly whether status action or not ([07a5d8c](https://github.com/MTES-MCT/rapportnav2/commit/07a5d8c597b99bebb9891409762b7ac0e177069d))
* fix setting status correctly whether status action or not ([feb96c1](https://github.com/MTES-MCT/rapportnav2/commit/feb96c15fa1df7db0caf7b4e126d72da69285041))
* release-please config update for build.gradle.kts ([fb2f40f](https://github.com/MTES-MCT/rapportnav2/commit/fb2f40fc9490d94041721da813cb98922f019b27))
