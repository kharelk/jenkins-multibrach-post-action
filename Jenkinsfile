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
            // def slack_user_id = ''
            // def stageResults = [:]

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
                    // unstable(message: "${STAGE_NAME} is unstable")                   
                    // unstable_stage = env.STAGE_NAME    
                    // sh 'pwd'
                    // sh 'git log'
                    // STAGE_FOUR_STATUS = 'pass'

                    try {
                        // do stuff
                        // Add to map as SUCCESS on successful execution 
                        sh 'ecsfho stage 4'                        
                        STAGE_FOUR_STATUS = "SUCCESS"
                    } catch (Exception e) {
                        // Set the result and add to map as UNSTABLE on failure
                        unstable("[ERROR]: ${STAGE_NAME} failed!")
                        currentBuild.result = "SUCCESS"
                        STAGE_FOUR_STATUS = "UNSTABLE"
                    }
                    if(STAGE_FOUR_STATUS == "UNSTABLE") {
                        sh 'echo stage-4 is unstable\n do stuff for unstable'
                    }
                }
            }
        }
        stage('check-stages-status') {
            steps {
                script{
                    sh 'echo stage check-stages-status'
                //     if (STAGE_ONE_STATUS != 'pass' || STAGE_TWO_STATUS != 'pass' 
                //         || STAGE_THREE_STATUS != 'pass' || STAGE_FOUR_STATUS != 'pass') {
                //             // Preparing Git
                //             sh 'git config user.email "no-reply@codebashing-jenkins.com"'
                //             sh 'git config user.name "Jenkins Server"'
                            
                //             //Rervert one commit back
                //             sh 'git fetch origin main'
                //             sh 'git reset --hard HEAD~1'
                //             sh 'git push -f origin main'
                //         }
                // }
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
                // echo "unstable stage name: ${unstable_stage}"
                // sh 'pwd'
                // sh 'git log'

                // notification.slackClusterUpdated(
                //     services: pipelineConfiguration.services,
                //     environment: environment,
                //     deployState: "Failed and the realease_candidate deployment was triggered for the staging env  \
                //     \nFailed on ${FAILED_STAGE} Stage",
                //     commitAuthorName: commitAuthorName,
                //     shortCommitHash: shortCommitHash,
                //     slackChannelName: "jenkins_deployments_be"
                // )

                // def userIds = slackUserIdsFromCommitters()
                // def userIdsString = userIds.collect { "<@$it>" }.join(' ')
                // // userId = slackUserIdFromEmail('harel.karavani@checkmarx.com')
                // slackSend(color: "good", message: "<@$userIdsString> Message from Jenkins Pipeline")
            }
            
        }
        failure {
            echo 'failed :('
            // echo "Failed stage name: ${FAILED_STAGE}"
        }
    }
}



















// #!/bin/groovy

// def call() {
//     def slack_user_id = ''

//     pipeline {
//         agent {
//             kubernetes {
//                 yaml '''
// apiVersion: v1
// kind: Pod
// metadata:
//   labels:
//     jenkins: slave
// spec:
//   containers:
//   - name: node
//     image: node:14
//     command:
//     - cat
//     tty: true
//   - name: python
//     image: python:3.6.10-slim-stretch
//     command:
//     - cat
//     tty: true
// '''
//             }
//         }
//         options {
//                 timestamps()
//                 timeout(time: 1, unit: 'HOURS')
//             }
//         environment {
//                 CI = true
//             }
//             stages {
//                 stage('stage-1') {
//                 steps {
//                     script{
//                         FAILED_STAGE = env.STAGE_NAME
//                         sh 'echo stage 1'
//                         sh 'ls'   
//                     }
//                 }
//             }
//             stage('stage-2') {
//                 steps {
//                     script{
//                         FAILED_STAGE = env.STAGE_NAME
//                         sh 'echo stage 2'
//                         sh 'ls'   
//                     }
//                 }
//             }
//             stage('stage-3') {
//                 steps {
//                     script{
//                         FAILED_STAGE = env.STAGE_NAME
//                         sh 'echo stage 3'
//                         sh 'ls'   
//                     }
//                 }
//             }
//             stage('stage-4') {
//                 steps {
//                     script{
//                         sh 'echo stage 4'
//                         unstable(message: "${STAGE_NAME} is unstable")                   
//                         unstable_stage = env.STAGE_NAME    
//                         sh 'pwd'
//                         sh 'git log'
//                     }
//                 }
//             }
//             stage('stage-5') {
//                 steps {
//                     script{
//                         FAILED_STAGE = env.STAGE_NAME
//                         sh 'echo stage 5'
//                         sh 'ls'   
//                     }
//                 }
//             }
//             }
//         post {
//             always {
//                 echo 'post always - will always happens'
//                 deleteDir() /* clean up our workspace */
//             }
//             success {
//                 echo 'succeeded!'
//             }
//             unstable {
//                 // echo 'unstable :/'
//                 // echo "unstable stage name: ${unstable_stage}"
//                 // sh 'git reset --hard HEAD~1'
//                 // sh 'git push -f origin main'
//                 script{
//                     echo 'unstable :/'
//                     echo "unstable stage name: ${unstable_stage}"
//                     sh 'pwd'
//                     sh 'git log'

//                     // notification.slackClusterUpdated(
//                     //     services: pipelineConfiguration.services,
//                     //     environment: environment,
//                     //     deployState: "Failed and the realease_candidate deployment was triggered for the staging env  \
//                     //     \nFailed on ${FAILED_STAGE} Stage",
//                     //     commitAuthorName: commitAuthorName,
//                     //     shortCommitHash: shortCommitHash,
//                     //     slackChannelName: "jenkins_deployments_be"
//                     // )


//                     userId = slackUserIdFromEmail('harel.karavani@checkmarx.com')
//                     slackSend(color: "good", message: "<@$userId> Message from Jenkins Pipeline")
//                 }
                
//             }
//             failure {
//                 echo 'failed :('
//                 echo "Failed stage name: ${FAILED_STAGE}"
//             }
//         }
//     }
// }
