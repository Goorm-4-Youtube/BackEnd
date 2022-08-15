pipeline { 
  agent any

  environment {
      repository = "seonwoohongmin/backend"  // repository name of your docker hub 
      DOCKERHUB_CREDENTIALS = credentials('dockerhub') // jenkins에 등록해 놓은 docker hub credentials 이름
      dockerImage = ' ' 
  }
  stages {
      stage('Cloning back-end Git') {
            steps { 
		checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Goorm-4-Youtube/backEnd.git']]]
            }
        } 
      stage('Build an image') { 
          steps { 
              script { 
	          sh "cp /var/jenkins_home/workspace/BackEnd_JarBiulde/target/youtube-clone-0.0.1-SNAPSHOT.jar /var/jenkins_home/workspace/BackEnd_Pipeline" // jar 파일을 현재 위치로 복사 
                  sh 'docker build -t $repository:v$BUILD_NUMBER .' // docker image build
              }
          } 
      }
      stage('Dockerhub Login'){
          steps{
              sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin' // docker hub login
          }
      }
      stage('Push our back-end image') { 
          steps { 
              script {
                sh 'docker push $repository:v$BUILD_NUMBER' //docker image push
              } 
          }
      } 
      stage('Cleaning up') { 
		  steps { 
              sh "docker rmi $repository:v$BUILD_NUMBER" // docker image remove
          }
      }
      stage('Manifest Update') { 
		  steps { 
                      checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Goorm-4-Youtube/Manifest.git']]]
			  sh "cd backend"
			  sh "sed -i 's/backend:v.*/backend:v$BUILD_NUMBER/' ./deployment.yaml  "
			  sh "cd .."
            sh "git add ."
            sh "git commit -m '[backend] image versioning v$BUILD_NUMBER '"
            sshagent(credentials('github')) {
                sh "git push -u origin master"
          }
      } 
	  
  }
    }
