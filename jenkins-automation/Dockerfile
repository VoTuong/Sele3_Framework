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
    maven \
    && wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | apt-key add - \
    && echo "deb https://packages.adoptium.net/artifactory/deb $(lsb_release -cs) main" > /etc/apt/sources.list.d/adoptium.list \
    && apt-get update && apt-get install -y temurin-21-jdk \
    && rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME and update PATH to use Java 21
ENV JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
ENV PATH="$JAVA_HOME/bin:${PATH}"

# Configure update-alternatives to use Java 21 as default
RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/temurin-21-jdk-amd64/bin/java 210 && \
    update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/temurin-21-jdk-amd64/bin/javac 210 && \
    update-alternatives --set java /usr/lib/jvm/temurin-21-jdk-amd64/bin/java && \
    update-alternatives --set javac /usr/lib/jvm/temurin-21-jdk-amd64/bin/javac

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