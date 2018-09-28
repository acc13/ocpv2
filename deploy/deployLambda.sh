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
BUCKET=deployocp
INVITE_S3_KEY=$ENV/lambas/InviteCandidate-1.0.zip


#copy web content first
LAMBDAS=$DEPLOY_FOLDER/../lambdas
ZIP=$LAMBDAS/InviteCandidate/build/distributions/InviteCandidate-1.0.zip
aws s3 cp $ZIP s3://$BUCKET/$INVITE_S3_KEY
aws lambda update-function-code --function-name ${ENV}_InviteCandidate --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY
aws lambda update-function-code --function-name ${ENV}_TestOutput --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY
aws lambda update-function-code --function-name ${ENV}_NotifyCodeUploaded --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY
aws lambda update-function-code --function-name ${ENV}_GetTestOutputResult --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY

echo
echo Deployment ended
