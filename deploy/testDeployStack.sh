#!/bin/bash

DEPLOY_FOLDER="${0%/*}"
CFTEMPLATE=cloudformation.yml
BUCKET=deployocp
KEY=cloudformation/$CFTEMPLATE

TEST_STACK_NAME=test

#template validation must be run first, otherwise, new template not uploaded
aws cloudformation update-stack --stack-name $TEST_STACK_NAME --template-url https://s3.amazonaws.com/$BUCKET/$KEY --capabilities CAPABILITY_NAMED_IAM
aws cloudformation wait stack-update-complete --stack-name $TEST_STACK_NAME
aws cloudformation describe-stacks --stack-name $TEST_STACK_NAME

echo
echo Deployment ended
