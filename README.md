# ocpv2

## Introduction
Welcome to the Symantec Online Coding Problem system.
These are the resources for building a serverless application (AWS) hosting "take-home" online coding problems for software developer interviews.


## Overview


## Deployment

#### Configuring AWS Resources
AWS resources need to be provisioned and configured once.
This is done using the CloudFormation templates found in the "deploy" directory.
Templates can be deployed via the AWS console or CLI.

*Note: DNS/Rte53 config for the intended domain is not part of CF config - that needs to be set up separately*

#### Deploying the code
The static web pages for inviting candidates and the coding problems themselves are deployed using the .sh scripts found in the "deploy" directory.
Dependencies:
- AWS CLI
  - Must be run with an AWS IAM user authorized to update lambda and S3
  - zip utility
