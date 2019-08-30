Create Project
```
oc new-project dev  --display-name="Development"
```


Deploy with s2i `fabric8`
```
oc new-app fabric8/s2i-java~https://github.com/marzelwidmer/microservices-demo.git#master \
        --context-dir=movie-catalog-service \
        --name=movie-catalog-service \
         -n dev
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


 