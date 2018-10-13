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

echo $0 starting

DEPLOY_FOLDER="${0%/*}"
BUCKET=deployocp
INVITE_S3_KEY=$ENV/lambas/OCPv2-1.0.zip


#copy web content first
JAVA=$DEPLOY_FOLDER/../java
ZIP=$JAVA/ocvp2/build/distributions/OCPv2-1.0.zip
aws s3 cp $ZIP s3://$BUCKET/$INVITE_S3_KEY

echo $0 finished
