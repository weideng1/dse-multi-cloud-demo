#!/bin/bash

rg='multi'
loc="westus"
usage="---------------------------------------------------
Deploys vms based on params in ./azure/params.json to
a resource group.

Usage:
deploy.sh [-h] [-l region] [-g resource-group]

Options:

 -h                 : display this message and exit
 -l region          : Azure region where 'resource-group' will be deployed,
                      default westus2
 -g resource-group  : name of resource-group to deploy,
                      default 'multi'
 -p parameters      : parameters specified as follows: 
                      \"{\"newStorageAccountName\":
                      {\"value\": \"acctname\"},\"adminUsername\": {\"value\": \"seb\"},
                      \"adminPassword\": {\"value\": \"iForgot\"},
                      \"dnsNameForPublicIP\": {\"value\": \"puppies\"}}\"

---------------------------------------------------"

while getopts 'hl:g:p:' opt; do
  case $opt in
    h) echo -e "$usage"
       exit 0
    ;;
    l) loc="$OPTARG"
    ;;
    g) rg="$OPTARG"
    ;;
    p) parameters="$OPTARG"
    ;;
    \?) echo "Invalid option -$OPTARG" >&2
        exit 1
    ;;
  esac
done

rand=$(LC_ALL=C tr -cd '[:alnum:]' < /dev/urandom | tr -cd '[:lower:]' | fold -w10 | head -n1)

az group create --name $rg --location $loc
az group deployment create \
--resource-group $rg \
--template-file ./azure/template-vnet.json \
--verbose

if [ -z $parameters ]; then
  $parameters="@./azure/params.json"
fi

az group deployment create \
--resource-group $rg \
--template-file ./azure/nodes.json \
--parameters ${parameters} \
--parameters '{"uniqueString": {"value": "'$rand'"}}' \
--verbose
