// Uses Declarative syntax to run commands inside a container.
pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
metadata:
  labels:
    jenkins: slave
spec:
  containers:
  - name: node
    image: node:14
    command:
    - cat
    tty: true
  - name: python
    image: python:3.6.10-slim-stretch
    command:
    - cat
    tty: true
'''
        }
    }
    options {
            timestamps()
            timeout(time: 1, unit: 'HOURS')
        }
    environment {
            CI = true
        }
    stages {
        stage('stage-1') {
            steps {
                script{
                    FAILED_STAGE = env.STAGE_NAME
                    sh 'echo stage 1'
                    sh 'ls'   
                }
            }
        }
        stage('stage-2') {
            steps {
                script{
                    FAILED_STAGE = env.STAGE_NAME
                    sh 'echo stage 2'
                    sh 'ls'   
                }
            }
        }
        stage('stage-3') {
            steps {
                script{
                    FAILED_STAGE = env.STAGE_NAME
                    sh 'echo stage 3'
                    sh 'ls'   
                }
            }
        }
        stage('stage-4') {
            steps {
                script{
                    sh 'echo stage 4'
                    unstable(message: "${STAGE_NAME} is unstable")                   
                    unstable_stage = env.STAGE_NAME    
                    sh 'git log'
                }
            }
        }
        stage('stage-5') {
            steps {
                script{
                    FAILED_STAGE = env.STAGE_NAME
                    sh 'echo stage 5'
                    sh 'ls'   
                }
            }
        }
    }
    post {
        always {
            echo 'post always - will always happens'
            deleteDir() /* clean up our workspace */
        }
        success {
            echo 'succeeded!'
        }
        unstable {
            echo 'unstable :/'
            echo "unstable stage name: ${unstable_stage}"
            sh 'git checkout main'
            sh 'git reset --hard HEAD~1'
            sh 'git push -f origin main'
        }
        failure {
            echo 'failed :('
            echo "Failed stage name: ${FAILED_STAGE}"
        }
    }
}
