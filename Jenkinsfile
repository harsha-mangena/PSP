// CI pipeline for the PSP monorepo.
//
// Scope, deliberately: backend unit/context tests (H2, no Kafka listeners -
// see scripts/test-backend.sh) and the frontend lint/build. It does NOT spin
// up docker-compose (SQL Server + Kafka) or run the Puppeteer verify-*.mjs
// E2E suite, since that needs the full stack up on ports this machine also
// runs Jenkins on (8080) - a separate pipeline can take that on later.
pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timestamps()
        disableConcurrentBuilds()
    }

    triggers {
        // GitHub can't webhook a localhost-only Jenkins, so poll instead.
        pollSCM('H/5 * * * *')
    }

    environment {
        // The launchd job Homebrew installs for Jenkins doesn't source
        // .zshrc/.bashrc, so nvm's node is invisible unless added here.
        PATH = "/opt/homebrew/opt/openjdk@17/bin:${HOME}/.nvm/versions/node/v24.18.0/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend') {
            matrix {
                axes {
                    axis {
                        name 'SERVICE'
                        values 'discovery-server', 'product-service', 'cart-service', 'api-gateway'
                    }
                }
                stages {
                    stage('Build & Test') {
                        steps {
                            dir("backend/${SERVICE}") {
                                sh './mvnw -B -ntp clean verify'
                            }
                        }
                        post {
                            always {
                                junit testResults: "backend/${SERVICE}/target/surefire-reports/*.xml", allowEmptyResults: true
                            }
                        }
                    }
                }
            }
        }

        stage('Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm ci'
                    sh 'npm run lint'
                    sh 'npm run build'
                }
            }
        }

        // Builds a tagged image per service from the jar/bundle the stages
        // above already produced. Only builds/tags locally - no registry
        // push is configured, since that needs registry credentials this
        // pipeline doesn't have yet.
        stage('Docker Images') {
            parallel {
                stage('discovery-server') {
                    steps {
                        sh "docker build -t psp/discovery-server:${env.BUILD_NUMBER} -t psp/discovery-server:latest backend/discovery-server"
                    }
                }
                stage('product-service') {
                    steps {
                        sh "docker build -t psp/product-service:${env.BUILD_NUMBER} -t psp/product-service:latest backend/product-service"
                    }
                }
                stage('cart-service') {
                    steps {
                        sh "docker build -t psp/cart-service:${env.BUILD_NUMBER} -t psp/cart-service:latest backend/cart-service"
                    }
                }
                stage('api-gateway') {
                    steps {
                        sh "docker build -t psp/api-gateway:${env.BUILD_NUMBER} -t psp/api-gateway:latest backend/api-gateway"
                    }
                }
                stage('frontend') {
                    steps {
                        sh "docker build -t psp/frontend:${env.BUILD_NUMBER} -t psp/frontend:latest frontend"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build, backend tests, and frontend build/lint all passed.'
        }
        failure {
            echo 'Pipeline failed - check the stage view above for which service broke.'
        }
    }
}
