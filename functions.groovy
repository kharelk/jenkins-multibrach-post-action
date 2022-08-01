def revertOneCommitBack() {
    repo = "https://github.com/kharelk/jenkins-multibrach-post-action"                    
    sourceBranch = "main"
    echo 'checkout branch '+sourcebranch

    checkout([
        $class: 'GitSCM',
        branches: [[name: "refs/heads/" + sourceBranch]],
        userRemoteConfigs: [[credentialsId: 'harel-github-creadentials', url: repo]],
    ])

    echo 'Reverting 1 commit back from branch: '+sourceBranch+'...'
    withCredentials([usernamePassword(
    credentialsId: 'harel-github-creadentials',
    passwordVariable: 'TOKEN',
    usernameVariable: 'USER')]) {
        sh 'git checkout main'
        sh 'git reset --hard HEAD~1'
        echo 'push to main'
        // sh 'git push -f origin main'
        sh "git push -f https://${USER}:${TOKEN}@github.com/kharelk/jenkins-multibrach-post-action.git main"
        echo 'Revert done!'
    }
}    
                    