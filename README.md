Create Project
```
oc new-project dev  --display-name="Development"
```


Deploy with s2i `fabric8`
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



# Webhooks
After creating a BuildConfig` from a GitHub repository, run:

```
oc describe bc/movie-catalog-service
```

This will output a webhook GitHub URL that looks like:
``` 
https://console.c3smonkey.ch:8443/apis/build.openshift.io/v1/namespaces/development/buildconfigs/movie-catalog-service/webhooks/<secret>/github
```
Cut and paste this URL into GitHub, from the GitHub web console.
In your GitHub repository, select Add Webhook from Settings → Webhooks & Services.
Paste the URL output (similar to above) into the Payload URL field.




 