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

        stage('Transfer Files') {
            steps {
                script {
                    withCredentials([string(credentialsId: "${SERVER_IP_CRED_ID}", variable: 'SERVER_IP')]) {
                        sshagent(credentials: ['aws-ec2-pem']) {
                            sh """
                            scp -o StrictHostKeyChecking=no target/store-0.0.1-SNAPSHOT.jar ubuntu@${SERVER_IP}:/home/ubuntu/
                            scp -o StrictHostKeyChecking=no docker-compose.yml ubuntu@${SERVER_IP}:/home/ubuntu/
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy to Dev') {
            steps {
                script {
                    deployToEnv('8081', 'dev')
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                input "Deploy to Staging?"
                script {
                    deployToEnv('8082', 'staging')
                }
            }
        }

        stage('Deploy to Prod') {
            steps {
                input "Deploy to Production?"
                script {
                    deployToEnv('8083', 'prod')
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

def deployToEnv(port, env) {
    withCredentials([string(credentialsId: "${SERVER_IP_CRED_ID}", variable: 'SERVER_IP')]) {
        sshagent(credentials: ['aws-ec2-pem']) {
            sh """
            ssh -o StrictHostKeyChecking=no ubuntu@${SERVER_IP} << 'EOF'
            cd /home/ubuntu/
            export APP_PORT=${port}
            sed -i 's/8080/${port}/' docker-compose.yml
            docker compose down
            docker compose up -d
            EOF
            """
        }
    }
}
