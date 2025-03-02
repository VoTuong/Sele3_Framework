# Jenkins Docker Setup: Configuration Guide

## 1. Introduction
This document explains how to set up a Jenkins environment using Docker with the following features:
- Jenkins installation with Java 21 and Allure Report.
- Integration with a GitHub repository for automation testing.
- Running tests with Maven.
- Automatically generating and displaying an Allure Report after each run.
- Automatically sending the report via email (using Gmail with an App Password) with the subject line including the repository name and test run time.
- Using a Multibranch Pipeline to automatically scan for branches in your GitHub repository.
- All components are launched with a single Docker command.
---

## 2. Directory Structure
Create a project folder (e.g., `jenkins-automation`) with the following structure:

```plaintext
jenkins-automation/
├── Dockerfile
├── docker-compose.yml
├── plugins.txt
├── pipeline.groovy
└── init.groovy.d/
    └── email.groovy
```
---

## 3. Configuration Files

### a. Dockerfile
This Dockerfile installs Jenkins, Java 21 (Temurin from Adoptium), Allure CLI, and configures email settings by copying the required plugins and initialization scripts.

```dockerfile
FROM jenkins/jenkins:lts
ARG ALLURE_VER=2.32.2

USER root

# Install required tools and dependencies
RUN apt-get update && apt-get install -y \
    git \
    unzip \
    curl \
    wget \
    gnupg2 \
    lsb-release \
    && wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | apt-key add - \
    && echo "deb https://packages.adoptium.net/artifactory/deb $(lsb_release -cs) main" > /etc/apt/sources.list.d/adoptium.list \
    && apt-get update && apt-get install -y temurin-21-jdk \
    && rm -rf /var/lib/apt/lists/*

# Install Allure CLI
RUN curl -o allure-${ALLURE_VER}.tgz -L https://github.com/allure-framework/allure2/releases/download/${ALLURE_VER}/allure-${ALLURE_VER}.tgz \
    && tar -zxvf allure-${ALLURE_VER}.tgz -C /opt/ \
    && ln -s /opt/allure-${ALLURE_VER}/bin/allure /usr/local/bin/allure \
    && rm allure-${ALLURE_VER}.tgz

# Install Jenkins plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

# Copy initialization scripts (for email configuration, etc.)
COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/

USER jenkins
````
---

### b. docker-compose.yml
This file builds and runs the Jenkins container, mapping the necessary ports and persisting Jenkins data.

```yaml
version: '3.8'

services:
  jenkins:
    build: .
    container_name: jenkins-automation
    ports:
      - "8080:8080"
      - "50000:50000"
    volumes:
      - jenkins_home:/var/jenkins_home
    restart: unless-stopped

volumes:
  jenkins_home:
```

---

### c. plugins.txt
List the required Jenkins plugins that support Git, Maven, Allure Report, email notifications, and pipeline features.

```plaintext
email-ext
mailer
pipeline-utility-steps
git
allure-jenkins-plugin
htmlpublisher
workflow-aggregator
github
```

---

### d. init.groovy.d/email.groovy
This Groovy script auto-configures Jenkins email settings using Gmail and an App Password. Update the email addresses and password accordingly.

```groovy
import jenkins.model.*
import hudson.tasks.Mailer

def jenkinsLocation = JenkinsLocationConfiguration.get()
jenkinsLocation.adminAddress = 'jenkins@example.com'
jenkinsLocation.save()

def descriptor = Jenkins.instance.getDescriptorByType(Mailer.DescriptorImpl)
descriptor.smtpHost = 'smtp.gmail.com'
descriptor.smtpPort = '587'
descriptor.useSsl = false
descriptor.useTls = true
descriptor.charset = 'UTF-8'
descriptor.replyToAddress = 'jenkins@example.com'
descriptor.authUsername = 'your-email@gmail.com'
descriptor.authPassword = 'your-app-password' // Thay bằng App Password

descriptor.save()
```

---

### e. pipeline.groovy
This pipeline script defines the steps to clone the GitHub repository, run Maven tests, generate the Allure report, publish it in Jenkins, and send an email with the report. Adjust the repository URL and other commands as needed.

```groovy
pipeline {
    agent any

    environment {
        ALLURE_RESULTS_DIR = 'allure-results'
        ALLURE_REPORT_DIR = 'allure-report'
        REPO_NAME = 'YourAutomationRepo'
    }

    stages {
        stage('Clone Repo') {
            steps {
                git 'https://github.com/your-org/your-automation-repo.git'
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
```

---

## 4. How to Run the Project
1. Install **Docker and Docker Compose**:
2. Open a terminal in the project root directory
3. Run:
   ```bash
   docker-compose up --build -d
   ```
4. **Access Jenkins**: [http://localhost:8080](http://localhost:8080)

