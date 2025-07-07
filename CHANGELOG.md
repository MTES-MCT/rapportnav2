# Changelog

## [2.12.1](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.12.0...docker-container@v2.12.1) (2025-07-07)


### Bug Fixes

* **backend:** nullable start date ([0d768be](https://github.com/MTES-MCT/rapportnav2/commit/0d768be2bacd66f1f3ed0c450b70482ffdde6814))
* **backend:** nullable start date ([7e4d7a5](https://github.com/MTES-MCT/rapportnav2/commit/7e4d7a5c117fb7dcf5c50b86dfb345c8db64dfde))

## [2.12.0](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.10...docker-container@v2.12.0) (2025-07-07)


### Features

* **backend:** missionAction rename mission_id_uuid to owner_id ([7bb3090](https://github.com/MTES-MCT/rapportnav2/commit/7bb309001369c9d82ff12a148d601a85b572202f))
* **backend:** missionAction rename mission_id_uuid to owner_id ([76727be](https://github.com/MTES-MCT/rapportnav2/commit/76727be99b035166c3f82e662ec44094ccba3780))
* **backend:** save general infos in mission creation ([ef78fa0](https://github.com/MTES-MCT/rapportnav2/commit/ef78fa0ed8f5f016f0de92e96466c07f4e1e148c))
* **backend:** separate create and update generalInfos ([257a213](https://github.com/MTES-MCT/rapportnav2/commit/257a2136795e899f009e7437cbd3cb61d7eca620))
* **backend:** update controller signature to match data definition ([b076a53](https://github.com/MTES-MCT/rapportnav2/commit/b076a532191f250cee18565328b191042e8792b0))
* **backend:** update jdp backend ([87fd3aa](https://github.com/MTES-MCT/rapportnav2/commit/87fd3aa9789d1e030dba21b0bd146db892a12cf9))
* **backend:** update missionCrew saving process ([d8e48a3](https://github.com/MTES-MCT/rapportnav2/commit/d8e48a38fd8c5097c5d767442eeae5fcc9a82c73))
* **frontend:** creation mission office report and external reinforcement ([68bf286](https://github.com/MTES-MCT/rapportnav2/commit/68bf286a4cc274bbe367619e8dfed54a7b9c3b36))
* **frontend:** from number to string missionId ([4c74428](https://github.com/MTES-MCT/rapportnav2/commit/4c74428bd13d0cde5dddd8887642d85a1b860b4b))
* **frontend:** general infos isJdp ([9224761](https://github.com/MTES-MCT/rapportnav2/commit/9224761ab65b9ce1b3f0fd7411c58debefd9a8a8))
* **frontend:** matching signature of data object ([c14b730](https://github.com/MTES-MCT/rapportnav2/commit/c14b7307fb2aabc6b3be606731dcaa03022f920a))


### Bug Fixes

* align title on mission list page ([11e7627](https://github.com/MTES-MCT/rapportnav2/commit/11e76270c7d55520441ab5461bad3a52ca8566bc))
* **backend:** build ([cecda8e](https://github.com/MTES-MCT/rapportnav2/commit/cecda8e28eb4d1d9a135039a3a422eadfefa5475))
* **backend:** fix test un updategeneralInfo ([fa6b86b](https://github.com/MTES-MCT/rapportnav2/commit/fa6b86b09bcb8dab0fd92c8a1b60bc0d2bb5e884))
* **backend:** get Owner Id in mission action entity ([61788b9](https://github.com/MTES-MCT/rapportnav2/commit/61788b97c4046c75a1130512f9eb468e6586a613))
* **backend:** mission action controller ([04e1735](https://github.com/MTES-MCT/rapportnav2/commit/04e17350254646290760ba22a2cbd872f156025a))
* **backend:** tests ([aaeb7ba](https://github.com/MTES-MCT/rapportnav2/commit/aaeb7bad76cb8a00dcbc4992bbe4a7aa63a7b374))
* don't trigger update action if action has not changed ([1dca62f](https://github.com/MTES-MCT/rapportnav2/commit/1dca62f0b587f265a56d2a1b37912da110889068))
* **frontend:** set owner id in mission action ([7df38e9](https://github.com/MTES-MCT/rapportnav2/commit/7df38e96df95fb82f7fa7b84add61ea62b26789c))
* **frontend:** test ([fe60921](https://github.com/MTES-MCT/rapportnav2/commit/fe60921dc549d805d6f62caf626f7e55f83ca441))
* set endDateTimeUtc mandatory for Rescue Action ([3442260](https://github.com/MTES-MCT/rapportnav2/commit/34422607a3d94570a6d9f02483de150fff05ad00))
* set validation for non negative numbers ([84a32d0](https://github.com/MTES-MCT/rapportnav2/commit/84a32d032f6856eaa98757f2b3c2bba918697c2e))

## [2.11.10](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.9...docker-container@v2.11.10) (2025-06-24)


### Bug Fixes

* fix validation on generalInfo PAM v2 ([55db70f](https://github.com/MTES-MCT/rapportnav2/commit/55db70f9c5f80992cc6ab2611f31675bbeb62ffd))

## [2.11.9](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.8...docker-container@v2.11.9) (2025-06-24)


### Bug Fixes

* **v1:** make startDateTime optional in graphql for Env Actions ([4f4a6f0](https://github.com/MTES-MCT/rapportnav2/commit/4f4a6f048ebca6e1b765d01afebf476b362888c7))
* **v1:** reduce observationsByUnit length on form ([db38c54](https://github.com/MTES-MCT/rapportnav2/commit/db38c544ec4b0f6adbc8cfa1d9e25aae19ccf449))
* **v2:** make generalInfo display correctly in PAM ([312bfb3](https://github.com/MTES-MCT/rapportnav2/commit/312bfb39735e4e6fac16e4c4ef6d71dc442f7fbc))

## [2.11.8](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.7...docker-container@v2.11.8) (2025-06-23)


### Bug Fixes

* **v1:** observationsByUnit on FishAction ([853e07b](https://github.com/MTES-MCT/rapportnav2/commit/853e07b2c657898d4f349d04ec9500220bfe0310))

## [2.11.7](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.6...docker-container@v2.11.7) (2025-06-23)


### Bug Fixes

* **v1:** observationsByUnit on FishAction ([586b863](https://github.com/MTES-MCT/rapportnav2/commit/586b8631dbcc4827937c1dacdb269661ce7deb4e))

## [2.11.6](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.5...docker-container@v2.11.6) (2025-06-23)


### Bug Fixes

* add more logging PAM v1 ([250bdbe](https://github.com/MTES-MCT/rapportnav2/commit/250bdbe33ac637d42d60c5db8831362304a2ab71))

## [2.11.5](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.4...docker-container@v2.11.5) (2025-06-23)


### Bug Fixes

* add more logging for NullPointer caused by data not fully retrieved ([#735](https://github.com/MTES-MCT/rapportnav2/issues/735)) ([8319177](https://github.com/MTES-MCT/rapportnav2/commit/83191774ba4989a2cf74ea1ada0230dd0ad01a1d))
* bump spring boot ([886b282](https://github.com/MTES-MCT/rapportnav2/commit/886b282671bd1c2083dfb5b7c198cca8c8c66c4f))

## [2.11.4](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.3...docker-container@v2.11.4) (2025-06-20)


### Bug Fixes

* lower tomcat-embed-core version ([d387e90](https://github.com/MTES-MCT/rapportnav2/commit/d387e90c45410261b480af01e69bb9908c7b15f7))

## [2.11.3](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.2...docker-container@v2.11.3) (2025-06-20)


### Bug Fixes

* force tomcat-embed-core version ([ac3234e](https://github.com/MTES-MCT/rapportnav2/commit/ac3234e231da416cb32e37f5496f50dd509f7fb5))

## [2.11.2](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.1...docker-container@v2.11.2) (2025-06-20)


### Bug Fixes

* bump spring-boot ([95f7fac](https://github.com/MTES-MCT/rapportnav2/commit/95f7fac6f1d40c61e983bc2f60e647da831a054c))

## [2.11.1](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.11.0...docker-container@v2.11.1) (2025-06-19)


### Bug Fixes

* force bump tomcat-embed-core for secu reasons ([b7bcc03](https://github.com/MTES-MCT/rapportnav2/commit/b7bcc03f411827484b6aef67196ee7eeb0c4d6f0))

## [2.11.0](https://github.com/MTES-MCT/rapportnav2/compare/docker-container@v2.10.1...docker-container@v2.11.0) (2025-06-19)


### Features

* add updating version of .gitlab-ci.yml for release-please ([10c29cc](https://github.com/MTES-MCT/rapportnav2/commit/10c29ccb1dc1a8bf5ce1ab1e9251b8f44f054aec))
* **backend:** Update cross control computation process ([28134b2](https://github.com/MTES-MCT/rapportnav2/commit/28134b21f19c34e7826034f8ebd2292e86528eee))
* **backend:** update few backend process ([5ce5f97](https://github.com/MTES-MCT/rapportnav2/commit/5ce5f97006eba4068e127b5c3c701564d3a43b96))
* **backend:** using patch instead of update ([e576dd1](https://github.com/MTES-MCT/rapportnav2/commit/e576dd1f9f8345519b635108006d7819289cf165))
* **backend:** using patch instead of update ([7cdab63](https://github.com/MTES-MCT/rapportnav2/commit/7cdab63c16342c9a890d00412600fde0d6b2e999))
* **frontend:** common element ([9abbd81](https://github.com/MTES-MCT/rapportnav2/commit/9abbd81017dbf02b49333442818de186f810445f))
* **frontend:** component mission action and target ([af183b9](https://github.com/MTES-MCT/rapportnav2/commit/af183b9264ddf29facb3e437bac11a502679551d))
* **frontend:** cross control display ([f83d9ae](https://github.com/MTES-MCT/rapportnav2/commit/f83d9ae52083678fd77633a9e6e877efd01138db))
* **v2:** add tz conversion in header ([46e2a51](https://github.com/MTES-MCT/rapportnav2/commit/46e2a519c2e59d8e4b5f12941fcb628c2263e527))
* **v2:** switch all dates to UTC ([1954936](https://github.com/MTES-MCT/rapportnav2/commit/19549364ca65d95b316257373ae6649be6d6b6b9))


### Bug Fixes

* add more permissions to release-please action ([80957de](https://github.com/MTES-MCT/rapportnav2/commit/80957de488421f47a019739d9127bc290822cf2b))
* fix PAM crew updates ([594134e](https://github.com/MTES-MCT/rapportnav2/commit/594134e5e60ddc37b34353644a3ace75672909a0))
* fix sending correct payload to Patch on Fish Actions ([#720](https://github.com/MTES-MCT/rapportnav2/issues/720)) ([897628b](https://github.com/MTES-MCT/rapportnav2/commit/897628b619fb683f5c02379d35812f2a205841ce))
* fix setting status correctly whether status action or not ([07a5d8c](https://github.com/MTES-MCT/rapportnav2/commit/07a5d8c597b99bebb9891409762b7ac0e177069d))
* fix setting status correctly whether status action or not ([feb96c1](https://github.com/MTES-MCT/rapportnav2/commit/feb96c15fa1df7db0caf7b4e126d72da69285041))
* fix version name in release-please manifest ([45d1b2f](https://github.com/MTES-MCT/rapportnav2/commit/45d1b2f6986fea4782d042b5fdb9b12b108cc4d2))
* release-please config update for .gitlab-ci.yml and build.gradle.kts ([82295bb](https://github.com/MTES-MCT/rapportnav2/commit/82295bbaa19d9ba413ae8cc1d23a7ecbdfbb0552))
* release-please config update for .gitlab-ci.yml and build.gradle.kts ([fe01c10](https://github.com/MTES-MCT/rapportnav2/commit/fe01c10fd7b516eb4ccbf50f3b0e758edf3dbf2a))
* release-please config update for build.gradle.kts ([c5103e3](https://github.com/MTES-MCT/rapportnav2/commit/c5103e380a19bb248e54e3c1114ced342a95b779))
* release-please config update for build.gradle.kts ([fb2f40f](https://github.com/MTES-MCT/rapportnav2/commit/fb2f40fc9490d94041721da813cb98922f019b27))
* release-please config update for build.gradle.kts ([285623d](https://github.com/MTES-MCT/rapportnav2/commit/285623d0d246dba0fafa40d84dc04446d1467546))
* release-please config update for build.gradle.kts ([d381a92](https://github.com/MTES-MCT/rapportnav2/commit/d381a927458aab2dcbccb2cc7b67ff264cb8fdb3))
* release-please config update for build.gradle.kts ([12df51f](https://github.com/MTES-MCT/rapportnav2/commit/12df51f36dab26ebf7ab71ce366e5f554c45000d))
* release-please config update for build.gradle.kts ([a1f6df6](https://github.com/MTES-MCT/rapportnav2/commit/a1f6df6ca8b194e727dc1faa21080a1f08aa43ef))
* release-please config update for build.gradle.kts ([8a898cf](https://github.com/MTES-MCT/rapportnav2/commit/8a898cfa252cc3a1edf6e40c4a50c1e5cc8412bd))
* release-please config update for build.gradle.kts ([64d92b5](https://github.com/MTES-MCT/rapportnav2/commit/64d92b52b2f344ff19433b09f429a4d141bda643))
* release-please config update for build.gradle.kts ([0865fab](https://github.com/MTES-MCT/rapportnav2/commit/0865faba4ad8d6ec347222cca3f661b3ed25b42c))
* release-please config update for build.gradle.kts ([01dfb31](https://github.com/MTES-MCT/rapportnav2/commit/01dfb317cb8a2d3b109c611db35d65a880fe2b21))
* release-please run on manual dispatch ([7f9445e](https://github.com/MTES-MCT/rapportnav2/commit/7f9445e3a16876083bfb0c7244f6132c706d8fcb))
* show correct timeline card for Note PAM ([62eae62](https://github.com/MTES-MCT/rapportnav2/commit/62eae62f24022f4bddcc5df0c7ed6eb36a90e203))
* timeline sorts UTC correctly ([d75c2c8](https://github.com/MTES-MCT/rapportnav2/commit/d75c2c8f1a5434ec4df909c4628aa9bcb4914a45))
* typo in mission-action.ts ([43e8b72](https://github.com/MTES-MCT/rapportnav2/commit/43e8b72607d2460858735069da10fd1df2ff6a3a))
* update release-please-config.json to try to overwrite versions in PR ([21d5c27](https://github.com/MTES-MCT/rapportnav2/commit/21d5c27c34dbb9bf63d610f081204cb903674cd4))
* UTC time conversion in header now updates instead of being rende… ([4995f97](https://github.com/MTES-MCT/rapportnav2/commit/4995f978ba1adb57e5ddd1c04187e6c183bd53ed))
* UTC time conversion in header now updates instead of being rendered just once ([6c72890](https://github.com/MTES-MCT/rapportnav2/commit/6c72890addedec49b4bcc9c98c00e38934f88571))
* validation on PAM generalInfos ([bd9cd65](https://github.com/MTES-MCT/rapportnav2/commit/bd9cd65b923967817c070f7b2047d36f711b0286))
