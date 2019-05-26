# gradle-project-template
A gradle template project containing configuration for common architectures including multi project builds, testing with JUnit 5 and Mockito, CI via TravisCI, Coverage via codecov, code climate via codacy and some other useful tools.

# Build status
|             	| Build Status                                                                                                                                              	| Test Code Coverage                                                                                                                                               	|
|-------------	|-----------------------------------------------------------------------------------------------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------------------------------------------------------------	|
| Master      	| [![Build Status](https://travis-ci.org/d3adspace/gradle-project-template.svg?branch=master)](https://travis-ci.org/d3adspace/gradle-project-template) 	| [![codecov](https://codecov.io/gh/d3adspace/gradle-project-template/branch/master/graph/badge.svg)](https://codecov.io/gh/d3adspace/gradle-project-template) 	|
| Development 	| [![Build Status](https://travis-ci.org/d3adspace/gradle-project-template.svg?branch=dev)](https://travis-ci.org/d3adspace/gradle-project-template)    	| [![codecov](https://codecov.io/gh/d3adspace/gradle-project-template/branch/dev/graph/badge.svg)](https://codecov.io/gh/d3adspace/gradle-project-template)    	|

# Steps for project generation

1. Create a new folder, enter it and use `gradle init`.
2. Add .gitignore file with your basic project files:
```
#####################
### Project files ###
#####################
*.iml
.idea
target
out

##############
### Gradle ###
##############
.gradle
build

# Never ignore gradle wrapper
!gradle/wrapper/gradle-wrapper.jar
```
3. Share project on github.
4. Add README with basic information.
5. Create a dev branch and protect the master branch.
6. Visit travis-ci.org and enable the builds for your project.
7. Create .travis.yml in a new branch and configure your build:
```
######################################
### Configure programming language ###
######################################
language: java

##################################
### Make sure using Oracle JDK ###
##################################
jdk:
  - oraclejdk8

###################################
### Disable Email notifications ###
###################################
notifications:
  email: false

###########################
### Actual Build Script ###
###########################
script:
  - ./gradlew build check
```
8. Let travis build and merge into dev as soon as it succeeds.
9. Add a code of conduct (See CODE_OF_CONDUCT.md) in a new branch and merge it into dev.
10. Add LICENSE file in a new branch and merge it into dev. You may also add the license text to be used as a default file header for all your project files.
11. Add GitHub issue templates for bugs and feature requests.
12. Add JUnit 5 and Mockito for testing in a new branch and merge into dev. May look like this:
```groovy
plugins {

    id 'java'
}

group 'de.d3adspace'
version '1.0-SNAPSHOT'

allprojects {

    apply plugin: 'java'
    apply plugin: 'jacoco'
    apply plugin: 'maven'

    sourceCompatibility = 1.11
    targetCompatibility = 1.11

    repositories {

        mavenLocal()
        mavenCentral()
    }

    dependencies {

        testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-api', version: '5.2.0'
        testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-params', version: '5.2.0'
        testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-engine', version: '5.2.0'
        testCompile group: 'org.junit.platform', name: 'junit-platform-engine', version: '1.2.0'
        testCompile group: 'org.mockito', name: 'mockito-junit-jupiter', version: '2.19.0'
        testCompile group: 'org.mockito', name: 'mockito-core', version: '2.19.0'
    }

    test {
        useJUnitPlatform()
    }
}
```
13. Add jacoco gradle plugin with `apply plugin: 'jacoco'` and configure the corresponding task that will generate and aggregate test reports: 
```groovy

task codeCoverageReport(type: JacocoReport) {
    executionData fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec")

    subprojects.each {
        sourceSets it.sourceSets.main
    }

    reports {
        xml.enabled = true
        html.enabled = true
        csv.enabled = true
        xml.destination = new File("${buildDir}/reports/jacoco/report.xml")
        html.destination = new File("${buildDir}/reports/jacoco/report.html")
        csv.destination = new File("${buildDir}/reports/jacoco/report.csv")
    }
}

```
14. Alter your build script to offer coverage reporting to codecov.io:
```
###########################
### Actual Build Script ###
###########################
script:
  - ./gradlew build check
  - ./gradlew codeCoverageReport

###################################
### Upload Code Coverage Report ###
###################################
after_success:
  - bash <(curl -s https://codecov.io/bash)
```
15. Add status badges to readme:
```
|             	| Build Status                                                                                                                                              	| Test Code Coverage                                                                                                                                               	|
|-------------	|-----------------------------------------------------------------------------------------------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------------------------------------------------------------	|
| Master      	| [![Build Status](https://travis-ci.org/d3adspace/gradle-project-template.svg?branch=master)](https://travis-ci.org/d3adspace/gradle-project-template) 	| [![codecov](https://codecov.io/gh/d3adspace/gradle-project-template/branch/master/graph/badge.svg)](https://codecov.io/gh/d3adspace/gradle-project-template) 	|
| Development 	| [![Build Status](https://travis-ci.org/d3adspace/gradle-project-template.svg?branch=dev)](https://travis-ci.org/d3adspace/gradle-project-template)    	| [![codecov](https://codecov.io/gh/d3adspace/gradle-project-template/branch/dev/graph/badge.svg)](https://codecov.io/gh/d3adspace/gradle-project-template)    	|
```
16. Configure builds for fat jars and dependencies. Our Example ("core" needs "api" at runtime):

Gradle Plugins (We use shadow: https://github.com/johnrengelman/shadow):
```
id 'com.github.johnrengelman.shadow' version '2.0.4'
```

Configuration depending on what you need. In our case we want to include all dependencies in runtime scope:

```
shadowJar {

    configurations = [project.configurations.runtime]
}
```

17. Add sonarqube Audits
In this case we will use a self hosted sonarqube. You can also use the public [Sonarcloud with travis-ci](https://docs.travis-ci.com/user/sonarcloud/). 

We will first add the sonarqube plugin to our build.gradle file:

```xml
plugins {
    id 'org.sonarqube' version "2.7"
    id 'java'
}
```

To integrate sonarqube into our CI pipeline we add the following build script:
```xml
###########################
### Actual Build Script ###
###########################
script:
- ./gradlew build test
- ./gradlew codeCoverageReport
- ./gradlew sonarqube -Dsonar.host.url=$SONAR_URL -Dsonar.projectKey=$SONAR_PROJECT_KEY -Dsonar.login=$SONAR_TOKEN
```

As our private sonarqube needs authentication, we have to [encrypt](https://docs.travis-ci.com/user/encryption-keys/) the login in the travis ci config:
```xml
###################
### Environment ###
###################
env:
  global:
    - SONAR_URL=https://sonar.klauke-enterprises.com
    - SONAR_PROJECT_KEY=gradle-project-template
    - secure: H8vdFbgvrbLsQ402ukiwaftS3UYfOnA7RuF62oAhU1BBRA0C3oPz3wynoYgWFqEdfWDmiTVYi0YdIJLCCq9aL7H9omEu6fiTYXjIF5AwaKe7Uo4BicQhHMEMC3Ew516wEAioJV64VZZh/ydMCi3vIMViiJGYu96uVQJSsNe234/VHgC0slhFPMLebqLm17FK4lGO6mSRM3DgEsgeJPGz7afXrZxQp/Awi7EpGs8gyra6M0pfQ9aSOisNUTt5oS8wtTM0ByD6Xdug5yK0+l5D/Ct3slS6CNmkKGQC8W6e/hNm457ZML5si7agyO0jIN2hgnHerME46dnR+R6j5Xp2H16dmACbhmIraQ5Zz7fJCXDAdHI7bfMB0Gonmdi/VlJrpv+dwJtp5s91G/Ju6Rx/D8zVoiu19s4TazJ+td4btCDNt0cZITh+q4HF3Wo94LAFsEO7nXgVgDZnH6vADHD8dGlHG6D648HV6O3MFRpZJBNN6wFksuVEBvyYdiDnfKobZtYoi9JBmV2zYVwubleiYft0jrzJOnfWg2884JzGJoYo+Sk5n5sEodeMnPO9JunIxabiprd57FrI12WZ8GU91aXmkeYBv/4hjPbAvRyYsE9nz6ZhH64qQ/oY9CDhi73jmq+2316qGcu/i7YwBoBFJNrPLSnLjxqwXPNX3iuydcQ=

```

18. Deployment into nexus

To reuse our library in foreign applications via Maven or Gradle we have to deploy our artifacts
into a maven repository. In this case we will use a Sonatype Nexus. 

The deployment is managed via the maven publish plugin: 

```grovy
apply plugin: 'java'
apply plugin: 'jacoco'
apply plugin: 'maven'
apply plugin: 'maven-publish'
```

Let's configure what to publish and where to publish:

```groovy
publishing {
    publications {
        maven(MavenPublication) {
            from(components.java)
        }
    }

    repositories {

        maven {

            def releasesRepoUrl = 'https://repository.klauke-enterprises.com/repository/maven-releases/'
            def snapshotsRepoUrl = 'https://repository.klauke-enterprises.com/repository/maven-snapshots/'
            url = version.endsWith('SNAPSHOT') ? snapshotsRepoUrl : releasesRepoUrl

            credentials {
                username = System.getenv('NEXUS_USERNAME')
                password = System.getenv('NEXUS_PASSWORD')
            }
        }
    }
}
```

The publishing is done via our CI pipeline is configured like this:

```xml
##################
### Deployment ###
##################
deploy:
  provider: script
  script: ./gradlew publish
  on:
    branch: master
    tags: true
```

But the CI still needs the credentials to access the nexus. These are supplied via envrionemtn variables. We can [encrypt](https://docs.travis-ci.com/user/encryption-keys/) them into our CI config:

```xml
env:
  global:
    - SONAR_URL=https://sonar.klauke-enterprises.com
    - SONAR_PROJECT_KEY=gradle-project-template
    - secure: H8vdFbgvrbLsQ402ukiwaftS3UYfOnA7RuF62oAhU1BBRA0C3oPz3wynoYgWFqEdfWDmiTVYi0YdIJLCCq9aL7H9omEu6fiTYXjIF5AwaKe7Uo4BicQhHMEMC3Ew516wEAioJV64VZZh/ydMCi3vIMViiJGYu96uVQJSsNe234/VHgC0slhFPMLebqLm17FK4lGO6mSRM3DgEsgeJPGz7afXrZxQp/Awi7EpGs8gyra6M0pfQ9aSOisNUTt5oS8wtTM0ByD6Xdug5yK0+l5D/Ct3slS6CNmkKGQC8W6e/hNm457ZML5si7agyO0jIN2hgnHerME46dnR+R6j5Xp2H16dmACbhmIraQ5Zz7fJCXDAdHI7bfMB0Gonmdi/VlJrpv+dwJtp5s91G/Ju6Rx/D8zVoiu19s4TazJ+td4btCDNt0cZITh+q4HF3Wo94LAFsEO7nXgVgDZnH6vADHD8dGlHG6D648HV6O3MFRpZJBNN6wFksuVEBvyYdiDnfKobZtYoi9JBmV2zYVwubleiYft0jrzJOnfWg2884JzGJoYo+Sk5n5sEodeMnPO9JunIxabiprd57FrI12WZ8GU91aXmkeYBv/4hjPbAvRyYsE9nz6ZhH64qQ/oY9CDhi73jmq+2316qGcu/i7YwBoBFJNrPLSnLjxqwXPNX3iuydcQ=
    - secure: pUStloSdx6cNK6Pcsp2Wj7piIvX5u26vu8f2f5eecVewObhkW29yiVtP17aXtwETbZPxfv3F+G8yeEeEgV8tYAE4o2BKUcr0chOxXGSBKlrs6kkdJQo7XwBxdYE8kvKqjcAWx7CBXDZssCyvSZU9V0mZxTYDMXzA0qaFUzCsEV1decx+Jc8gpL4MqcE9z8/dsammYBTV39Gla2wJbKI5iY5S5dWMzVUlXuiYIIiUrUurHLta8oTkpJ7VBQsSHcZ7gPPOsN2E5XcuZk9gkNoJlZJu8mtu/mkwOZLz2g4GZ65hUuWwNYUkjNCeBXUGAIuf/X7gyKWVmwsG6eO07dasBgd93fGN8PyVcXZWZYI8jQd4WhS2e+xf+REH4UOEFcydSAvnKoeBo03PXYpREzY4QI37by7TKCdtutxVFpTDpm3awxOugd9IPNAzNJ/foRhiHpT1BxbLKirkjnneHERXHkDsOQC0Vgq8ihZRTyJVuXYQltBwau69kIxpCFWk1lhBXRu7Yvnva52+r0oIz6IcvUX9k+yFVVSuVxevc7MHZBEx54FNlG+lblwH8peYQij7i4My2+SgKXYZXgEpEwXM3e2hUpBsbZHRBpIH140YPwI0I3LV5ZRS5Q/jTJDqJjjfMDFW0E2WTbJEenIA30rWTIxHCc+DRHDUTeNz07uMDTI=
    - secure: bNszyk4/NP6VmgJbWRTfiWYL9uzaso0KsXK5zHoHkfea4gCEaWfUwi8bmErJXZnGdLizQa2qlEvIRmKn5z0SBvjo2NkM/5HiO4/6xeJLsH9pC6WPVcBhf6NFcIOt+2VcXpIfKFUbjmt/r+GSVxGGv6jBan8G7mEBif/CmjKenq8n1GPoXCWVFgv33XEQraDMcLpPu0mQMMkfVQYR+gp6hgzw+z9mtg0xAaKcFS4fwjyujnH/oIsZ3U40HyUvU2+AZ34pomkhBxu9VcZ4q7f1+v7dzCOzgUeb9hyuHWrJIJgQC0KEC5LiNJc6Xh52s5ETyzMal0kncQpyBdK67dqK4D/JXN2fDjDNOBPFFEEy7uAtPMOnnFyorLL28LBYsPmMgc/jW3u2lB8q7J8OBr4cjS4jF6e8PIoj3eVx8j0t04qd7doVewk6HTb4vkH+NjgX4OpeLU24hVbGSFdK3T66QjpTaF3f0w6T4KdIga872C0xetC7cKduiF9RSkf2J/htnB8+/+nCGwca/oFK9OJiebs5dRU16JxUOf9UEq8lVsdWdfwg4fSw0bqWJLpAinXtGdWyJrVIj7HwHIWuxOQm4cXFu8DEkt4JjIpzZbe5/rwY478RVonYGj8BnS5eQmmVm0tZY5gmMn9a7lkeN88e+YLxYqyQnYIhQ4K+XqamDWs=
```
