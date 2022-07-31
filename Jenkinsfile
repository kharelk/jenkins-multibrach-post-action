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

                    // def repoUrlWithAuth = "https://kharelk:<PASSWORD>@github.com/kharelk/jenkins-multibrach-post-action.git"
                    def sourceBranch = "main"

                    try {
                        sh 'ecsfho stage 4'                        
                        STAGE_FOUR_STATUS = "SUCCESS"
                    } catch (Exception e) {
                        unstable("[ERROR]: ${STAGE_NAME} UNSTABLE!")
                        currentBuild.result = "SUCCESS"
                        STAGE_FOUR_STATUS = "UNSTABLE"
                    }
                    //test 6d
                    if( STAGE_FOUR_STATUS == "UNSTABLE") {
                        echo 'stage-4 is '+ STAGE_FOUR_STATUS
                        // echo 'git checkout branch main...'
                        sh 'git config --global credential.helper cache'
                        sh 'git config --global push.default simple'
                        
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: "refs/heads/" + sourceBranch]],
                            userRemoteConfigs: [[credentialsId: 'harel-github-creadentials', url: "https://github.com/kharelk/jenkins-multibrach-post-action"]],
                            // extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'helm']]
                        ])
                        // git branch: 'main', credentialsId: 'harel-github-creadentials', url: 'https://github.com/kharelk/jenkins-multibrach-post-action'

                        echo 'Reverting 1 commit back...'
                        sh 'git checkout main'
                        sh 'git reset --hard HEAD~1'
                        echo 'push to main'
                        sh 'git push -f origin main'
                        // sh 'git push origin HEAD:main'
                        // sh "git push -f --repo=${repoUrlWithAuth} --set-upstream ${repoUrlWithAuth} ${sourceBranch}"
                        echo 'Revert done!'
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
