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
export APP_PORT=$1
sed -i "s/8080/$APP_PORT/" docker-compose.yml
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
                            scp -o StrictHostKeyChecking=no target/store-0.0.1-SNAPSHOT.jar ubuntu@${SERVER_IP}:/home/ubuntu/
                            scp -o StrictHostKeyChecking=no docker-compose.yml ubuntu@${SERVER_IP}:/home/ubuntu/
                            scp -o StrictHostKeyChecking=no deploy.sh ubuntu@${SERVER_IP}:/home/ubuntu/
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                input "Deploy to Staging?"
                script {
                    withCredentials([string(credentialsId: "${SERVER_IP_CRED_ID}", variable: 'SERVER_IP')]) {
                        deployToEnv('8081', SERVER_IP)
                        visitUrl('staging', 'http://3.1.221.135:8081/swagger-ui/index.html')
                    }
                }
            }
        }

        stage('Deploy to Prod') {
            steps {
                input "Deploy to Production?"
                script {
                    withCredentials([string(credentialsId: "${SERVER_IP_CRED_ID}", variable: 'SERVER_IP')]) {
                        deployToEnv('8082', SERVER_IP)
                        visitUrl('production', 'http://3.1.221.135:8082/swagger-ui/index.html')
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

def deployToEnv(port, serverIp) {
    sshagent(credentials: ['aws-ec2-pem']) {
        sh """
        ssh -o StrictHostKeyChecking=no ubuntu@${serverIp} "/home/ubuntu/deploy.sh ${port}"
        """
    }
}

def visitUrl(env, url) {
    echo "Visit the ${env} environment at ${url}"
}
