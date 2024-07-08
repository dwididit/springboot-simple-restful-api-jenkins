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
                sh 'docker compose build'
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
            rm -rf springboot-simple-restful-api-jenkins
            git clone https://${GITHUB_TOKEN}@github.com/dwididit/springboot-simple-restful-api-jenkins.git
            cd springboot-simple-restful-api-jenkins
            export APP_PORT=${port}
            sed -i 's/8080/${port}/' docker-compose.yml
            docker compose down
            docker compose up -d
            EOF
            """
        }
    }
}
