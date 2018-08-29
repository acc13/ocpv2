#!/bin/bash
aws s3 sync ./web s3://test.yetanotherwhatever.io --content-type "text/html" --acl public-read

