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
                
                    // CHECKOUT AND PUSH UPDATE TO MAIN BRANCH AND DEVELOP BRANCH
                    repo = "https://github.com/kharelk/jenkins-multibrach-post-action"                    
                    sourceBranch = "main"
                    sourceBranch_dev = "test"

                    echo 'checkout branch '+sourceBranch
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "refs/heads/" + sourceBranch], [name: "refs/heads/" + sourceBranch_dev]],
                        userRemoteConfigs: [[credentialsId: 'harel-github-creadentials', url: repo]],
                    ])

                    withCredentials([usernamePassword(
                    credentialsId: 'harel-github-creadentials',
                    passwordVariable: 'TOKEN',
                    usernameVariable: 'USER')]) {
                        sh 'git checkout main'

                        dir('overlays'){
                            sh "sed -i \"s/tag: .*\$/tag: \\'" + "000004" + "\\'/\" dev.yaml"
                            sh "git add dev.yaml"
                            try {
                                sh "git commit -m 'commit for main branch'"
                            } catch(Exception e) {
                                log.infoMessage(e.toString())
                            }
                            retry(5) {
                                sleep(2)
                                sh "git push -f https://${USER}:${TOKEN}@github.com/kharelk/jenkins-multibrach-post-action.git HEAD:main"
                                sh "git push -f https://${USER}:${TOKEN}@github.com/kharelk/jenkins-multibrach-post-action.git HEAD:test"
                                
                                // sh "git push origin HEAD:"+ sourceBranch
                            }
                            
                        // sh 'git checkout develop'

                        // dir('overlays'){
                        //     sh "sed -i \"s/tag: .*\$/tag: \\'" + "000002" + "\\'/\" dev.yaml"
                        //     sh "git add dev.yaml"
                        //     try {
                        //         sh "git commit -m 'commit for dev branch'"
                        //     } catch(Exception e) {
                        //         log.infoMessage(e.toString())
                        //     }
                        //     retry(5) {
                        //         sleep(2)
                        //         sh "git push -f https://${USER}:${TOKEN}@github.com/kharelk/jenkins-multibrach-post-action.git HEAD:main"
                        //         // sh "git push origin HEAD:"+ sourceBranch
                        //     }


                        }  

                        // echo 'Push to main'
                        // // sh 'git push -f origin main'
                        // sh "git push -f https://${USER}:${TOKEN}@github.com/kharelk/jenkins-multibrach-post-action.git HEAD:main"
                        // echo 'Push done!'
                    }
                    

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
