# Create Project
```
oc new-project dev  --display-name="Development"
```

# Deploy with s2i `fabric8`
## catalog-service
```
oc new-app fabric8/s2i-java~https://github.com/marzelwidmer/microservices-demo.git#master \
        --context-dir=movie-catalog-service \
        --name=movie-catalog-service
```
Update BuildConfig with Secret from MongoDB
```
oc set env bc/movie-catalog-service --from="secret/mongodb" --prefix=MONGO_
```
Expose Route
```
oc expose svc/movie-catalog-service; oc get route movie-catalog-service
```

See build logs
```
oc logs -f bc/movie-catalog-service
```

Get BuildConfig as YAML 
``` 
oc get bc/movie-catalog-service -o yaml
```

## info-service
```
oc new-app fabric8/s2i-java~https://github.com/marzelwidmer/microservices-demo.git#master \
        --context-dir=movie-info-service \
        --name=movie-info-service
```
Update BuildConfig with Secret from MongoDB and Expose Service
```
oc set env bc/movie-info-service --from="secret/mongodb" --prefix=MONGO_ 
oc expose svc/movie-info-service
oc get route movie-info-service
```

See build logs
```
oc logs -f bc/movie-info-service
```

Get BuildConfig as YAML 
``` 
oc get bc/movie-info-service -o yaml
```

Start Build with CLI
```
oc start-build movie-info-service
oc logs -f bc/movie-info-service    
```


## rating-service
```
oc new-app fabric8/s2i-java~https://github.com/marzelwidmer/microservices-demo.git#master \
        --context-dir=movie-rating-service \
        --name=movie-rating-service
```
Update BuildConfig with Secret from MongoDB and Expose Service
```
oc set env bc/movie-rating-service --from="secret/mongodb" --prefix=MONGO_ ; oc expose svc/movie-rating-service; oc get route movie-rating-service
```

## WebHooks
After creating a `BuildConfig` from a GitHub repository, run:
```
oc describe bc/movie-catalog-service
oc describe bc/movie-info-service
oc describe bc/movie-rating-service
```
This will output a WebHook GitHub URL's 
> **_Note:_** The URL <secret> we will replace with a secret. This is just an place holder in the URL.
``` 
https://console.c3smonkey.ch:8443/apis/build.openshift.io/v1/namespaces/development/buildconfigs/movie-catalog-service/webhooks/<secret>/github
https://console.c3smonkey.ch:8443/apis/build.openshift.io/v1/namespaces/development/buildconfigs/movie-info-service/webhooks/<secret>/github
https://console.c3smonkey.ch:8443/apis/build.openshift.io/v1/namespaces/development/buildconfigs/movie-rating-service/webhooks/<secret>/github
```

To resolve the secret run we will replace the above value `<secret>` run the following command.
``` 
oc get bc/movie-catalog-service -o json | jq '.spec.triggers[].github.secret'
oc get bc/movie-info-service -o json | jq '.spec.triggers[].github.secret'
oc get bc/movie-rating-service -o json | jq '.spec.triggers[].github.secret'
```

Cut and paste this URL into GitHub, from the GitHub web console.
In your GitHub repository, select Add Webhook from Settings → Webhooks.
Paste the URL output (similar to above) into the Payload URL field.

> **_Hint:_** `SSL Disable (not recommended)` if your cluster don't have a valid SSL certificate.
```
SSL verification
 By default, we verify SSL certificates when delivering payloads.
```

![Add GitHub WebHook](/Add-GitHub-WebHook.png)

> **_Note:_**  This is a cheap Delivery  Pipeline . We have a multi Maven project so we create for every service a WebHook.


![GitHub WebHooks](/GitHub-WebHooks.png)

Now when we will push all projects will be build and deployed on Openshift.

![GitHub WebHooks](/Builds.png)


# Reset GitHub WebHooks
oc set triggers bc/catalog-service-pipeline --from-github
oc set triggers bc/catalog-service-pipeline --from-webhook
oc get bc/catalog-service-pipeline -o json | jq '.spec.triggers[].github.secret'



# Test API
## catalog-service
### Request :
```
http http://movie-catalog-service-development.apps.c3smonkey.ch/api/movies/catalog/foo/0000-0000-0000-0000-0001                                                                     1739ms  Sun Sep  1 12:04:48 2019
```
### Response :
```json
[
    {
        "id": "0000-0000-0000-0000-0001",
        "rating": 7.2
    }
]
```
## info-service
### Request :
```
http http://movie-info-service-development.apps.c3smonkey.ch/api/movies/info/0000-0000-0000-0000-0001
```
### Response :
```json
[
    {
        "desc": "Lawman Luke Hobbs and outcast Deckard Shaw form an unlikely alliance when a cyber-genetically enhanced villain threatens the future of humanity.",
        "id": "0000-0000-0000-0000-0001",
        "name": "Fast & Furious Presents: Hobbs & Shaw"
    }
]
```

## rating-service
### Request :
```
http  http://movie-rating-service-development.apps.c3smonkey.ch/api/movies/ratings/0000-0000-0000-0000-0001
```
### Response :
```json
[
    {
        "id": "0000-0000-0000-0000-0001",
        "rating": 7.2
    }
]
```