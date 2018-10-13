#!/bin/bash

display_usage() { 
	echo -e "\nUsage:\n$0 --env <environment/stack name>\n" 
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

echo
echo $0 starting

DEPLOY_FOLDER="${0%/*}"
BUCKET=deployocp
INVITE_S3_KEY=$ENV/lambas/OCPv2-1.0.zip

$DEPLOY_FOLDER/publishLambda.sh --env $ENV

aws lambda update-function-code --function-name ${ENV}_RegisterIntern --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY
aws lambda update-function-code --function-name ${ENV}_InviteCandidate --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY
aws lambda update-function-code --function-name ${ENV}_TestOutput --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY
aws lambda update-function-code --function-name ${ENV}_NotifyCodeUploaded --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY
aws lambda update-function-code --function-name ${ENV}_GetTestOutputResult --s3-bucket $BUCKET --s3-key $INVITE_S3_KEY

echo $0 finished
echo
