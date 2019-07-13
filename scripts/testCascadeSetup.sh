kubectl run test --image=ubuntu --restart=Never --command -- sleep infinity
uid=$(kubectl get pod/test -o=jsonpath='{.metadata.uid}')
kubectl run deploy-test --image=ubuntu --replicas=2 --overrides='{"metadata":{"ownerReferences":[{"apiVersion":"v1","kind":"Pod","controller":true,"blockOwnerDeletion":true,"name":"test","uid":"'"$uid"'"}]}}' --command -- sleep infinity
