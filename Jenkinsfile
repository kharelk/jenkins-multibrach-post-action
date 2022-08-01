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
            stage('Set1') {
                steps {
                    script {
                        USER_EMAILS = load("functions.groovy").getCbDevelopersEmails()
                        
                        if (env.CHANGE_AUTHOR_DISPLAY_NAME) { // otherwise the object is not defined
                            getCommitAuthorNameUnderline = sh (
                                script: "echo ${env.CHANGE_AUTHOR_DISPLAY_NAME} | sed 's| |_|g' | awk '{print tolower(\$0)}'",
                                returnStdout: true).trim()
                        } else {
                            getCommitAuthorNameUnderline = common.commandExecutionShell("git log -1 --pretty=format:'%an' | sed 's| |_|g' | awk '{print tolower(\$0)}'").trim()
                        }
                        git_commit_user_email = USER_EMAILS[getCommitAuthorNameUnderline]
                        echo "getCommitAuthorNameUnderline: " + getCommitAuthorNameUnderline
                        echo "git_commit_user_email: " + git_commit_user_email
                    }
                }
            }
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
                    } catch (Exception e) {
                        unstable("[ERROR]: ${STAGE_NAME} UNSTABLE!")
                        currentBuild.result = "SUCCESS"
                        STAGE_FOUR_STATUS = "UNSTABLE"
                        unstable_stage = env.STAGE_NAME
                    }
                    // test commit reverting
                    if( STAGE_FOUR_STATUS == "UNSTABLE") {
                        echo 'stage-4 is '+ STAGE_FOUR_STATUS



                        //1) FIRST APPROACH
                        // repo = "https://github.com/kharelk/jenkins-multibrach-post-action"                    
                        // sourceBranch = "main"
                        // echo 'checkout branch '+sourceBranch

                        // checkout([
                        //     $class: 'GitSCM',
                        //     branches: [[name: "refs/heads/" + sourceBranch]],
                        //     userRemoteConfigs: [[credentialsId: 'harel-github-creadentials', url: repo]],
                        // ])

                        // echo 'Reverting 1 commit back from branch: '+sourceBranch+'...'
                        // withCredentials([usernamePassword(
                        // credentialsId: 'harel-github-creadentials',
                        // passwordVariable: 'TOKEN',
                        // usernameVariable: 'USER')]) {
                        //     sh 'git checkout main'
                        //     sh 'git reset --hard HEAD~1'
                        //     echo 'push to main'
                        //     // sh 'git push -f origin main'
                        //     sh "git push -f https://${USER}:${TOKEN}@github.com/kharelk/jenkins-multibrach-post-action.git main"
                        //     echo 'Revert done!'
                        // }

                        // log.infoMessage("Sending email")
                        // log.infoMessage("To: ${emailTo}")
                        // log.infoMessage("body: ${body}")
                        // log.infoMessage("subject: ${subject}")

                        // emailext (
                        //     subject: "${subject}" ,
                        //     body: body,
                        //     recipientProviders: [[$class: 'RequesterRecipientProvider']],
                        //     to: emailTo
                        // )
                    
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
            script{
                echo 'unstable :/'
                echo "unstable stage name: ${unstable_stage}"
            }
            
        }
        failure {
            echo 'failed :('
            // echo "Failed stage name: ${FAILED_STAGE}"
        }
    }
}
