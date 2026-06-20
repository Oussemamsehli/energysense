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
                    sh './mvnw package -o -Dtest=IngestReadingServiceTest -DfailIfNoTests=false -Dmaven.repo.local=/var/jenkins_home/.m2/repository'
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir('core-api') {
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }
    }

    post {
        success { echo 'Pipeline completed successfully!' }
        failure { echo 'Pipeline failed!' }
    }
}