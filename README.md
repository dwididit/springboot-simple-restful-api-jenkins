# Project Overview

This project is a simple RESTful API created using Spring Boot.


## CI/CD with Jenkins
This project includes a Jenkins pipeline for CI/CD. The pipeline performs the following tasks:
1. Checks out the code from the Git repository.
2. Builds the project using Maven.
3. Runs unit tests.
4. Deploys the application to the specified environment.


### Setting Up Jenkins
1. Install Jenkins from the official website.
2. Install the necessary plugins:
   - Git Plugin
   - Maven Integration Plugin
3. Configure Jenkins with your Git repository and Maven settings.

### Creating a Pipeline
1. Create a new pipeline in Jenkins.
2. Configure the pipeline script to point to your Jenkinsfile.


Example `Jenkinsfile`:
```groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/yourusername/your-repository.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                // Add your deployment steps here
            }
        }
    }
}
```