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