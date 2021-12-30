pipeline {
    agent any 
    stages {
        stage('Download the student repo') {
            steps {
                dir('MAVEN') {
                git branch: 'main', credentialsId: 'GitLab', url: 'https://gitlab.com/clouddevops-b44/studentapp-ui.git'
                }
            }
        }
        stage('Download the Terraform-Ec2') {
            steps {
                 dir('EC2') {
                git branch: 'main', credentialsId: 'GitLab', url: 'https://gitlab.com/clouddevops-b44/terraform-ec2.git'
                }
            }
        }
        stage('Download the Terraform-VPC') {
            steps {
                 dir('VPC') {
                git branch: 'main', credentialsId: 'GitLab', url: 'https://gitlab.com/clouddevops-b44/terraform-vpc.git'
                 }
            }
        }
        stage('Listing EC2'){
            steps {
                dir('EC2') {
                sh "ls -ltr"
                }
            }
        }
        stage('Listing Student') {
            steps {
                 dir('MAVEN') {
                sh "ls -ltr"
                }
            }
        }
    }
}