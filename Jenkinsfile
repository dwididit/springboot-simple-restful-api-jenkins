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
                    withCredentials([string(credentialsId: "${GITHUB_TOKEN_CRED_ID}", variable: 'GITHUB_TOKEN'),
                                     string(credentialsId: "${SERVER_IP_CRED_ID}", variable: 'SERVER_IP')]) {
                        deployToEnv('8081', 'dev', SERVER_IP, GITHUB_TOKEN)
                    }
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                input "Deploy to Staging?"
                script {
                    withCredentials([string(credentialsId: "${GITHUB_TOKEN_CRED_ID}", variable: 'GITHUB_TOKEN'),
                                     string(credentialsId: "${SERVER_IP_CRED_ID}", variable: 'SERVER_IP')]) {
                        deployToEnv('8082', 'staging', SERVER_IP, GITHUB_TOKEN)
                    }
                }
            }
        }

        stage('Deploy to Prod') {
            steps {
                input "Deploy to Production?"
                script {
                    withCredentials([string(credentialsId: "${GITHUB_TOKEN_CRED_ID}", variable: 'GITHUB_TOKEN'),
                                     string(credentialsId: "${SERVER_IP_CRED_ID}", variable: 'SERVER_IP')]) {
                        deployToEnv('8083', 'prod', SERVER_IP, GITHUB_TOKEN)
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

def deployToEnv(port, env, serverIp, githubToken) {
    sshagent(credentials: ['aws-ec2-pem']) {
        sh """
        ssh -o StrictHostKeyChecking=no ubuntu@${serverIp} << 'EOF'
        cd /home/ubuntu/
        rm -rf springboot-simple-restful-api-jenkins
        git clone https://${githubToken}@github.com/dwididit/springboot-simple-restful-api-jenkins.git
        cd springboot-simple-restful-api-jenkins
        export APP_PORT=${port}
        sed -i 's/8080/${port}/' docker-compose.yml
        docker compose down
        docker compose up -d
        EOF
        """
    }
}
