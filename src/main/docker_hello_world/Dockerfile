# build with:
#     docker build -t hello_nginx .
#
# run directly with:
#     docker rm -f hello_nginx_container  &&  docker run --name hello_nginx_container -i -t -p 80:80 hello_nginx
#
# run with docker compose:
#     docker compose up
#
# Check application at:  http://localhost/
#
# Push it to Docker Hub
#   docker tag hello_nginx mrmos/hello_nginx:latest
#   docker login
#   docker push mrmos/hello_nginx:latest
#  --> check if it is available: https://hub.docker.com/u/mrmos
#





FROM nginx:alpine

RUN rm /etc/nginx/conf.d/*
ADD helloworld.conf /etc/nginx/conf.d/
ADD index.html /usr/share/nginx/html/

