pipeline {
    agent any

    environment {
        SERVER_IP = credentials('server-ip-id')  // Fetch EC2 instance IP from Jenkins credentials
        GITHUB_TOKEN = credentials('github-token-id')  // Fetch GitHub token from Jenkins credentials
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from GitHub repository
                git url: "https://${GITHUB_TOKEN}@github.com/dwididit/springboot-simple-restful-api-jenkins.git", branch: 'master'
            }
        }

        stage('Build') {
            steps {
                // Build the Maven project
                sh 'mvn clean package'
                // Build Docker images using Docker Compose
                sh 'docker compose build'
            }
        }

        stage('Deploy to Dev') {
            steps {
                script {
                    // Deploy to the Dev environment
                    deployToEnv('8081', 'dev')
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                // Prompt for manual confirmation before deploying to Staging
                input "Deploy to Staging?"
                script {
                    // Deploy to the Staging environment
                    deployToEnv('8082', 'staging')
                }
            }
        }

        stage('Deploy to Prod') {
            steps {
                // Prompt for manual confirmation before deploying to Production
                input "Deploy to Production?"
                script {
                    // Deploy to the Production environment
                    deployToEnv('8083', 'prod')
                }
            }
        }
    }

    post {
        always {
            // Clean workspace after the build
            cleanWs()
        }
    }
}

// Function to deploy to a specified environment
def deployToEnv(port, env) {
    sshagent(credentials: ['aws-ec2-pem']) {
        sh """
        // Copy Docker Compose file to the EC2 instance
        scp -o StrictHostKeyChecking=no docker-compose.yml ubuntu@${SERVER_IP}:/home/ubuntu/
        // SSH into the EC2 instance and run Docker Compose commands
        ssh -o StrictHostKeyChecking=no ubuntu@${SERVER_IP} << EOF
        cd /home/ubuntu/
        export APP_PORT=${port}
        sed -i 's/8080/${APP_PORT}/' docker-compose.yml
        docker compose down
        docker compose up -d
        EOF
        """
    }
}
