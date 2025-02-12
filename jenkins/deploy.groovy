pipeline {
    agent any
    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/SGrechushkin/jenkins-azure-pipeline'
            }
        }
        stage('Terraform Init') {
            steps {
                sh 'terraform init'
            }
        }
        stage('Terraform Apply') {
            steps {
                sh 'terraform apply -auto-approve'
            }
        }
        stage('Ansible Setup') {
            steps {
                sh 'ansible-playbook ansible/playbook.yml'
            }
        }
    }
}
