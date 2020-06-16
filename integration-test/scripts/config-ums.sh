#!/usr/bin/env bash

date

echo "Profile file before config-ums"
cat integcb/Profile

# remove any UMS_HOST config in the Profile file
sed '/export UMS_HOST/d' ./integcb/Profile > ./integcb/Profile.tmp
mv ./integcb/Profile.tmp ./integcb/Profile

echo "Configuring context to use remote UMS"
echo "export UMS_HOST=ums.thunderhead-dev.cloudera.com" >> integcb/Profile

echo "Profile file after config-ums"
cat integcb/Profile

