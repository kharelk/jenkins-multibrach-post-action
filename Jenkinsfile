#!/bin/groovy


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
  - name: ubuntu
    image: ubuntu:latest
    # Just spin & wait forever
    command: [ "/bin/bash", "-c", "--" ]
    args: [ "while true; do sleep 30; done;" ]
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
            // unstable_stage = ''
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
                    // sh 'sleep 5'
                    
                    // try {
                    //     sh 'echo stage 4'
                    //     STAGE_FOUR_STATUS = "SUCCESS"
                    // } catch (Exception e) {
                    //     unstable("[ERROR]: ${STAGE_NAME} UNSTABLE!")
                    //     currentBuild.result = "SUCCESS"
                    //     STAGE_FOUR_STATUS = "UNSTABLE"
                    //     // unstable_stage = env.STAGE_NAME
                    // }
                    // test commit reverting test
                    // if( STAGE_FOUR_STATUS == "UNSTABLE") {

                    //     //REVERT 1 COMMIT BACK:
                    //     // echo 'stage-4 is '+ STAGE_FOUR_STATUS
                    //     // repo = "https://github.com/kharelk/jenkins-multibrach-post-action"                    
                    //     // sourceBranch = "main"
                    //     // echo 'checkout branch '+sourceBranch

                    //     // checkout([
                    //     //     $class: 'GitSCM',
                    //     //     branches: [[name: "refs/heads/" + sourceBranch]],
                    //     //     userRemoteConfigs: [[credentialsId: 'harel-github-creadentials', url: repo]],
                    //     // ])

                    //     // echo 'Reverting 1 commit back from branch: '+sourceBranch+'...'
                    //     // withCredentials([usernamePassword(
                    //     // credentialsId: 'harel-github-creadentials',
                    //     // passwordVariable: 'TOKEN',
                    //     // usernameVariable: 'USER')]) {
                    //     //     sh 'git checkout main'
                    //     //     sh 'git reset --hard HEAD~1'
                    //     //     echo 'push to main'
                    //     //     // sh 'git push -f origin main'
                    //     //     sh "git push -f https://${USER}:${TOKEN}@github.com/kharelk/jenkins-multibrach-post-action.git main"
                    //     //     echo 'Revert done!'
                    //     // }
                    // }

                        def USER_EMAILS = [:]

                        USER_EMAILS = [
                            kharelk: "harel.karavani@checkmarx.com",
                            simon_shkilevich: "simon.shkilevich@checkmarx.com",
                        ]
                        
                        if (env.CHANGE_AUTHOR_DISPLAY_NAME) { // otherwise the object is not defined
                            getCommitAuthorNameUnderline = sh (
                                script: "echo ${env.CHANGE_AUTHOR_DISPLAY_NAME} | sed 's| |_|g' | awk '{print tolower(\$0)}'",
                                returnStdout: true).trim()
                        } else {
                            getCommitAuthorNameUnderline = sh(
                                    script: "git log -1 --pretty=format:'%an' | sed 's| |_|g' | awk '{print tolower(\$0)}'",
                                    returnStdout: true
                                ).trim().replace('"', "");
                            
                            // getCommitAuthorNameUnderline = common.commandExecutionShell("git log -1 --pretty=format:'%an' | sed 's| |_|g' | awk '{print tolower(\$0)}'").trim()
                        }

                        USER_EMAILS.find{ it.key == "{getCommitAuthorNameUnderline}" }?.value 
                        git_commit_user_email = USER_EMAILS[getCommitAuthorNameUnderline]
                        git_commit_user_email = USER_EMAILS.find{ it.key == getCommitAuthorNameUnderline.toString() }?.value 

                        echo "getCommitAuthorNameUnderline: " + getCommitAuthorNameUnderline
                        echo "git_commit_user_email: " + git_commit_user_email
                        
                        echo "USER_EMAILS[kharelk] = "+ USER_EMAILS['kharelk']



                        // Send emails to everyone involved
                        notification.sendCdEmailNotification (
                        buildStatus: currentBuild.result,
                        emailTo: git_commit_user_email,
                        lastCommitAuthorName: getCommitAuthorNameUnderline
                        )
                }
            }
        }
        stage('stage-5') {
            steps {
                script{
                    FAILED_STAGE = env.STAGE_NAME
                    sh 'echo stage 5'
                    // sh 'ls'
                    // sh 'sleep 999999'
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
            script{
                echo 'unstable :/'
                // echo "unstable stage name: ${unstable_stage}"
            }
            
        }
        failure {
            echo 'failed :('
            // echo "Failed stage name: ${FAILED_STAGE}"
        }
    }
}
