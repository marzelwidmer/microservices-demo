# microservices-demo
Spring Boot Microservices 

## Clone all Git submodule
```
git clone --recursive git@github.com:marzelwidmer/microservices-demo.git
```

Example application for distributed tracing with OpenTracing and Jaeger post.

# Changelog
https://jenkins-jenkins.apps.c3smonkey.ch/job/jenkins/job/jenkins-order-service-pipeline/lastSuccessfulBuild/artifact/target/changelog.html


# oc cheatsheet
https://gist.github.com/tuxfight3r/79bddbf4af9b6d13d590670c40fec3e0#file-openshift_cli_tricks-md

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
https://devhints.io/git-log-format
