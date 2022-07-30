
def stageResults = [:]


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
            STAGE_ONE_STATUS = ''
            STAGE_TWO_STATUS = ''
            STAGE_THREE_STATUS = ''
            STAGE_FOUR_STATUS = ''
            unstable_stage = ''
        }
    stages {
        stage('stage-1') {
            steps {
                script{
                    FAILED_STAGE = env.STAGE_NAME
                    sh 'echo stage 1'
                    sh 'ls'   
                    STAGE_ONE_STATUS = 'pass'
                }
            }
        }
        stage('stage-2') {
            steps {
                script{
                    FAILED_STAGE = env.STAGE_NAME
                    sh 'echo stage 2'
                    sh 'ls'   
                    STAGE_TWO_STATUS = 'pass'
                }
            }
        }
        stage('stage-3') {
            steps {
                script{
                    FAILED_STAGE = env.STAGE_NAME
                    sh 'echo stage 3'
                    sh 'ls'
                    STAGE_THREE_STATUS = 'pass'
                }
            }
        }
        stage('stage-4') {
            steps {
                script{
                    sh 'echo stage 4'
                    try {

                        sh 'ecsfho stage 4'                        
                        STAGE_FOUR_STATUS = "SUCCESS"
                        stageResults."{STAGE_NAME}" = "SUCCESS"

                    } catch (Exception e) {
                        unstable("[ERROR]: ${STAGE_NAME} failed!")
                        currentBuild.result = "SUCCESS"
                        STAGE_FOUR_STATUS = "UNSTABLE"
                        
                        stageResult."{STAGE_NAME}" = "UNSTABLE"

                        echo 'stage-4 is ${STAGE_FOUR_STATUS}, do stuff for unstable pipeline'

                    }
                    // if(STAGE_FOUR_STATUS == "UNSTABLE") {
                    if(stageResults.find{ it.key == "{STAGE_NAME}" }?.value == "UNSTABLE") {
                        echo 'stage-4 is '+ STAGE_FOUR_STATUS + ', do stuff for unstable pipeline'
                    }
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
            // deleteDir() /* clean up our workspace */
        }
        success {
            echo 'succeeded!'
        }
        unstable {
            // echo 'unstable :/'
            // echo "unstable stage name: ${unstable_stage}"
            // sh 'git reset --hard HEAD~1'
            // sh 'git push -f origin main'
            script{
                echo 'unstable :/'
                echo "unstable stage name: ${STAGE_FOUR_STATUS}"
            }
            
        }
        failure {
            echo 'failed :('
            // echo "Failed stage name: ${FAILED_STAGE}"
        }
    }
}
