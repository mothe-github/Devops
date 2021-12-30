pipeline {
    agent any 
    environment {
        sonarUrl = "13.235.77.47"
        sonarToken = "3b7c32a20ec09688425c7f3765d078d1deaf6646"
        majorVersion = "0"
        minorVersion = "0"
        patchVersion = "???????? Automatically fetche ??????????"
    }
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

        stage('Download the Terraform-RDS') {
            steps {
                 dir('RDS') {
                git branch: 'main', credentialsId: 'GitLab', url: 'https://gitlab.com/clouddevops-b44/terraform-rds.git'
                 }
            }
        }

        stage('Compile Code') {
            steps {
                dir('MAVEN') {
                sh 'mvn clean compile'
                sh 'ls -ltr target'
                }
            }
        }

        // stage('Code Quality Analysis'){
        //     steps {
        //         withSonarQubeEnv('My SonarQube Server') {
        //         sh 'mvn sonar:sonar   -Dsonar.projectKey=student   -Dsonar.host.url=http://${sonarUrl}:9000   -Dsonar.login=${sonarToken}'
        //         }
        //     }
        // }

        // stage("Quality Gate") {
        //     steps {
        //       timeout(time: 1, unit: 'MINUTES') {
        //         waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }
        
        stage('Build the artifacts'){
            steps {
                dir('MAVEN') {
                sh 'mvn clean package'
                sh 'ls -ltr target/'
                sh 'env'
               // q q sh 'ls -ltr  ${WORKSPACE}/target/studentapp-studentapp.war'
                }
            }
        }
        stage('Unit and Integration Tests'){
            steps {
                dir('MAVEN') {
                sh "whoami"
                sh 'echo Running Unit and Integrations tests completed'
                // mvn test && mvn verify   ( mvn test : Unit Testing , mvn verify : Integration Testint ) 
                }
            }
        }
// This phase is to have the deployment on the same instance again and again
        // stage('Deploying the artifact to Test'){
        //     steps {
        //         dir('MAVEN') {
        //         sh 'pwd && ls -ltr'
        //         sh 'ansible-playbook -i ansible/hosts ansible/deploy.yml'
        //         }
        //     }
        // }   

        stage('Uploading the Artifact to S3 Bucket'){
            steps {
                dir('MAVEN') {
                   withCredentials([usernamePassword(credentialsId: 'AWSKEYS', passwordVariable: 'AWSSECRETKEY', usernameVariable: 'AWSACCESSKEY')]) {
                    sh '''
                        export AWS_ACCESS_KEY_ID=$AWSACCESSKEY
                        export AWS_SECRET_ACCESS_KEY=$AWSSECRETKEY
                        export AWS_DEFAULT_REGION=us-east-2
                        aws s3 ls
                        ls -ltr target/
                        pwd
                        cp target/studentapp-studentapp.war studentapp-${majorVersion}-${minorVersion}-${BUILD_NUMBER}.war
                        aws s3 cp studentapp-${majorVersion}-${minorVersion}-${BUILD_NUMBER}.war s3://student-artifacts/
                    '''
                   }
                }
            }
        } 

        stage('Terraform VPC '){
            steps {
                dir('VPC') {
                   withCredentials([usernamePassword(credentialsId: 'AWSKEYS', passwordVariable: 'AWSSECRETKEY', usernameVariable: 'AWSACCESSKEY')]) {
                    sh '''
                        export AWS_ACCESS_KEY_ID=$AWSACCESSKEY
                        export AWS_SECRET_ACCESS_KEY=$AWSSECRETKEY
                        export AWS_DEFAULT_REGION=us-east-2
                        echo $PATH
                        env
                        aws s3 ls
                        terraform init -backend-config=config/dev-backend.tfvars
                        terraform apply -auto-approve  -var-file=config/dev.tfvars
                    '''
                   }
                }
            }
        } 

        stage('Terraform EC2'){
            steps {
                dir('EC2') {
                    withCredentials([usernamePassword(credentialsId: 'AWSKEYS', passwordVariable: 'AWSSECRETKEY', usernameVariable: 'AWSACCESSKEY')]) {
                    sh '''
                        export AWS_ACCESS_KEY_ID=$AWSACCESSKEY
                        export AWS_SECRET_ACCESS_KEY=$AWSSECRETKEY
                        export AWS_DEFAULT_REGION=us-east-2
                        echo $PATH
                        env
                        terraform init -backend-config=config/dev-backend.tfvars
                        terraform apply -auto-approve  -var-file=config/dev.tfvars
                    '''
                        }
                   }
               }
            }

        stage('Terraform RDS'){
            steps {
                dir('RDS') {
                    withCredentials([usernamePassword(credentialsId: 'AWSKEYS', passwordVariable: 'AWSSECRETKEY', usernameVariable: 'AWSACCESSKEY')]) {
                    sh '''
                        export AWS_ACCESS_KEY_ID=$AWSACCESSKEY
                        export AWS_SECRET_ACCESS_KEY=$AWSSECRETKEY
                        export AWS_DEFAULT_REGION=us-east-2
                        echo $PATH
                        env
                        terraform init -backend-config=config/dev-backend.tfvars
                        terraform apply -auto-approve  -var-file=config/dev.tfvars
                    '''
                        }
                   }
               }
            }

        stage('Deploying the artifacts') {
            steps {
                dir('MAVEN') {
                    sh "cd ansible"
                    sh "cat /tmp/hosts-from-terraform"
                    sh "ansible-playbook -i /tmp/hosts-from-terraform ansible/deploy.yml"
                 }
            }
        } 
    }
}

