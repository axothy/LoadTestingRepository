СБОРКА:

1) Собираем проект

gradle build -x test

2) Собираем Dockerfile из терминала в проекте

docker build -t axothy/airline-company-application:latest airline-company-application

3) Пушим в Docker Hub

docker push axothy/airline-company-application:latest

----------------------

ДЕПЛОЙ:

1) Применяем манифесты

kubectl apply -f airline-company-pipeline/k8s/base/dc.yaml
kubectl apply -f airline-company-pipeline/k8s/base/svc.yaml

----------------------


ПОЛЕЗНЫЕ КОМАНДЫ:

зачистить проект
kubectl delete deployment --all
kubectl delete service --all

поды и их ребут
kubectl get pods
kubectl delete pods --all

инфа о нодах
kubectl get nodes -o wide

удалить только приклад
kubectl delete deployment airline-company-application-dc
kubectl delete service airline-company-application-svc
