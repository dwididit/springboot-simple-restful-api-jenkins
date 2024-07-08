pipeline {
    agent any

    environment {
        SERVER_IP_CRED_ID = 'server-ip-id'
        GITHUB_TOKEN_CRED_ID = 'github-token-id'
    }

    stages {
        stage('Checkout') {
            steps {
                withCredentials([string(credentialsId: "${GITHUB_TOKEN_CRED_ID}", variable: 'GITHUB_TOKEN')]) {
                    git url: "https://${GITHUB_TOKEN}@github.com/dwididit/springboot-simple-restful-api-jenkins.git", branch: 'master'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Prepare Deployment') {
            steps {
                writeFile file: 'deploy.sh', text: '''#!/bin/bash
cd /home/ubuntu/
docker compose down
docker compose up -d
'''
                sh 'chmod +x deploy.sh'
            }
        }

        stage('Transfer Files') {
            steps {
                script {
                    withCredentials([string(credentialsId: "${SERVER_IP_CRED_ID}", variable: 'SERVER_IP')]) {
                        sshagent(credentials: ['aws-ec2-pem']) {
                            sh """
                            scp -o StrictHostKeyChecking=no target/store-0.0.1-SNAPSHOT.jar ubuntu@${env.SERVER_IP}:/home/ubuntu/
                            scp -o StrictHostKeyChecking=no docker-compose.yml ubuntu@${env.SERVER_IP}:/home/ubuntu/
                            scp -o StrictHostKeyChecking=no deploy.sh ubuntu@${env.SERVER_IP}:/home/ubuntu/
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                script {
                    withCredentials([string(credentialsId: "${SERVER_IP_CRED_ID}", variable: 'SERVER_IP')]) {
                        deployToStaging(env.SERVER_IP)
                        visitUrl('staging', "http://${env.SERVER_IP}:8081/swagger-ui/index.html")
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}

def deployToStaging(serverIp) {
    sshagent(credentials: ['aws-ec2-pem']) {
        sh """
        ssh -o StrictHostKeyChecking=no ubuntu@${serverIp} "/home/ubuntu/deploy.sh"
        """
    }
}

def visitUrl(env, url) {
    echo "Visiting the ${env} environment at ${url}"
    script {
        def response = httpRequest url: url, validResponseCodes: '200'
        if (response.status == 200) {
            echo "Visit to ${url} was successful"
        } else {
            error "Visit to ${url} failed with status code: ${response.status}"
        }
    }
}
