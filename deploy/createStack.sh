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

BUCKET=deployocp
DEPLOY_DIR="${0%/*}"
CFPROPS=cloudformation.yml
CFPROPS_PATH=$DEPLOY_DIR/cloudformation/$CFPROPS
KEY=cloudformation/$CFPROPS

LAMBDAS=$DEPLOY_DIR/../lambdas
ZIP=$LAMBDAS/InviteCandidate/build/distributions/InviteCandidate-1.0.zip

STACK_NAME=$ENV

if [[ $skip != "true" ]]
then
	aws s3 mb s3://$BUCKET
	aws s3 cp $CFPROPS_PATH s3://$BUCKET/$KEY
	#aws s3 cp $ZIP s3://$BUCKET/$INVITE_S3_KEY
fi

aws cloudformation create-stack --stack-name $STACK_NAME --template-url https://s3.amazonaws.com/$BUCKET/$KEY --capabilities CAPABILITY_NAMED_IAM
aws cloudformation wait stack-create-complete --stack-name $STACK_NAME
aws cloudformation describe-stacks --stack-name $STACK_NAME

echo
echo Deployment ended
