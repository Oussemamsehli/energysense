pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "oussemamsehli/energysense-core-api"
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
                    sh "docker build --no-cache -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh "docker logout"
                }
            }
        }
        stage('Update Manifest') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        git config user.email "jenkins@energysense.local"
                        git config user.name "Jenkins"
                        sed -i 's|${DOCKER_IMAGE}:.*|${DOCKER_IMAGE}:${DOCKER_TAG}|g' k8s/deployment.yaml
                        git add k8s/deployment.yaml
                        git commit -m "ci(k8s): update image to version ${DOCKER_TAG}" || echo "No changes to commit"
                        git push file:///var/jenkins_home/energysense-repo HEAD:feature/k8s-manifests --force
                    """
                }
            }
        }
    }
    post {
        success { echo 'Pipeline completed successfully!' }
        failure { echo 'Pipeline failed!' }
    }
}
