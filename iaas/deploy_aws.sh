#!/bin/bash

set -x

currentdir=`dirname $0`
region='us-west-2'
stackname='multi'
usage="---------------------------------------------------
Deploys vms based on params in ./aws/params.json
in a CFn stack.

Usage:
deploy.sh [-h] [-r region] [-s stack]

Options:

 -h                 : display this message and exit
 -r region          : AWS region where 'stack' will be deployed,
                      default us-west-2
 -s stack           : name of AWS CFn stack to deploy,
                      default 'multi'
 -p parameters      : parameters specified as follows: 

                      ParameterKey=KeyPairName,ParameterValue=MyKey ParameterKey=InstanceType,ParameterValue=t1.micro ...

                      without this flag the script uses params.json

---------------------------------------------------"

while getopts 'hr:s:p:' opt; do
  case $opt in
    h) echo -e "$usage"
       exit 0
    ;;
    r) region="$OPTARG"
    ;;
    s) stackname="$OPTARG"
    ;;
    p) params="$OPTARG"
    ;;
    \?) echo "Invalid option -$OPTARG" >&2
        exit 1
    ;;
  esac
done

if [ -z "$params" ]; then
  params="file://${currentdir}/aws/params.json"
fi

echo "Deploying 'datacenter.template' in stack $stackname in region $region"
aws cloudformation create-stack  \
--region $region \
--stack-name $stackname  \
--disable-rollback  \
--capabilities CAPABILITY_IAM  \
--template-body file://${currentdir}/aws/datacenter.template  \
--parameters ${params}
echo "Waiting for stack to complete..."
aws cloudformation wait stack-create-complete --stack-name $stackname
