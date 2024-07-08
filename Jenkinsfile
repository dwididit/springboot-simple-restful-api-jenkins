pipeline {
    agent any

    environment {
        SERVER_IP = credentials('server-ip-id')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-repo/your-spring-boot-app.git'
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
    withCredentials([sshUserPrivateKey(credentialsId: 'ssh-key-id', keyFileVariable: 'SSH_KEY')]) {
        sh """
        scp -o StrictHostKeyChecking=no docker-compose.yml ec2-user@${SERVER_IP}:/home/ec2-user/
        ssh -i ${SSH_KEY} ec2-user@${SERVER_IP} << EOF
        cd /home/ec2-user/
        export APP_PORT=${port}
        docker-compose down
        docker-compose up -d
        EOF
        """
    }
}
