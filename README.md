# multi-cloud-service

Is meant to run in a docker container.

Fill out the creds in the config directory and run:

    docker build -t multi-cloud ./

    docker run -p 8083:8083 --name multi-cloud -t multi-cloud 
