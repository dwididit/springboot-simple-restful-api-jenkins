pipeline {
    agent any

    environment {
        SERVER_IP = credentials('server-ip-id')
        GITHUB_TOKEN = credentials('github-token-id')
    }

    stages {
        stage('Checkout') {
            steps {
                git url: "https://${GITHUB_TOKEN}@github.com/dwididit/springboot-simple-restful-api-jenkins.git", branch: 'master'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
                sh 'docker-compose build'
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
    sshagent(credentials: ['aws-ec2-pem']) {
        sh """
        scp -o StrictHostKeyChecking=no docker-compose.yml ubuntu@${SERVER_IP}:/home/ubuntu/
        ssh -o StrictHostKeyChecking=no ubuntu@${SERVER_IP} << EOF
        cd /home/ubuntu/
        export APP_PORT=${port}
        sed -i 's/8080/${APP_PORT}/' docker-compose.yml
        docker-compose down
        docker-compose up -d
        EOF
        """
    }
}
