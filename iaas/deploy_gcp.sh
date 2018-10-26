#!/bin/bash

usage="--------------------------------------------------------------------------
Deploys vms in GCP based on parameters in ./gcp/clusterParameters.yaml
using Google Deployment Manager (deployment-manager).

Usage:
deploy.sh [-h] [-d deployment-name]

Options:

 -h                 : display this message and exit
 -d		    : name of GCP gcloud deployment [required]
 -p parameters      : parameters specified as follows: 
                      string-key:'string-value',integer-key:12345 

--------------------------------------------------------------------------"

while getopts 'hd:p:' opt; do
  case $opt in
    h) echo -e "$usage"
       exit 0
    ;;
    d) deploy="$OPTARG"
    ;;
    d) parameters="$params"
    ;;
    \?) echo "Invalid option -$OPTARG" >&2
        exit 1
    ;;
  esac
done

echo "Deploying 'clusterParameters.yaml' in GCP gcloud deployment: $deploy"
if [ -z $parameters ]; then
    gcloud deployment-manager deployments create $deploy --config ./gcp/clusterParameters.yaml --labels delpoyer-app=assethub,create_user=sebastian_estevez_datastax_com,org=presales
else
    gcloud deployment-manager deployments create $deploy --config ./gcp/clusterParameters.yaml --labels delpoyer-app=assethub,create_user=sebastian_estevez_datastax_com,org=presales --properties ${params}
fi
