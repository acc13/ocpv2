#!/bin/bash

BUCKET=deployocp
PROJ_ROOT="${0%/*}"
CFPROPS=cloudformation.yml
CFPROPS_PATH=$PROJ_ROOT/../deploy/cloudformation/$CFPROPS
KEY=cloudformation/$CFPROPS

TEST_STACK_NAME=test

aws s3 mb s3://$BUCKET
aws s3 cp $CFPROPS_PATH s3://$BUCKET/$KEY
aws cloudformation update-stack --stack-name $TEST_STACK_NAME --template-url https://s3.amazonaws.com/$BUCKET/$KEY --capabilities CAPABILITY_NAMED_IAM
aws cloudformation wait stack-update-complete --stack-name $TEST_STACK_NAME
aws cloudformation describe-stacks --stack-name $TEST_STACK_NAME

echo
echo Deployment ended
