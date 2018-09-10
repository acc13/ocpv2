#!/bin/bash

BUCKET=deployocp
PROJ_ROOT="${0%/*}"
CFPROPS=cloudformation.yml
CFPROPS_PATH=$PROJ_ROOT/../deploy/cloudformation/$CFPROPS
KEY=cloudformation/$CFPROPS

aws s3 mb s3://$BUCKET
aws s3 cp $CFPROPS_PATH s3://$BUCKET/$KEY
aws cloudformation update-stack --stack-name test --template-body s3://$BUCKET/$KEY

echo
echo Deployment ended
