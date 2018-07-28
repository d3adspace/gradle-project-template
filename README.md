# gradle-project-template
A gradle template project containing configuration for common architectures including multi project builds, testing with JUnit 5 and Mockito, CI via TravisCI, Coverage via codecov, code climate via codacy and some other useful tools.

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
```
plugins {

    id 'java'
}

group 'de.d3adspace'
version '1.0-SNAPSHOT'

subprojects {

    apply plugin: 'java'
    apply plugin: 'maven'

    sourceCompatibility = 1.8
    targetCompatibility = 1.8

    repositories {

        /**
         * Resolving local maven repository.
         */
        mavenLocal()

        /**
         * Maven central repository.
         */
        mavenCentral()
    }

    dependencies {

        /**
         * JUnit Jupiter as a testing framework.
         */
        testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-api', version: '5.2.0'

        /**
         * JUnit Jupiter parameter configuration.
         */
        testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-params', version: '5.2.0'

        /**
         * JUnit Jupiter testing engine.
         */
        testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-engine', version: '5.2.0'

        /**
         * Vintage platform engine.
         */
        compile group: 'org.junit.platform', name: 'junit-platform-engine', version: '1.2.0'

        /**
         * JUnit jupiter with mockito.
         */
        testCompile group: 'org.mockito', name: 'mockito-junit-jupiter', version: '2.19.0'

        /**
         * Mockito for mocking.
         */
        testCompile group: 'org.mockito', name: 'mockito-core', version: '2.19.0'
    }

    test {
        useJUnitPlatform()
    }
}
```
