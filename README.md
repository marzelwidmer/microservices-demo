# microservices-demo
Spring Boot Microservices example application for [http://blog.marcelwidmer.org/](http://blog.marcelwidmer.org/)

## Clone all Git submodule
```
git clone --recursive git@github.com:marzelwidmer/microservices-demo.git
```

# Changelog
- [Catalog-Service Changelog](https://jenkins-jenkins.apps.c3smonkey.ch/job/jenkins/job/jenkins-order-service-pipeline/lastSuccessfulBuild/artifact/target/changelog.html)
- [Customer-Service Changelog](https://jenkins-jenkins.apps.c3smonkey.ch/job/jenkins/job/jenkins-customer-service-pipeline/lastSuccessfulBuild/artifact/target/changelog.html)
- [Order-Service Changelog](https://jenkins-jenkins.apps.c3smonkey.ch/job/jenkins/job/jenkins-order-service-pipeline/lastSuccessfulBuild/artifact/target/changelog.html)


## Deploy to Development from local maschin
```bash
$ ./mvnw clean fabric8:deploy -pl catalog-service,customer-service,order-service -Dfabric8.namespace=development
```

# OKD cheatsheet
[oc cli tricks](https://gist.github.com/tuxfight3r/79bddbf4af9b6d13d590670c40fec3e0#file-openshift_cli_tricks-md)

Show not Running POD's
```bash
$ oc get pods --field-selector=status.phase!=Running
```

Show only Running POD's
```bash
$ oc get pods --field-selector=status.phase=Running
```

Get Secret
```bash
$ oc get bc/catalog-service-pipeline -n jenkins -o json | jq '.spec.triggers[].github.secret'
```

Get Route Host
```bash
$ oc get route/jaeger-collector -o json | jq '.spec.host'e
```

Tail log of POD
```bash
$ oc logs -f order-service-22-cqqn4 --tail=50
```

# Git cheatsheet
- [git-log-format](https://devhints.io/git-log-format)
- [git-log](https://devhints.io/git-log)
- [git-tricks](https://devhints.io/git-tricks)
- [git-branch](https://devhints.io/git-branch)
- [git-revisions](https://devhints.io/git-revisions)
- [tig](https://devhints.io/tig)
- [git-extras](https://devhints.io/git-extras)



# Jaeger
[java-spring-jaeger](https://github.com/opentracing-contrib/java-spring-jaeger/blob/master/README.md)

## Create Jaeger Project
```bash
$ oc new-project jaeger --display-name="Distributed Tracing System" 
```

## Install Jaeger
Install Jaeger on OpenShift to collect the traces
```bash
$ oc process -f https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/all-in-one/jaeger-all-in-one-template.yml | oc create -f -
```

## Create Route
Create a route to access the Jaeger collector
```bash
$ oc expose service jaeger-collector --port=14268 -n jaeger
```

## Get Route Host
Get the route address
```bash
$ oc get route/jaeger-collector -n jaeger -o json | jq '.spec.host'
```

## Update Spring Configuration

```yaml
opentracing:
  jaeger:
    log-spans: true
    http-sender:
      url: http://jaeger-collector-jaeger.apps.c3smonkey.ch/api/traces
```

## Jaeger UI
[Jaeger UI](https://jaeger-query-jaeger.apps.c3smonkey.ch/search)



# ConfigMap
Apply `ConfigMap`
```bash
$ oc apply -f deployments/configmap.yaml
```

## Additional ConfigMap Commands
Create `ConfigMap` from file.
```bash
$ oc create configmap order-service --from-file=src/main/resources/application.yaml
```

Get all `ConfigMaps`
```bash
$ oc get configmaps
```

Get `ConfigMap` as `yaml`
```bash
$ oc get configmap order-service -o yaml
```

Describe `ConfigMap`
```bash
$ oc describe configmap order-service
```

Delete `ConfigMap`
```bash
$ oc delete configmap order-service
```

# Watch running POD
```bash
$ watch oc get pods --field-selector=status.phase=Running                                                                         28.6m  Thu Sep 19 16:14:40 2019
```

# Tail logfile
```bash
$ oc logs -f order-service-37-hh2tb
```





