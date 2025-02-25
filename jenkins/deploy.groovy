pipeline {
    agent any
    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/SGrechushkin/jenkins-azure-pipeline'
            }
        }
        stage('Terraform Init & Apply') {
            steps {
                script {
                    withCredentials([
                        string(credentialsId: 'azure-subscription-id', variable: 'AZURE_SUBSCRIPTION_ID'),
                        string(credentialsId: 'azure-client-id', variable: 'AZURE_CLIENT_ID'),
                        string(credentialsId: 'azure-client-secret', variable: 'AZURE_CLIENT_SECRET'),
                        string(credentialsId: 'azure-tenant-id', variable: 'AZURE_TENANT_ID')
                    ]) {
                        sh '''
                        cd terraform
                        export TF_VAR_subscription_id=$AZURE_SUBSCRIPTION_ID
                        export TF_VAR_client_id=$AZURE_CLIENT_ID
                        export TF_VAR_client_secret=$AZURE_CLIENT_SECRET
                        export TF_VAR_tenant_id=$AZURE_TENANT_ID
                        
                        echo "DEBUG: TF_VAR_subscription_id is set to $TF_VAR_subscription_id"
                        
                        terraform init
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