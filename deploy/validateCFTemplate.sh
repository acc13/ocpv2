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
CFTEMPLATE_PATH=$DEPLOY_FOLDER/cloudformation/$CFTEMPLATE
BUCKET=deployocp
KEY=$ENV/cloudformation/$CFTEMPLATE

TEST_STACK_NAME=test

aws s3 mb s3://$BUCKET
echo "bucket $BUCKET created"
aws s3 cp $CFTEMPLATE_PATH s3://$BUCKET/$KEY
echo "template $CFTEMPLATE uploaded"
aws cloudformation validate-template --template-url https://s3.amazonaws.com/$BUCKET/$KEY
echo "validation returned"
