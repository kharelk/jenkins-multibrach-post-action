/**
 * Send email notification.
 *
 * @param emailTo
 * @param subject The subject of the mail.
 * @param body The body of the mail.
 *
 * Usage example: notification.sendEmailNotification('CxDevOps@checkmarx.com','email subject', 'email body')
 */

def sendEmailNotification(emailTo, subject, body) {
    log.infoMessage("Sending email")
    log.infoMessage("To: ${emailTo}")
    log.infoMessage("body: ${body}")
    log.infoMessage("subject: ${subject}")

    emailext (
        subject: "${subject}" ,
        body: body,
        recipientProviders: [[$class: 'RequesterRecipientProvider']],
        to: emailTo
      )
}

/**
 * Send email notification which contains last changes plugin.
 *
 * @param buildStatus The current build status
 * @param emailTo The reciepients list
 * @param deployEnvironment The deploy environment (dev/staging/production)
 * @param branchName The current git branch name (i.e develop)
 * @param repositoryUrl The git repository url
 * @param shortCommitHash
 * @param commitAuthorName
 *
 * Usage example: notification.sendLastChangesEmailNotification('currentBuild.result', deployEnvironment, branchName,
    repositoryUrl, shortCommitHash, commitAuthorName)
*/
def sendLastChangesEmailNotification(
  buildStatus = 'STARTED', emailTo, deployEnvironment, branchName,
  repositoryUrl, shortCommitHash, commitAuthorName) {
    buildStatus = buildStatus ?: 'SUCCESS'
    def publisher = LastChanges.getLastChangesPublisher "PREVIOUS_REVISION", "SIDE", "LINE", true, true, "", "", "", "", ""
    publisher.publishLastChanges()
    def htmlDiff = publisher.getHtmlDiff()
    writeFile file: 'build-diff.html', text: htmlDiff
      emailext (
        subject: "Jenkins - changes of ${env.JOB_NAME} #${env.BUILD_NUMBER}",
        attachmentsPattern: '**/*build-diff.html',
        mimeType: 'text/html',
        body: """<p>Build Status: ${buildStatus}</p>
          <p>Build URL: ${env.BUILD_URL}</p>
          <p>Environment: ${deployEnvironment}</p>
          <p>Repository URL: ${repositoryUrl}</p>
          <p>Branch Name: ${branchName} </p>
          <p>Short Commit Hash: ${shortCommitHash} </p>

          <p>See attached diff of build <b>${env.JOB_NAME} #${env.BUILD_NUMBER}</b>.</p>
          <p>Check build changes on Jenkins <b><a href="${env.BUILD_URL}/last-changes">here</a></b>.</p>
          """,
        to: emailTo )
}

/**
 * Send slack notification on deployment.
 *
 * @param buildStatus The current build status
 * @param deployEnvironment The deploy environment (dev/staging/production)
 * @param branchName The current git branch name (i.e develop)
 * @param repositoryUrl The git repository url
 * @param shortCommitHash
 * @param commitAuthorName
 *
 * Usage example: notification.slackNotification('currentBuild.result', deployEnvironment, branchName, repositoryUrl,
 shortCommitHash, commitAuthorName)
 */
def slackNotification(String buildStatus = 'STARTED',deployEnvironment, branchName, repositoryUrl,
shortCommitHash, commitAuthorName) {
    // build status of null means successful
    buildStatus = buildStatus ?: 'SUCCESS'
    def color = matchColorToBuildResult(buildStatus)

    // Default values
    def message = "Build Status: ${buildStatus} \n" +
      "Build URL: ${env.BUILD_URL} \n " +
      "Environment: " + deployEnvironment + " \n " +
      "Repository URL: " + repositoryUrl + " \n " +
      "Branch Name: " + branchName + " \n " +
      "Short Commit Hash: " + shortCommitHash + " \n " +
      "Commit Author: " + commitAuthorName

    slackSend(
      baseUrl: 'https://codebashing.slack.com/services/hooks/jenkins-ci/',
      channel: 'builds',
      color: "${color}",
      message: "${message}",
      teamDomain: 'codebashing',
      tokenCredentialId: 'SlackIntegrationToken'
    )
}



def slackPrNotification(Map parameters = [:]) {
  buildStatus = parameters.get('buildStatus', 'SUCCESS')
  repo_name  = parameters.get('repo_name')
  slackChannelName = parameters.get('slackChannelName')

    // build status of null means successful
    def color = matchColorToBuildResult(buildStatus)

    // Default values
    def message = "Build Status: ${buildStatus} \n" +
      "Build URL: ${env.BUILD_URL} \n " +
      "PR Name: " + env.CHANGE_TITLE + " \n " +
      "PR Authuor: " + env.CHANGE_AUTHOR_DISPLAY_NAME + " \n " +
      "PR GitHub URL: " + env.CHANGE_URL


    slackSend(
      channel: slackChannelName,
      color: color,
      message: message,
      teamDomain: 'codebashing',
    )
}

/**
* Send a notification to codebashing's slack team domain
*
* @param channel The channel to send the notification to
* @param color The message color
*
* Usage example: notification.simpleSlackNotification(channel, color, message)
*/
def simpleSlackNotification(channel, color, message) {
  log.infoMessage("simpleSlackNotification(${channel}, ${color}, ${message})")
  slackSend(
    channel: channel,
    color: color,
    message: message,
    teamDomain: 'codebashing',
  )
}

/**
* Match the color sends to slack to the build result
*
* @param buildStatus The current build status
*
* Usage example: notification.matchColorToBuildResult('currentBuild.result')
*/
def matchColorToBuildResult(buildStatus) {
  def color

  if (buildStatus == 'STARTED' || buildStatus == 'SUCCESS') {
      color = 'good'
    } else if (buildStatus == 'UNSTABLE') {
      color = 'warning'
    } else {
      color = 'danger'
    }

  return color
}

def slackClusterUpdated(Map parameters = [:]) {
  services = parameters.get('services')
  environment = parameters.get('environment')
  deployState = parameters.get('deployState')
  commitAuthorName = parameters.get('commitAuthorName')
  shortCommitHash = parameters.get('shortCommitHash')
  slackChannelName = parameters.get('slackChannelName', 'builds')

  def COLOR_MAP = [
    'SUCCESS': '#2EB886',   // Green
    'FAILURE': '#A30200',   // Red
    'UNSTABLE': '#DAA038',  // Yellow
    'ABORTED': '#BDC3C7'    // Grey
    ]
  def message = "## Build Status ##  \
    \nEnv name: "+ environment + " \
    \nServices: " + services + " \
    \nDeployState: " + deployState + " \
    \nCommit Author: " + commitAuthorName + " \
    \nTag: " + shortCommitHash

  slackSend(
    channel: slackChannelName,
    color: COLOR_MAP[deployState],
    message: message,
    teamDomain: 'codebashing',
  )
}

def sendCdEmailNotification(Map parameters = [:]) {
  buildStatus = parameters.get('buildStatus')
  emailTo = parameters.get('emailTo')
  lastCommitAuthorName = parameters.get('lastCommitAuthorName')

  def publisher = LastChanges.getLastChangesPublisher "PREVIOUS_REVISION", "SIDE", "LINE", true, true, "", "", "", "", ""
  publisher.publishLastChanges()
  def htmlDiff = publisher.getHtmlDiff()
  writeFile file: 'build-diff.html', text: htmlDiff

  emailext (
    subject: "Jenkins - ${env.JOB_NAME} #${env.BUILD_NUMBER} ${buildStatus}",
    // This will send to all culprits  commiters and suspects of who broke the build.
    recipientProviders: [culprits(),brokenBuildSuspects()],
    attachmentsPattern: '**/*build-diff.html',
    mimeType: 'text/html',
    body: """<p>Build Status: ${buildStatus}</p>
      <p>Build URL: ${env.BUILD_URL}</p>
      <p>Deploy State: ${buildStatus} </p>
      <p>Last commit by: ${lastCommitAuthorName} </p>
      <br><p>Attached you can find a build diff or go to the link below</p>
      <p>Build Changes: ${env.BUILD_URL}changes </p>

      <br><p>Best,</p>
      <p>Jenkins Admin</p>
      """,
    to: emailTo
    )
}

def customSlackNotification(Map parameters = [:]) {
  message = parameters.get('message')
  color = parameters.get('color', '#ff6620')
  slackChannelName = parameters.get('slackChannelName')

  log.infoMessage("customSlackNotification(${slackChannelName}, ${color}, ${message})")
  slackSend(
    channel: slackChannelName,
    color: color,
    message: message,
    teamDomain: 'codebashing',
  )
}
