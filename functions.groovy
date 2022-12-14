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

def getCbDevelopersEmails(Map parameters = [:]) {
   def email = [:]
        email = [
            harel_karavani: "harel.karavani@checkmarx.com",
            simon_shkilevich: "simon.shkilevich@checkmarx.com",
        ]
    return email
}

def getCbDevelopersSlackUsers(Map parameters = [:]) {
   def slackUser = [:]
        slackUser = [
            ilanshtok: "Ilans"
            valeryb: "Valery Boltavsky",
            valbol: "Valery Boltavsky",
            'Nir_Cohen': "Nir Cohen",
            li0nbelenky: "Arie Belenky"
            'ronits_cx': "Ronit Steinberg",
            'Ronit Steinberg': "Ronit Steinberg",
            miriam_horlick: "Miriam Horlick",
            miriamso:  "Miriam Horlick",
            'Rivka Reich': "Rivka Reich",
            rivkareich: "Rivka Reich",
            cxsimon: "Simon Shkilevich",
            chedvakatze: "Chedva K",
            harelkaravani: "Harel Karavani",
            'Harel Karavani': "Harel Karavani"
        ]
    return slackUser
}