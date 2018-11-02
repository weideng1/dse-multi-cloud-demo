#!/bin/bash

set -x

usage="--------------------------------------------------------------------------
Deploys vms in GCP based on parameters in ./gcp/clusterParameters.yaml
using Google Deployment Manager (deployment-manager).

Usage:
deploy.sh [-h] [-d deployment-name]

Options:

 -h                 : display this message and exit
 -d		              : name of GCP gcloud deployment [required]
 -p parameters      : parameters specified as follows: 
                      string-key:'string-value',integer-key:12345 
 -l labels          : labels specified as follows:
                      key:value,key:value,...

--------------------------------------------------------------------------"

while getopts 'hd:p:l:' opt; do
  case $opt in
    h) echo -e "$usage"
       exit 0
    ;;
    d) deploy="$OPTARG"
    ;;
    p) parameters="$OPTARG"
    ;;
    l) labels="$OPTARG"
    ;;
    \?) echo "Invalid option -$OPTARG" >&2
        exit 1
    ;;
  esac
done

echo "Deploying 'clusterParameters.yaml' in GCP gcloud deployment: $deploy"
if [ -z "${parameters}" ]; then
    gcloud deployment-manager deployments create $deploy --config ./gcp/clusterParameters.yaml --labels delpoyer-app=assethub,create_user=sebastian_estevez_datastax_com,org=presales
elif [ -z "${labels}" ]; then
    gcloud deployment-manager deployments create $deploy --template ./gcp/datastax.py --properties $parameters
else
    gcloud deployment-manager deployments create $deploy --template ./gcp/datastax.py --properties $parameters --labels $labels
fi
