pipeline {
    agent any

    environment {
        ALLURE_RESULTS_DIR = 'allure-results'
        ALLURE_REPORT_DIR = 'allure-report'
        REPO_NAME = 'Sele3_Framework'
    }

    stages {
        stage('Clone Repo') {
            steps {
                git 'https://github.com/VoTuong/Sele3_Framework.git'
            }
        }

        stage('Build & Run Tests') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Generate Allure Report') {
            steps {
                sh "allure generate ${ALLURE_RESULTS_DIR} -o ${ALLURE_REPORT_DIR} --clean"
            }
        }

        stage('Publish Report') {
            steps {
                publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'allure-report',
                        reportFiles: 'index.html',
                        reportName: 'Allure Report'
                ])
            }
        }
    }

    post {
        always {
            script {
                def runTime = new Date().format("yyyy-MM-dd HH:mm:ss")
                emailext(
                        subject: "Test Report for ${env.REPO_NAME} - ${runTime}",
                        body: "Báo cáo test đã được tạo. Xem tại: ${env.BUILD_URL}",
                        to: "dtuong.vo@gmail.com",
                        attachmentsPattern: "**/allure-report/index.html",
                        mimeType: 'text/html'
                )
            }
        }
    }
}