#!/bin/bash

DEPLOY_FOLDER="${0%/*}"
BUCKET=deployocp
INVITE_S3_KEY=lambas/InviteCandidate-1.0.zip


#copy web content first
LAMBDAS=$DEPLOY_FOLDER/../lambdas
ZIP=$LAMBDAS/InviteCandidate/build/distributions/InviteCandidate-1.0.zip
aws s3 cp $ZIP s3://$BUCKET/$INVITE_S3_KEY
aws lambda update-function-code --function-name Ocpv2InviteCandidate --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY

echo
echo Deployment ended
