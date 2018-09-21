#!/bin/bash

DEPLOY_FOLDER="${0%/*}"
CFTEMPLATE=cloudformation.yml
CFTEMPLATE_PATH=$DEPLOY_FOLDER/cloudformation/$CFTEMPLATE
BUCKET=deployocp
KEY=cloudformation/$CFTEMPLATE

TEST_STACK_NAME=test

aws s3 mb s3://$BUCKET
echo "bucket $BUCKET created"
aws s3 cp $CFTEMPLATE_PATH s3://$BUCKET/$KEY
echo "template $CFTEMPLATE uploaded"
aws cloudformation validate-template --template-url https://s3.amazonaws.com/$BUCKET/$KEY
echo "validation returned"
