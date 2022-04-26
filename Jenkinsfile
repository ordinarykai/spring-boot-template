node {
 
    properties([
            disableConcurrentBuilds(),
            gitLabConnection(gitLabConnection: 'gitlab'),
            buildDiscarder(logRotator(numToKeepStr: '2')),
    ])
 
    stage('Checkout') {
        echo '开始拉取代码.....'
        git branch: "master", credentialsId: 'Gitlab-Credential', url: 'http://192.168.2.105:9091/xielong/diamond.git'
 
    }
 
    stage('Build') {
        withMaven (
            maven: 'M3',  mavenSettingsConfig: 'Maven-Config'
        ) {
            updateGitlabCommitStatus name: 'build', state: 'running'
            echo '开始执行打包操作.....'
            sh 'mvn clean package'
        }
    }
 
    updateGitlabCommitStatus name: 'build', state: 'success'
 
}