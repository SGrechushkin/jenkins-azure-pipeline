pipeline {
    agent any
    environment {
        AZURE_CLIENT_ID = 'your-client-id'
        AZURE_CLIENT_SECRET = 'your-client-secret'
        AZURE_TENANT_ID = 'your-tenant-id'
        AZURE_SUBSCRIPTION_ID = 'your-subscription-id'
    }
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
                terraform apply -auto-approve
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