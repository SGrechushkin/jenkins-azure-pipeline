pipeline {
    agent any
    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/SGrechushkin/jenkins-azure-pipeline'
            }
        }
        stage('Terraform Init') {
            steps {
                sh 'cd terraform && terraform init'
            }
        }
        stage('Terraform Apply') {
            steps {
                sh '''
                cd terraform
                terraform apply -auto-approve -var="subscription_id=${ARM_SUBSCRIPTION_ID}"
                '''
            }
        }
        stage('Ansible Setup') {
            steps {
                sh 'cd ansible && ansible-playbook playbook.yml'
            }
        }
    }
}