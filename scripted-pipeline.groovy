node() {
    stage('Download the code') {
        git branch: 'main', credentialsId: 'GitLab', url: 'https://gitlab.com/clouddevops-b44/ansible.git'
    }
    
    stage('List the files') {
        sh 'ls -ltr'
    }   
}