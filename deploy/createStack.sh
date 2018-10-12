#!/bin/bash

display_usage() { 
    echo -e "\nUsage:\n$0 --env <environment/stack name> [--clean] \n" 
    exit 1
    } 

# idiomatic parameter and option handling in sh
while test $# -gt 0
do
    case "$1" in
        --clean) 
            CLEAN="true"
            ;;
        --env)
            shift
            ENV=$1
            ;;
        --*) 
            echo "Unexpected argument $1"
            display_usage
            ;;
    esac
    shift
done

# VALIDATION
if [[ -z $ENV ]]
then
    display_usage
fi

echo
echo $0 started

DEPLOY_FOLDER="${0%/*}"
CFTEMPLATE=cloudformation.yml
BUCKET=deployocp
KEY=$ENV/cloudformation/$CFTEMPLATE

STACK_NAME=$ENV

if [[ $skip != "true" ]]
then
    $DEPLOY_FOLDER/publishLambda.sh --env $ENV
fi

aws cloudformation create-stack --stack-name $STACK_NAME --template-url https://s3.amazonaws.com/$BUCKET/$KEY --capabilities CAPABILITY_NAMED_IAM
aws cloudformation wait stack-create-complete --stack-name $STACK_NAME
aws cloudformation describe-stacks --stack-name $STACK_NAME

echo $0 finished
echo
