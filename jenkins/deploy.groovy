pipeline {
    agent any
    environment {
        // Задайте тут змінні середовища для ваших Azure credentials, якщо потрібно
        AZURE_CLIENT_ID = credentials('a82541c6-ff6d-41f1-b707-223bf98238a0')  // Приклад для string credentials
        AZURE_CLIENT_SECRET = credentials('a82541c6-ff6d-41f1-b707-223bf98238a0') //ghb
    }
    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/SGrechushkin/jenkins-azure-pipeline'
            }
        }
        stage('Terraform Init') {
            steps {
                script {
                    // Тут можете використати ці змінні для ініціалізації Terraform
                    withCredentials([string(credentialsId: 'a82541c6-ff6d-41f1-b707-223bf98238a0', variable: 'AZURE_CLIENT_ID')]) {
                        sh 'cd terraform && terraform init'
                    }
                }
            }
        }
        stage('Terraform Apply') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'a82541c6-ff6d-41f1-b707-223bf98238a0', variable: 'AZURE_CLIENT_SECRET')]) {
                        sh '''
                        cd terraform
                        terraform apply -auto-approve
                        '''
                    }
                }
            }
        }
        stage('Ansible Setup') {
            steps {
                sh 'cd ansible && ansible-playbook playbook.yml'
            }
        }
    }
}
