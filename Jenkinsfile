pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "energysense/core-api"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                dir('core-api') {
                    sh 'chmod +x mvnw'
                    sh './mvnw package'
                }
            }
            post {
                always {
                    junit 'core-api/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir('core-api') {
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}