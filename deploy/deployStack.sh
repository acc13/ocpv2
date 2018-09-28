#!/bin/bash

display_usage() { 
	echo -e "\nUsage:\n$0 --env <environment/stack name> [--clean] \n" 
    exit 1
	} 

# idiomatic parameter and option handling in sh
while test $# -gt 0
do
    case "$1" in
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

DEPLOY_FOLDER="${0%/*}"
CFTEMPLATE=cloudformation.yml
BUCKET=deployocp
KEY=$ENV/cloudformation/$CFTEMPLATE

STACK_NAME=$ENV

#template validation must be run first, otherwise, new template not uploaded
aws cloudformation update-stack --stack-name $STACK_NAME --template-url https://s3.amazonaws.com/$BUCKET/$KEY --capabilities CAPABILITY_NAMED_IAM
aws cloudformation wait stack-update-complete --stack-name $STACK_NAME
aws cloudformation describe-stacks --stack-name $STACK_NAME

echo
echo Deployment ended
