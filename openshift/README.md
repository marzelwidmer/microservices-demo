See: https://github.com/openshift/origin/tree/master/examples/jenkins

```bash
oc new-project dev  --display-name="Development"
```









# OpenShift

## Login

``` 
oc login <MASTER-URL>
```

## Search Jenkins OpenShift Template

```
oc new-app -S jenkins
```

### Install Jenkins on OpenShift with Blue Ocean plugin
```
oc new-build jenkins:2~https://github.com/siamaksade/jenkins-blueocean.git \
                     --name=jenkins-blueocean
```


```$xslt
                                           --name=jenkins-blueocean
--> Found image 6852735 (5 weeks old) in image stream "openshift/jenkins" under tag "2" for "jenkins:2"

    Jenkins 2 
    --------- 
    Jenkins is a continuous integration server

    Tags: jenkins, jenkins2, ci

    * A source build using source code from https://github.com/siamaksade/jenkins-blueocean.git will be created
      * The resulting image will be pushed to image stream "jenkins-blueocean:latest"
      * Use 'start-build' to trigger a new build

--> Creating resources with label build=jenkins-blueocean ...
    buildconfig "jenkins-blueocean" created
--> Success
    Build configuration "jenkins-blueocean" created and build triggered.
    Run 'oc logs -f bc/jenkins-blueocean' to stream the build progress.
    
```




#### Deploy customized Jenkins Ephemeral docker image
```
oc new-app  -f https://raw.githubusercontent.com/openshift/origin/master/examples/jenkins/jenkins-ephemeral-template.json \
    -p NAMESPACE=keepcalm \
    -p JENKINS_IMAGE_STREAM_TAG=jenkins-blueocean:latest \
    -p MEMORY_LIMIT=2Gi
```



```
oc create -f https://raw.githubusercontent.com/jboss-openshift/application-templates/master/jboss-image-streams.json
```