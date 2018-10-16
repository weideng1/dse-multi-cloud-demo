FROM ubuntu:16.04
MAINTAINER Sebastian Estevez estevezsebastian@gmail.com

# Install all apt-get utils and required repos
RUN apt-get update && \
    apt-get upgrade -y && \
    # Install add-apt-repository
    apt-get install -y \
        software-properties-common curl apt-transport-https python-pip groff less jq && \
    apt-get update

# Azure sources.list
RUN AZ_REPO=$(lsb_release -cs); echo "deb [arch=amd64] https://packages.microsoft.com/repos/azure-cli/ $AZ_REPO main" | \
    tee /etc/apt/sources.list.d/azure-cli.list

RUN curl -L https://packages.microsoft.com/keys/microsoft.asc | apt-key add -

# Google sources list
RUN export CLOUD_SDK_REPO="cloud-sdk-$(lsb_release -c -s)" && \
    echo "deb http://packages.cloud.google.com/apt $CLOUD_SDK_REPO main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list && \
    curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -


    # Install
RUN apt-get update && apt-get install -y \
	  git maven \
    apt-transport-https azure-cli \
    google-cloud-sdk

RUN add-apt-repository ppa:openjdk-r/ppa

RUN apt-get install -y openjdk-8-jdk 

RUN echo java --version

# AWS cli
RUN pip install awscli --upgrade --user
RUN echo 'PATH=/root/.local/bin/:$PATH' >> /root/.bashrc
ENV PATH=/root/.local/bin/:${PATH}
RUN echo $PATH

COPY ./config config
COPY ./config/.aws/ /root/.aws

# Check login AWS
#RUN aws ec2 describe-instances

# Login gcloud
RUN gcloud auth activate-service-account --key-file=/config/gcloud/gcloud-service-key.json
RUN cat /config/gcloud/gcloud-service-key.json | jq ".project_id" | xargs gcloud config set project 
#RUN gcloud config set project asset-hub-168516
# gcloud auth activate-service-account asset-hub@asset-hub-168516.iam.gserviceaccount.com --key-file=../../config/gcloud/gcloud-service-key.json

# Login azure
#RUN AZ_USER=$(head -1 config/azure/creds.txt)
#RUN AZ_PASSWORD=$(tail -1 config/azure/creds.txt)
#RUN az login -u $AZ_USER -p $AZ_PASSWORD

#RUN git clone https://github.com/phact/dse-multi-cloud-demo.git
COPY ./ /dse-multi-cloud-demo/


#RUN cd /dse-multi-cloud-demo/iaas && ./deploy_aws.sh
#RUN /dse-multi-cloud-demo/iaas/deploy_azure.sh
#RUN /dse-multi-cloud-demo/iaas/deploy_gcp.sh
