# microservices-demo
Spring Boot Microservices example application for [http://blog.marcelwidmer.org/](http://blog.marcelwidmer.org/){:target="_blank"}

## Clone all Git submodule
```
git clone --recursive git@github.com:marzelwidmer/microservices-demo.git
```

# Changelog
- [Catalog-Service Changelog](https://jenkins-jenkins.apps.c3smonkey.ch/job/jenkins/job/jenkins-order-service-pipeline/lastSuccessfulBuild/artifact/target/changelog.html){:target="_blank"}
- [Customer-Service Changelog](https://jenkins-jenkins.apps.c3smonkey.ch/job/jenkins/job/jenkins-customer-service-pipeline/lastSuccessfulBuild/artifact/target/changelog.html){:target="_blank"}
- [Order-Service Changelog](https://jenkins-jenkins.apps.c3smonkey.ch/job/jenkins/job/jenkins-order-service-pipeline/lastSuccessfulBuild/artifact/target/changelog.html){:target="_blank"}


# OKD cheatsheet
[oc cli tricks](https://gist.github.com/tuxfight3r/79bddbf4af9b6d13d590670c40fec3e0#file-openshift_cli_tricks-md){:target="_blank"}

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
- [git-log-format](https://devhints.io/git-log-format){:target="_blank"}
- [git-log](https://devhints.io/git-log){:target="_blank"}
- [git-tricks](https://devhints.io/git-tricks){:target="_blank"}
- [git-branch](https://devhints.io/git-branch){:target="_blank"}
- [git-revisions](https://devhints.io/git-revisions){:target="_blank"}
- [tig](https://devhints.io/tig){:target="_blank"}
- [git-extras](https://devhints.io/git-extras){:target="_blank"}






