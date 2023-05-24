node('slave1'){


	def dev_counter = 0
    String[] a = JOB_NAME.split('/')

    
    stage('test'){

        sh "sonar-scanner --version"

        sh "git --version"

    }

    

   stage('git clone'){

        sh "rm -rf *"

        sh "git clone git@bitbucket.org:t500/${a[1]}.git --branch ${env.BRANCH_NAME} --single-branch"

    }

     stage('sonar-scan'){

        withSonarQubeEnv('sonar-dev'){
		//sh "sonar-scanner -Dsonar.projectKey=${a[1]} -Dsonar.projectName=${a[1]} -Dsonar.source=. -Dsonar.branch.name=${env.BRANCH_NAME}"

             sh "sonar-scanner -Dsonar.projectKey=randomkey -Dsonar.projectName=${JOB_NAME} -Dsonar.source=."

         }

     }
     stage("Quality Gate"){

         timeout(time: 1, unit: 'HOURS') { 

             def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv

             if (qg.status != 'OK') {

             error "Pipeline aborted due to quality gate failure: ${qg.status}"

             }

        }

     }

}