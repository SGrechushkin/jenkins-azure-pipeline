pipeline {
    agent any
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/your_repo.git'
            }
        }
        stage('Terraform Destroy') {
            steps {
                sh 'cd terraform && terraform destroy -auto-approve'
            }
        }
    }
}
