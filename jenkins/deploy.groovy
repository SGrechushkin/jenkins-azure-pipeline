pipeline {
    agent any
    stages {
        stage('Terraform Init & Apply') {
            steps {
                script {
                    withCredentials([
                        azureServicePrincipal(
                            credentialsId: 'a82541c6-ff6d-41f1-b707-223bf98238a0',
                            subscriptionIdVariable: 'AZURE_SUBSCRIPTION_ID',
                            clientIdVariable: 'AZURE_CLIENT_ID',
                            clientSecretVariable: 'AZURE_CLIENT_SECRET',
                            tenantIdVariable: 'AZURE_TENANT_ID'
                        )
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